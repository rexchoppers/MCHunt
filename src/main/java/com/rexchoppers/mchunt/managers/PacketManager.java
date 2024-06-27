package com.rexchoppers.mchunt.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.rexchoppers.mchunt.MCHunt;

public class PacketManager {
    private final MCHunt plugin;

    public PacketManager(MCHunt plugin) {
        this.plugin = plugin;
    }

    public void registerPackets() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(
                new PacketAdapter(plugin, PacketType.Play.Server.OPEN_SIGN_EDITOR) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        ArenaSetupManager arenaSetupManager = PacketManager.this.plugin.getArenaSetupManager();

                        if (arenaSetupManager.getArenaSetupByPlayerUuid(
                                arenaSetupManager.getArenaSetups(),
                                event.getPlayer().getUniqueId()).isPresent()) {
                            event.setCancelled(true);
                        }
                    }
                }
        );
    }
}
