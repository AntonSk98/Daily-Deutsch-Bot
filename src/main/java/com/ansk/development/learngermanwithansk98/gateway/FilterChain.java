package com.ansk.development.learngermanwithansk98.gateway;

import com.ansk.development.learngermanwithansk98.service.filter.IFilter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * A component that applies a chain of filters to a given input received from {@link DailyDeutschBotConsumer}.
 * It iterates through a list of filters to check whether the received input is allowed.
 *
 * @author Anton Skripin
 */
@Component
public class FilterChain {

    private final List<IFilter> filters;

    public FilterChain(List<IFilter> filters) {
        this.filters = filters;
    }

    public void filter(Update update) {

    }
}
