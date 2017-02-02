package me.ialistannen.roleassigner.event;

import me.ialistannen.roleassigner.command.CommandAssignRole;
import me.ialistannen.roleassigner.command.system.CommandManager;
import me.ialistannen.roleassigner.command.system.CommandResult;
import me.ialistannen.roleassigner.util.MessageUtil;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MessageBuilder.Styles;

/**
 * A simple command listener
 */
public class CommandListener implements IListener<MessageReceivedEvent> {

    private final String prefix;

    static {
        CommandManager.INSTANCE.addCommand(new CommandAssignRole());
    }

    /**
     * @param prefix The prefix
     */
    public CommandListener(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        String content = event.getMessage().getContent();
        if (!content.startsWith(prefix)) {
            return;
        }

        String keyword = content.split(" ")[0].substring(prefix.length());
        String message = content.contains(" ") ? content.split(" ")[1] : "";

        IChannel channel = event.getMessage().getChannel();

        CommandManager.INSTANCE
                .getCommand(keyword)
                .ifPresent(command -> {
                    CommandResult result = command.execute(
                            event,
                            event.getMessage(),
                            message,
                            channel
                    );

                    if (result == CommandResult.SEND_USAGE) {
                        MessageUtil.send(
                                new MessageBuilder(event.getClient())
                                        .withChannel(channel)
                                        .appendContent("Usage:", Styles.BOLD_ITALICS)
                                        .appendContent(" ")
                                        .appendContent(command.getUsage(), Styles.INLINE_CODE)
                        );
                    }
                });
    }
}
