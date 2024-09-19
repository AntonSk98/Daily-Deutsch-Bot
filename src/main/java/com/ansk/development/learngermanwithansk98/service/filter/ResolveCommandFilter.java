package com.ansk.development.learngermanwithansk98.service.filter;

import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Filter that checks whether the incoming command is a known one or whether one of the commands is being already processed.
 *
 * @author Anton Skripin
 */
@Component
@Order(2000)
public class ResolveCommandFilter implements IFilter {

    private final CommandCache commandCache;

    /**
     * Constructor.
     *
     * @param commandCache See {@link CommandCache}
     */
    public ResolveCommandFilter(CommandCache commandCache) {
        this.commandCache = commandCache;
    }

    @Override
    public void filter(FilterParameters parameters) {
        Command.find(parameters.input())
                .map(cmd -> {
                    commandCache.clear();
                    return cmd;
                })
                .or(() -> Optional.ofNullable(commandCache.getCurrentCommand()))
                .map(cmd -> {
                    commandCache.setCurrentCommand(cmd);
                    return cmd;
                })
                .orElseThrow(() -> new IllegalStateException("Could not map to any known commands!"));

    }
}
