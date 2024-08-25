package com.ansk.development.learngermanwithansk98.gateway;

import com.ansk.development.learngermanwithansk98.service.model.Navigation;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.ansk.development.learngermanwithansk98.service.model.Navigation.NEXT;
import static com.ansk.development.learngermanwithansk98.service.model.Navigation.PREVIOUS;

/**
 * Mapping utility class.
 *
 * @author Anton Skripin
 */
public class MapperUtils {

    public static Navigation map(Update update) {
        return Optional.ofNullable(update.getCallbackQuery())
                .map(CallbackQuery::getData)
                .map(data -> {
                    if (data.equals(PREVIOUS.getCommand())) {
                        return Navigation.previous();
                    }
                    if (data.equals(NEXT.getCommand())) {
                        return Navigation.next();
                    }

                    return null;
                }).orElse(null);
    }
}
