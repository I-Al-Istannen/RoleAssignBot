package me.ialistannen.roleassigner.command.system;

import java.util.Objects;

/**
 * A skeleton for Command
 */
public abstract class AbstractCommand implements Command {

    private String keyword, usage;

    /**
     * @param keyword The keyword
     * @param usage The usage
     */
    public AbstractCommand(String keyword, String usage) {
        Objects.requireNonNull(keyword, "keyword can not be null!");
        Objects.requireNonNull(usage, "usage can not be null!");
        
        this.keyword = keyword;
        this.usage = usage;
    }

    @Override
    public boolean isYourKeyword(String keyword) {
        return this.keyword.equalsIgnoreCase(keyword);
    }

    @Override
    public String getUsage() {
        return usage;
    }
}
