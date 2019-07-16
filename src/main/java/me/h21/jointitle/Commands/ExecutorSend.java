package me.h21.jointitle.Commands;

import me.h21.jointitle.Config.Config;
import me.h21.jointitle.JTMain;
import me.rojo8399.placeholderapi.PlaceholderService;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.HashMap;
import java.util.Map;

public class ExecutorSend implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        CommentedConfigurationNode config = Config.getConfig();
        PlaceholderService phservice = JTMain.getInstance().getPlaceHolder();

        String title = args.<String>getOne("title").get();

        if(src instanceof ConsoleSource){

            throw new CommandException(Text.of("This command can only be run by player!"));
        }

        if(src instanceof Player) {

            Player player = (Player) src;

            if(title.toLowerCase() == "admin"){

                player.sendTitle(Title.builder()
                        .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getAdminSampel(), "#title:", "#")), player, null))
                        .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getAdminSampel(), "#subtitle:", "#")), player, null))
                        .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getAdminSampel(), "#actionbar:", "#")), player, null))
                        .stay(config.getNode("Admin").getNode("Staytime").getInt())
                        .fadeIn(config.getNode("Admin").getNode("FadeIn").getInt())
                        .fadeOut(config.getNode("Admin").getNode("FadeOut").getInt())
                        .build());

            }else if(title.toLowerCase() == "vip"){

                player.sendTitle(Title.builder()
                        .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getVipSampel(), "#title:", "#")), player, null))
                        .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getVipSampel(), "#subtitle:", "#")), player, null))
                        .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getVipSampel(), "#actionbar:", "#")), player, null))
                        .stay(config.getNode("Vip").getNode("Staytime").getInt())
                        .fadeIn(config.getNode("Vip").getNode("FadeIn").getInt())
                        .fadeOut(config.getNode("Vip").getNode("FadeOut").getInt())
                        .build());

            }else if(title.toLowerCase() == "default"){

                player.sendTitle(Title.builder()
                        .title(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getDefaultSampel(), "#title:", "#")), player, null))
                        .subtitle(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getDefaultSampel(), "#subtitle:", "#")), player, null))
                        .actionBar(phservice.replacePlaceholders(TextSerializers.FORMATTING_CODE.deserialize(StringUtils.substringBetween(Config.getDefaultSampel(), "#actionbar:", "#")), player, null))
                        .stay(config.getNode("Default").getNode("Staytime").getInt())
                        .fadeIn(config.getNode("Default").getNode("FadeIn").getInt())
                        .fadeOut(config.getNode("Default").getNode("FadeOut").getInt())
                        .build());

            }
        }

        return CommandResult.success();
    }
}
