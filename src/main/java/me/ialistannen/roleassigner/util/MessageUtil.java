package me.ialistannen.roleassigner.util;

import sx.blah.discord.util.MessageBuilder;

/**
 * A util for message actions
 */
public class MessageUtil {

    /**
     * @param builder The {@link MessageBuilder}
     */
    public static void send(MessageBuilder builder) {
        Util.doChecked(builder::send);
    }
}
