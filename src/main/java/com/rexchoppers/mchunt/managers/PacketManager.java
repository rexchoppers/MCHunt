package com.rexchoppers.mchunt.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.rexchoppers.mchunt.MCHunt;
import org.bukkit.inventory.ItemStack;

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
                        ArenaSetupRepository arenaSetupManager = PacketManager.this.plugin.getArenaSetupManager();

                        if (arenaSetupManager.getArenaSetupForPlayer(event.getPlayer().getUniqueId()).isEmpty()) {
                            return;
                        }

                        if (event.getPlayer().getInventory().getItemInMainHand().getType().isAir()) {
                            return;
                        }

                        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
                        String action = PacketManager.this.plugin.getItemManager().getItemAction(itemInHand);

                        if (action == null) {
                            return;
                        }

                        if (action.equals("mchunt.setup.arenaSign")) {
                            event.setCancelled(true);
                        }
                    }
                }
        );
    }
}
