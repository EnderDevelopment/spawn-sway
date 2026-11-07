package com.enderdevelopment.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Main extends JavaPlugin implements Listener {
    private static final double BOB_HEIGHT = 0.08;
    private static final int BOB_PERIOD_TICKS = 40;

    private final Map<UUID, CameraSession> sessions = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            startCamera(player);
        }
    }

    @Override
    public void onDisable() {
        for (CameraSession session : sessions.values()) {
            session.task.cancel();
        }
        sessions.clear();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTask(this, () -> startCamera(event.getPlayer()));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTask(this, () -> startCamera(event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        stopCamera(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        CameraSession session = sessions.get(player.getUniqueId());
        if (session == null || session.teleporting) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) {
            event.setTo(from);
            return;
        }

        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()
                || from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch()) {
            event.setTo(session.anchor.clone());
        }
    }

    private void startCamera(Player player) {
        stopCamera(player.getUniqueId());
        Location anchor = player.getLocation().clone();
        CameraSession session = new CameraSession(anchor);
        sessions.put(player.getUniqueId(), session);
        session.task = Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (!player.isOnline() || !player.isValid()) {
                stopCamera(player.getUniqueId());
                return;
            }

            double phase = (session.tick % BOB_PERIOD_TICKS) * (Math.PI * 2.0 / BOB_PERIOD_TICKS);
            Location bobbed = session.anchor.clone().add(0.0, Math.sin(phase) * BOB_HEIGHT, 0.0);
            session.teleporting = true;
            player.teleport(bobbed);
            session.teleporting = false;
            session.tick++;
        }, 1L, 1L);
    }

    private void stopCamera(UUID playerId) {
        CameraSession session = sessions.remove(playerId);
        if (session != null) {
            session.task.cancel();
        }
    }

    private static final class CameraSession {
        private final Location anchor;
        private BukkitTask task;
        private int tick;
        private boolean teleporting;

        private CameraSession(Location anchor) {
            this.anchor = anchor;
        }
    }
}
