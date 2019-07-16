package me.h21.jointitle;

import com.google.inject.Inject;
import me.h21.jointitle.Commands.ExecutorReload;
import me.h21.jointitle.Commands.ExecutorSend;
import me.h21.jointitle.Config.Config;
import me.h21.jointitle.Listener.PlayerJoinListener;
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
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandSpec;
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

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.spongepowered.api.Sponge.getGame;

@Plugin(id = "jointitle", name = "JoinTitle", authors = {"h21/DeLanau"}, description = "Join Title plugin recoded",
        dependencies = {@Dependency(id = "luckperms", optional = true), @Dependency(id = "placeholderapi", optional = false)})
public class JTMain {

    @Inject
    private Logger logger;

    private static PlaceholderService phservice;
    private static JTMain instance;

    public static JTMain getInstance(){
        return instance;
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File configFile;

    /**Game Init Events**/

    @Listener
    public void preInit(GamePreInitializationEvent event) {Config.buildConfig(loader, configFile);}

    @Listener
    public void Init(GameInitializationEvent event){createAndRegisterCmd();}
    @Listener
    public void postInit(GamePostInitializationEvent event){

        instance = this;

        Sponge.getEventManager().registerListeners(this, new PlayerJoinListener());

    }

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
    public void onReload(GameReloadEvent event){
        Config.reloadConfig();
        Sponge.getGame().getServer().getBroadcastChannel().send(Text.of(TextColors.GREEN, "Join Title config have been reloaded!"));
    }

    /** creating and registering commands**/

    private void createAndRegisterCmd(){

        Map arg1 = new HashMap<String, String> (){{put("admin","admin");put("vip","vip");put("default", "default");}};

        CommandSpec jt_main = CommandSpec.builder()
                .child(CommandSpec.builder()
                        .description(Text.of("Send title to player"))
                        .arguments(GenericArguments.choices(Text.of("title"), arg1))
                        .executor(new ExecutorSend()).build(), "send")
                .child(CommandSpec.builder()
                        .description(Text.of("Reloads join title config"))
                        .executor(new ExecutorReload()).build(), "reload")
                .permission("jointitle.commands.admin")
                .description(Text.of("Command for reloading config and sending title")).build();

        getGame().getCommandManager().register(this, jt_main, "jt", "jointitle");

    }

    public PlaceholderService getPlaceHolder(){
        return phservice;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader(){
        return loader;
    }

}