package me.h21.jointitle;

import com.google.inject.Inject;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.rojo8399.placeholderapi.PlaceholderService;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.spongepowered.api.Sponge.getGame;

@Plugin(id = "jointitle", name = "JoinTitle", authors = {"h21/DeLanau"}, dependencies = {@Dependency(id = "luckperms", optional = true), @Dependency(id = "placeholderapi", optional = false)})
public class JTMain {

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private CommentedConfigurationNode config;

    @Inject
    private Logger logger;

    /** String creating**/

    public String def;
    public String def_vip;
    public String def_admin;

    /**Game Events**/

    @Listener
    public void preInit(GamePreInitializationEvent event) { //creating config
        try {
            config = loader.load();

            /**String assignment thx to dualspiral**/

            def = config.getNode("Default").getNode("Text").getString();
            def_vip = config.getNode("Vip").getNode("Text").getString();
            def_admin = config.getNode("Admin").getNode("Text").getString();

            if (!defaultConfig.toFile().exists()) {

                //default config

                config.getNode("Default").getNode("Text").setValue("#title:title#subtitle:subtitle#actionbar:actionbar#").setComment("Your text need to be between :and#. For ex title:Welcome to my server#");

                config.getNode("Default").getNode("Staytime").setValue(202).setComment("Sets the duration in ticks how long the title should stay on. Default 202ticks = 10sec.");
                config.getNode("Default").getNode("FadeIn").setValue(101).setComment("Sets the duration in ticks of the fade in effect of the title. Default 101ticks = 5sec.");
                config.getNode("Default").getNode("FadeOut").setValue(101).setComment("Sets the duration in ticks of the fade out effect of the title. Default 101ticks = 5sec.");

                //vip config

                config.getNode("Vip").getNode("Text").setValue("#title:title#subtitle:subtitle#actionbar:actionbar#").setComment("Your text need to be between :and#. For ex title:Welcome to my server#");

                config.getNode("Vip").getNode("Staytime").setValue(202).setComment("Sets the duration in ticks how long the title should stay on. Default 202ticks = 10sec.");
                config.getNode("Vip").getNode("FadeIn").setValue(101).setComment("Sets the duration in ticks of the fade in effect of the title. Default 101ticks = 5sec.");
                config.getNode("Vip").getNode("FadeOut").setValue(101).setComment("Sets the duration in ticks of the fade out effect of the title. Default 101ticks = 5sec.");

                //admin config

                config.getNode("Admin").getNode("Text").setValue("#title:title#subtitle:subtitle#actionbar:actionbar#").setComment("Your text need to be between :and#. For ex title:Welcome to my server#");

                config.getNode("Admin").getNode("Staytime").setValue(202).setComment("Sets the duration in ticks how long the title should stay on. Default 202ticks = 10sec.");
                config.getNode("Admin").getNode("FadeIn").setValue(101).setComment("Sets the duration in ticks of the fade in effect of the title. Default 101ticks = 5sec.");
                config.getNode("Admin").getNode("FadeOut").setValue(101).setComment("Sets the duration in ticks of the fade out effect of the title. Default 101ticks = 5sec.");

                loader.save(config);
            }
        } catch (IOException e) {
            logger.warn("Error loading default configuration!");
        }

    }

    @Listener
    public void Init(GameInitializationEvent event){createAndRegisterCmd();}
    @Listener
    public void postInit(GamePostInitializationEvent event){}

    /**Server Events**/

    @Listener
    public void onServerStart(GameStartedServerEvent event){
        phservice = Sponge.getServiceManager().getRegistration(PlaceholderService.class).get().getProvider(); //PlaceholderApi utils
        logger.info("JoinTitle have been started");
    }
    @Listener
    public void onServerStop(GameStoppedEvent event){
        logger.info("JoinTitle have been stopped");
    }
    @Listener
    public void onReload(GameReloadEvent event) throws IOException { //config reload
        try {
            config = loader.load();
            Sponge.getGame().getServer().getBroadcastChannel().send(Text.of(TextColors.GREEN, "JoinTitle Config have been reloaded!"));
        }catch (IOException e){
            logger.warn("Error reloading config!");
        }
    }

    /**PlaceholderApi Utils**/

    private static PlaceholderService phservice;

    /**Player Join event**/

    @Listener
    public void playerJoinEvent(ClientConnectionEvent.Join e) {

        /**LuckPerms Utils**/

        LuckPermsApi api = LuckPerms.getApi();
        Node node = api.getNodeFactory().newBuilder("*").build();

        /**Sponge Utils**/

        UUID id = e.getTargetEntity().getUniqueId();
        Player player = e.getTargetEntity();

        /**Permissions booleans**/

        boolean data = api.getUserManager().loadUser(id).join().getCachedData().getPermissionData(Contexts.global()).getPermissionValue("jointitle.title.default").asBoolean();
        boolean data_vip = api.getUserManager().loadUser(id).join().getCachedData().getPermissionData(Contexts.global()).getPermissionValue("jointitle.title.vip").asBoolean();
        boolean data_admin = api.getUserManager().loadUser(id).join().getCachedData().getPermissionData(Contexts.global()).getPermissionValue("jointitle.title.admin").asBoolean();

        if (data == true) { //perm check

            e.getTargetEntity().sendTitle(Title.builder()
                    .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def.toString(), "#title:", "#")), player, null))
                    .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def, "#subtitle:", "#")), player, null))
                    .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def, "#actionbar:", "#")), player, null))
                    .stay(config.getNode("Default").getNode("Staytime").getInt())
                    .fadeIn(config.getNode("Default").getNode("FadeIn").getInt())
                    .fadeOut(config.getNode("Default").getNode("FadeOut").getInt())
                    .build());

        }

        if(data_vip == true){ //perm check

            e.getTargetEntity().sendTitle(Title.builder()
                    .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_vip, "#title:", "#")), player, null))
                    .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_vip, "#subtitle:", "#")), player, null))
                    .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_vip, "#actionbar:", "#")), player, null))
                    .stay(config.getNode("Vip").getNode("Staytime").getInt())
                    .fadeIn(config.getNode("Vip").getNode("FadeIn").getInt())
                    .fadeOut(config.getNode("Vip").getNode("FadeOut").getInt())
                    .build());

        }

        if(data_admin == true || api.getUserManager().loadUser(id).join().hasPermission(node).asBoolean() == true){ //perm check and wildcard check

            e.getTargetEntity().sendTitle(Title.builder()
                    .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_admin, "#title:", "#")), player, null))
                    .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_admin, "#subtitle:", "#")), player, null))
                    .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_admin, "#actionbar:", "#")), player, null))
                    .stay(config.getNode("Admin").getNode("Staytime").getInt())
                    .fadeIn(config.getNode("Admin").getNode("FadeIn").getInt())
                    .fadeOut(config.getNode("Admin").getNode("FadeOut").getInt())
                    .build());

        }

    }

    /** creating and registering commands**/

    private void createAndRegisterCmd(){

        Map arg1 = new HashMap<String, String> (){{put("admin","admin");put("vip","vip");put("default", "default");}};

        CommandSpec jt_send_title = CommandSpec.builder() //creating send cmd
                .description(Text.of("Send title to player"))
                .arguments(GenericArguments.choices(Text.of("title"), arg1))
                .executor((CommandSource src, CommandContext args) -> {

                    String title = args.<String>getOne("title").get();

                    Player player = (Player) src;

                    if(src instanceof Player) {

                    if(title.toLowerCase() == "admin"){

                        player.sendTitle(Title.builder()
                                .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_admin, "#title:", "#")), player, null))
                                .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_admin, "#subtitle:", "#")), player, null))
                                .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_admin, "#actionbar:", "#")), player, null))
                                .stay(config.getNode("Admin").getNode("Staytime").getInt())
                                .fadeIn(config.getNode("Admin").getNode("FadeIn").getInt())
                                .fadeOut(config.getNode("Admin").getNode("FadeOut").getInt())
                                .build());

                    }else if(title.toLowerCase() == "vip"){

                        player.sendTitle(Title.builder()
                                .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_vip, "#title:", "#")), player, null))
                                .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_vip, "#subtitle:", "#")), player, null))
                                .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def_vip, "#actionbar:", "#")), player, null))
                                .stay(config.getNode("Vip").getNode("Staytime").getInt())
                                .fadeIn(config.getNode("Vip").getNode("FadeIn").getInt())
                                .fadeOut(config.getNode("Vip").getNode("FadeOut").getInt())
                                .build());

                    }else if(title.toLowerCase() == "default"){

                        player.sendTitle(Title.builder()
                                .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def, "#title:", "#")), player, null))
                                .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def, "#subtitle:", "#")), player, null))
                                .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(def, "#actionbar:", "#")), player, null))
                                .stay(config.getNode("Default").getNode("Staytime").getInt())
                                .fadeIn(config.getNode("Default").getNode("FadeIn").getInt())
                                .fadeOut(config.getNode("Default").getNode("FadeOut").getInt())
                                .build());

                    }
                    }

                    return CommandResult.success();
                })
                .build();

        CommandSpec jt_reload = CommandSpec.builder() //creating reload cmd
                .description(Text.of("Reloads join title config"))
                .executor((CommandSource src, CommandContext args) -> {

                    try{
                        config = loader.load();
                        src.sendMessage(Text.of(TextColors.GREEN, "Config have been reloaded!"));
                    }catch (IOException e){
                        logger.warn("Error reloading config!");
                        src.sendMessage(Text.of(TextColors.RED, "Error reloading config!"));
                    }

                    return CommandResult.success();
                })
                .build();

        CommandSpec jt_main = CommandSpec.builder() //creating and registering main cmd
                .description(Text.of("Command for reloading config and sending title"))
                .permission("jointitle.commands.admin")
                .child(jt_reload,"reload")
                .child(jt_send_title, "send")
                .build();

        getGame().getCommandManager().register(this, jt_main, "jt", "jointitle");

    }

}
