package com.gate.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SwordGateBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    // 0=左下, 1=右下, 2=左上, 3=右上
    public static final IntegerProperty PART = IntegerProperty.create("part", 0, 3);

    // 奥行きドア程度のシェイプ
    private static final VoxelShape SHAPE_NS = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape SHAPE_EW = Block.box(7, 0, 0, 9, 16, 16);

    public SwordGateBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1.0F)
                .noOcclusion()
                .noCollission());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return facing.getAxis() == Direction.Axis.Z ? SHAPE_NS : SHAPE_EW;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (!player.getInventory().contains(Items.NETHERITE_SWORD.getDefaultInstance())) {
                player.getInventory().add(Items.NETHERITE_SWORD.getDefaultInstance());
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide && state.getValue(PART) == 0) {
            // 左下ブロックが設置されたら、残りの3つも設置
            Direction facing = state.getValue(FACING);
            Direction widthDir = facing.getClockWise();

            // 右下
            level.setBlock(pos.relative(widthDir), state.setValue(PART, 1), 3);
            // 左上
            level.setBlock(pos.above(), state.setValue(PART, 2), 3);
            // 右上
            level.setBlock(pos.relative(widthDir).above(), state.setValue(PART, 3), 3);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            Direction facing = state.getValue(FACING);
            int part = state.getValue(PART);
            BlockPos basePos = getBasePos(pos, part, facing);
            Direction widthDir = facing.getClockWise();

            // 2×2全体を破壊
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    BlockPos checkPos = basePos.relative(widthDir, x).above(y);
                    if (!checkPos.equals(pos) && level.getBlockState(checkPos).is(this)) {
                        level.removeBlock(checkPos, false);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private BlockPos getBasePos(BlockPos pos, int part, Direction facing) {
        Direction widthDir = facing.getClockWise();
        BlockPos result = pos;

        // 横方向のオフセット（右側なら左へ）
        if (part == 1 || part == 3) {
            result = result.relative(widthDir.getOpposite());
        }

        // 縦方向のオフセット（上側なら下へ）
        if (part == 2 || part == 3) {
            result = result.below();
        }

        return result;
    }
}