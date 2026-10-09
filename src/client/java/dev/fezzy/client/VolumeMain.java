package dev.fezzy.client;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.joml.Vector2i;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VolumeMain {

    private static final Map<UUID, VolumeMessage> VOLUME_MESSAGES = new HashMap<>();

    private static boolean nameTagsHidden = false;

    public static boolean handleMiddleClick() {
        Minecraft client = Minecraft.getInstance();

        if (client.mouseHandler.isMiddlePressed() && client.crosshairPickEntity instanceof AbstractClientPlayer target) {
            PlayerState state = ClientManager.getPlayerStateManager().getState(target.getUUID());
            if (state != null) {
                double prevVolume = VoicechatClient.PLAYER_VOLUME_CONFIG.getVolume(state.getUuid());
                double newVolume = prevVolume == 0.0 ? 1.0 : 0.0;
                VoicechatClient.PLAYER_VOLUME_CONFIG.setVolume(state.getUuid(), newVolume);
                VoicechatClient.PLAYER_VOLUME_CONFIG.save();
                String message = newVolume == 0.0 ? "-100%" : "+0%";
                VOLUME_MESSAGES.put(state.getUuid(), new VolumeMessage(Component.literal(message)));
                return true;

            }
        }
        return false;
    }

    public static boolean handleScroll(Vector2i vector) {
        Minecraft client = Minecraft.getInstance();

        if (client.mouseHandler.isRightPressed() && client.crosshairPickEntity instanceof AbstractClientPlayer target) {
            PlayerState state = ClientManager.getPlayerStateManager().getState(target.getUUID());
            if (state != null) {

                double prevVolume = VoicechatClient.PLAYER_VOLUME_CONFIG.getVolume(state.getUuid());
                double newVolume = Math.clamp(prevVolume + (prevVolume >= 1.0 ? 0.1 : 0.05) * (vector.y == 0 ? -vector.x : vector.y), 0.0, 4.0);
                VoicechatClient.PLAYER_VOLUME_CONFIG.setVolume(state.getUuid(), newVolume);
                VoicechatClient.PLAYER_VOLUME_CONFIG.save();
                int percent = (int) Math.round(100.0 * (newVolume - 1.0));
                VOLUME_MESSAGES.put(state.getUuid(), new VolumeMessage(Component.literal((percent >= 0 ? "+" + percent : String.valueOf(percent)) + "%")));
                return true;
            }
        }
        return false;
    }

    public static void toggleNameTagsHidden() {
        nameTagsHidden = !nameTagsHidden;
    }

    public static boolean shouldHideNameTag(AvatarRenderState state) {
        if (!nameTagsHidden) {
            return false;
        }

        Entity self = Minecraft.getInstance().player;
        return self == null || state.id != self.getId();
    }

    public static void modifyNamePlateText(AvatarRenderState state) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            Entity entity = world.getEntity(state.id);
            if (entity != null) {
                VolumeMessage volumeMessage = VOLUME_MESSAGES.get(entity.getUUID());
                if (volumeMessage != null && (System.currentTimeMillis() - volumeMessage.timestamp) < 3000L) {
                    state.nameTag = volumeMessage.text;
                }
            }
        }
    }

    private record VolumeMessage(Component text, long timestamp) {

        public VolumeMessage(Component text) {
            this(text, System.currentTimeMillis());
        }

    }
}