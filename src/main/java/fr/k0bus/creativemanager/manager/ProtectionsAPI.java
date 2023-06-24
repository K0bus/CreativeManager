package fr.k0bus.creativemanager.manager;

import fr.k0bus.creativemanager.CreativeManager;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ProtectionsAPI {

    final CreativeManager plugin;
    final LandsIntegration lands;
    final GriefPrevention griefPrevention;


    public ProtectionsAPI(CreativeManager plugin)
    {
        this.plugin = plugin;
        this.lands = plugin.getLandsIntegration();
        this.griefPrevention = plugin.getGriefPrevention();
    }

    public boolean isMember(Player player, Location location)
    {
        return true;
    }

    public boolean WG(Player player, Location location)
    {
        return false;
    }
    public boolean GP(Player player, Location location)
    {
        if(griefPrevention == null) return false;
        Collection<Claim> claims = griefPrevention.dataStore.getClaims(location.getChunk().getX(), location.getChunk().getZ());
        for (Claim claim:claims) {
            if(claim.allowBuild(player, null) != null)
                return true;
        }
        return false;
    }

    public boolean Lands(Player player, Location location) {
        Land land = lands.getLand(location);
        if(land != null)
            return land.getTrustedPlayer(player.getUniqueId()) != null;
        return false;
    }
}
