package dev.fezzy.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class VolumescrollClient implements ClientModInitializer {

	private static final KeyMapping HIDE_NAME_TAGS_KEY = new KeyMapping(
			"key.volumescroll.hide_name_tags",
			InputConstants.Type.KEYBOARD,
			InputConstants.KEY_MINUS,
			KeyMapping.Category.MULTIPLAYER
	);

	@Override
	public void onInitializeClient() {
		KeyMappingHelper.registerKeyMapping(HIDE_NAME_TAGS_KEY);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (HIDE_NAME_TAGS_KEY.consumeClick()) {
				VolumeMain.toggleNameTagsHidden();
			}
		});
	}
}
