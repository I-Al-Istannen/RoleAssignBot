package me.ialistannen.roleassigner.command.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The command manager
 */
public enum CommandManager {
    INSTANCE;


    private List<Command> commandList = new ArrayList<>();

    /**
     * Adds a command
     *
     * @param command The {@link Command} to add
     */
    public void addCommand(Command command) {
        commandList.add(command);
    }

    /**
     * @param keyword The keyword of the command
     *
     * @return The command, if found
     */
    public Optional<Command> getCommand(String keyword) {
        return commandList.stream()
                .filter(command -> command.isYourKeyword(keyword))
                .findAny();
    }
}
