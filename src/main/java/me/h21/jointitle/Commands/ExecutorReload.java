package me.h21.jointitle.Commands;

import me.h21.jointitle.Config.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ExecutorReload implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Config.reloadConfig();
        src.sendMessage(Text.of(TextColors.GREEN, "Join Title successfully reloaded"));

        return CommandResult.success();
    }
}
