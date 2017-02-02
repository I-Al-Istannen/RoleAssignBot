package me.ialistannen.roleassigner.command;

import java.util.List;
import java.util.stream.Collectors;

import me.ialistannen.roleassigner.command.system.AbstractCommand;
import me.ialistannen.roleassigner.command.system.CommandResult;
import me.ialistannen.roleassigner.util.ConfigManager;
import me.ialistannen.roleassigner.util.MessageUtil;
import me.ialistannen.roleassigner.util.Util;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MessageBuilder.Styles;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * A command to set a role
 */
public class CommandAssignRole extends AbstractCommand {

    public CommandAssignRole() {
        super(
                ConfigManager.INSTANCE.getString("command.assignRole.keyword"),
                ConfigManager.INSTANCE.getString("command.assignRole.usage")
        );
    }

    @Override
    public CommandResult execute(MessageReceivedEvent event, IMessage message, String content, IChannel channel) {
        if (content.isEmpty()) {
            return CommandResult.SEND_USAGE;
        }
        List<IRole> rolesByName = channel.getGuild().getRoles()
                .stream()
                .filter(iRole -> iRole.getName().equalsIgnoreCase(content))
                .collect(Collectors.toList());

        if (rolesByName.isEmpty()) {
            MessageUtil.send(new MessageBuilder(message.getClient())
                    .withChannel(channel)
                    .appendContent("Could not find the role", Styles.ITALICS)
                    .appendContent(" ")
                    .appendContent(content, Styles.INLINE_CODE)
            );
            return CommandResult.OKAY;
        }

        if (rolesByName.size() > 1) {
            MessageUtil.send(new MessageBuilder(message.getClient())
                    .withChannel(channel)
                    .appendContent("Multiple roles with the name ", Styles.ITALICS)
                    .appendContent(" ")
                    .appendContent(content, Styles.INLINE_CODE)
                    .appendContent(" ")
                    .appendContent("found", Styles.ITALICS)
            );
            return CommandResult.OKAY;
        }

        List<String> allowedRoles = ConfigManager.INSTANCE.getList("allowed-roles", String.class);

        if (!Util.containsIgnoreCase(content, allowedRoles)) {
            MessageUtil.send(new MessageBuilder(message.getClient())
                    .withChannel(channel)
                    .appendContent("This role is blocked.", Styles.ITALICS)
            );
            return CommandResult.OKAY;
        }

        for (IRole iRole : channel.getGuild().getRoles()) {
            if (Util.containsIgnoreCase(iRole.getName(), allowedRoles)) {
                Util.doChecked(() -> message.getAuthor().removeRole(iRole));
            }
        }

        Util.doChecked(() -> {
            message.getAuthor().addRole(rolesByName.get(0));
            MessageUtil.send(new MessageBuilder(message.getClient())
                    .withChannel(channel)
                    .appendContent(message.getAuthor().mention())
                    .appendContent(" ")
                    .appendContent(", I assigned you the role", Styles.ITALICS)
                    .appendContent(" ")
                    .appendContent(rolesByName.get(0).getName(), Styles.INLINE_CODE)
            );
        }, throwable -> {
            if (throwable instanceof MissingPermissionsException) {
                MessageUtil.send(new MessageBuilder(message.getClient())
                        .withChannel(channel)
                        .appendContent("I do not have the permission to assign this role.", Styles.ITALICS)
                );
            }
            else {
                throwable.printStackTrace();
            }
        });
        return CommandResult.OKAY;
    }
}
