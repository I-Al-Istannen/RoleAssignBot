package me.ialistannen.roleassigner.util;

import java.util.List;
import java.util.function.Consumer;

/**
 * Some static utility functions
 */
public class Util {

    /**
     * @param runnable The {@link CheckedRunnable} to run
     * @param onError What to do in case of an error. Null for just print it
     */
    public static void doChecked(CheckedRunnable runnable, Consumer<Throwable> onError) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            if (onError != null) {
                onError.accept(throwable);
            }
            else {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * @param runnable The {@link CheckedRunnable} to run
     *
     * @see #doChecked(CheckedRunnable, Consumer)
     */
    public static void doChecked(CheckedRunnable runnable) {
        doChecked(runnable, null);
    }

    /**
     * @param string The string to search
     * @param list The list
     *
     * @return True if the string is in the list
     */
    public static boolean containsIgnoreCase(String string, List<String> list) {
        return list.stream().anyMatch(s -> s.equalsIgnoreCase(string));
    }

    public interface CheckedRunnable {

        /**
         * the action to execute
         *
         * @throws Throwable Any exception that may occur
         */
        void run() throws Throwable;
    }
}
