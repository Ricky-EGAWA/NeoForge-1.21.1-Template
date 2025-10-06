package com.big_face.head;

import com.mojang.serialization.Codec;
import com.big_face.TutorialMod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class HeadScaleAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TutorialMod.MOD_ID);

    public static final Supplier<AttachmentType<Float>> HEAD_SCALE = ATTACHMENT_TYPES.register(
        "head_scale",
        () -> AttachmentType.builder(() -> 1.0F)
            .serialize(Codec.FLOAT)
            .build()
    );
}