package me.ialistannen.roleassigner.command.system;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

/**
 * A simple command
 */
public interface Command {

    /**
     * @param keyword The keyword to check
     *
     * @return True if the keyword belongs to you
     */
    boolean isYourKeyword(String keyword);

    /**
     * @return The usage of the command
     */
    String getUsage();

    /**
     * @param event The event
     * @param message The message that triggered it
     * @param content The content of the message
     * @param channel The channel it occurred in
     */
    CommandResult execute(MessageReceivedEvent event, IMessage message, String content, IChannel channel);
}
