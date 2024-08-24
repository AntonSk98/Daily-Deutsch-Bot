package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.domain.CardActionState;
import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Cache to manage a {@link CardActionState}.
 *
 * @author Anton Skripin
 */
@Component
public class CardActionCache {

    private CardActionState cardActionState;

    public CardActionState getStateOrDefault(Supplier<CardDefinitionAction> initAction) {
        if (Objects.isNull(cardActionState)) {
            this.cardActionState = CardActionState.of(initAction.get());
        }
        return getState();
    }

    public CardActionState getState() {

        return cardActionState;
    }

    public void setState(CardActionState cardActionState) {
        this.cardActionState = cardActionState;
    }
}
