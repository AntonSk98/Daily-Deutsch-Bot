package com.ansk.development.learngermanwithansk98.gateway.telegram;

import com.ansk.development.learngermanwithansk98.service.filter.FilterParameters;
import com.ansk.development.learngermanwithansk98.service.filter.IFilter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.ansk.development.learngermanwithansk98.gateway.telegram.TelegramMapper.input;
import static com.ansk.development.learngermanwithansk98.gateway.telegram.TelegramMapper.userId;

/**
 * A component that applies a chain of filters to a given input received from {@link DailyDeutschBotConsumer}.
 * It iterates through a list of filters to check whether the received input is allowed to be further processed.
 *
 * @author Anton Skripin
 */
@Component
public class FilterChain {

    private final List<IFilter> filters;

    /**
     * Constructor
     *
     * @param filters filters. See {@link IFilter}
     */
    public FilterChain(List<IFilter> filters) {
        this.filters = filters;
    }

    /**
     * Applies the chain of filters to the given {@link Update}.
     * This method iterates through each filter in the chain and applies it to the {@code update}.
     *
     * @param update the {@link Update} to be filtered.
     */
    public void filter(Update update) {
        FilterParameters filterParameters = new FilterParameters(
                userId(update),
                input(update)
        );

        filters.forEach(iFilter -> iFilter.filter(filterParameters));
    }
}
