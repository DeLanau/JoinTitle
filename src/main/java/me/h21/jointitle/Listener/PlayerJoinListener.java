package me.h21.jointitle.Listener;

import me.h21.jointitle.Config.Config;
import me.h21.jointitle.JTMain;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.rojo8399.placeholderapi.PlaceholderService;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.UUID;

public class PlayerJoinListener {

    @Listener
    public void playerJoinEvent(ClientConnectionEvent.Join e) {

        /**LuckPerms Utils**/

        LuckPermsApi api = LuckPerms.getApi();
        Node node = api.getNodeFactory().newBuilder("*").build();

        /**PlaceholderAPI Utils**/

        PlaceholderService phservice = JTMain.getInstance().getPlaceHolder();

        /**Misc Utils**/

        UUID id = e.getTargetEntity().getUniqueId();
        Player player = e.getTargetEntity();

        CommentedConfigurationNode config = Config.getConfig();

        /**Permissions booleans**/

        boolean data = api.getUserManager().loadUser(id).join().getCachedData().getPermissionData(Contexts.global()).getPermissionValue("jointitle.title.default").asBoolean();
        boolean data_vip = api.getUserManager().loadUser(id).join().getCachedData().getPermissionData(Contexts.global()).getPermissionValue("jointitle.title.vip").asBoolean();
        boolean data_admin = api.getUserManager().loadUser(id).join().getCachedData().getPermissionData(Contexts.global()).getPermissionValue("jointitle.title.admin").asBoolean();

        if (data == true) { //perm check

            e.getTargetEntity().sendTitle(Title.builder()
                    .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getDefaultSampel(), "#title:", "#")), player, null))
                    .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getDefaultSampel(), "#subtitle:", "#")), player, null))
                    .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getDefaultSampel(), "#actionbar:", "#")), player, null))
                    .stay(config.getNode("Default").getNode("Staytime").getInt())
                    .fadeIn(config.getNode("Default").getNode("FadeIn").getInt())
                    .fadeOut(config.getNode("Default").getNode("FadeOut").getInt())
                    .build());

        }

        if(data_vip == true){ //perm check

            e.getTargetEntity().sendTitle(Title.builder()
                    .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getVipSampel(), "#title:", "#")), player, null))
                    .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getVipSampel(), "#subtitle:", "#")), player, null))
                    .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getVipSampel(), "#actionbar:", "#")), player, null))
                    .stay(config.getNode("Vip").getNode("Staytime").getInt())
                    .fadeIn(config.getNode("Vip").getNode("FadeIn").getInt())
                    .fadeOut(config.getNode("Vip").getNode("FadeOut").getInt())
                    .build());

        }

        if(data_admin == true || api.getUserManager().loadUser(id).join().hasPermission(node).asBoolean() == true){ //perm and wildcard check

            e.getTargetEntity().sendTitle(Title.builder()
                    .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getAdminSampel(), "#title:", "#")), player, null))
                    .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getAdminSampel(), "#subtitle:", "#")), player, null))
                    .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getAdminSampel(), "#actionbar:", "#")), player, null))
                    .stay(config.getNode("Admin").getNode("Staytime").getInt())
                    .fadeIn(config.getNode("Admin").getNode("FadeIn").getInt())
                    .fadeOut(config.getNode("Admin").getNode("FadeOut").getInt())
                    .build());

        }

    }

}

