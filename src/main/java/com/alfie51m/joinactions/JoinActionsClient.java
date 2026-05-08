package com.alfie51m.joinactions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class JoinActionsClient implements ClientModInitializer {

	private static boolean hasRunThisConnection = false;

	@Override
	public void onInitializeClient() {
		JoinActionsConfig.load();

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			Minecraft mc = Minecraft.getInstance();
			ServerData serverData = mc.getCurrentServer();
			if (serverData == null) return;

			JoinActionsConfig.load();
			if (hasRunThisConnection) return;

			String currentAddress = serverData.ip.toLowerCase();

			for (int i = 0; i < 4; i++) {
				String server = JoinActionsConfig.servers[i];
				String command = JoinActionsConfig.commands[i];

				if (server.isEmpty() || command.isEmpty()) continue;

				if (currentAddress.contains(server)) {
					hasRunThisConnection = true;

					final boolean[] executed = {false};
					ClientTickEvents.END_CLIENT_TICK.register(c -> {
						if (!executed[0]
								&& c.player != null
								&& c.player.connection != null) {

							c.player.connection.sendCommand(command);
							executed[0] = true;
						}
					});

					break; // only one match per join
				}
			}
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			hasRunThisConnection = false;
		});
	}
}