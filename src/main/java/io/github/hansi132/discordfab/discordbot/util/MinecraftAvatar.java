package io.github.hansi132.discordfab.discordbot.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MinecraftAvatar {
    public static final String API_URL = "https://crafatar.com/";

    public static String generateUrl(@NotNull final UUID uuid,
                                     @NotNull final RenderType renderType,
                                     @NotNull final RenderType.Model model,
                                     final int size,
                                     final int scale,
                                     final boolean overlay) {
        final StringBuilder builder = new StringBuilder(API_URL)
                .append(renderType.code).append('/').append(uuid);

        if (renderType == RenderType.TEXTURE || renderType == RenderType.CAPE) {
            return builder.toString();
        }

        if (renderType.modifier.size) {
            builder.append("?size=").append(size);
        }

        if (renderType.modifier.scale) {
            builder.append("&scale=").append(scale);
        }

        if (renderType.modifier.overlay && overlay) {
            builder.append("&overlay");
        }

        if (renderType.modifier.model) {
            builder.append("&default=").append(model.code);
        }

        return builder.toString();
    }

    public enum RenderType {
        AVATAR("Avatar", "avatars", Modifier.ALL),
        HEAD("Head", "renders/head", Modifier.ALL),
        BODY("Body", "renders/body", Modifier.ALL),
        CAPE("Cape", "cape", Modifier.MODEL),
        TEXTURE("Texture", "skins", Modifier.MODEL);

        private final String name;
        private final String code;
        private final Modifier modifier;

        RenderType(final String name, final String code, final Modifier modifier) {
            this.name = name;
            this.code = code;
            this.modifier = modifier;
        }

        public String getName() {
            return this.name;
        }

        public Modifier getModifier() {
            return this.modifier;
        }

        @Nullable
        public static MinecraftAvatar.RenderType getByName(@NotNull final String name) {
            for (MinecraftAvatar.RenderType value : values()) {
                if (name.equalsIgnoreCase(value.name)) {
                    return value;
                }
            }

            return null;
        }

        enum Modifier {
            ALL(true, true, true, true),
            MODEL(false, false, false, true);

            private final boolean size;
            private final boolean scale;
            private final boolean overlay;
            private final boolean model;

            Modifier(boolean size, boolean scale, boolean overlay, boolean model) {
                this.size = size;
                this.scale = scale;
                this.overlay = overlay;
                this.model = model;
            }
        }

        public enum Model {
            DEFAULT("Steve", "MHF_Steve"),
            SLIM("Alex", "MHF_Alex");

            private final String name;
            private final String code;

            Model(final String name, final String code) {
                this.name = name;
                this.code = code;
            }
        }
    }
}
