package com.ansk.development.learngermanwithansk98.service.filter;

import com.ansk.development.learngermanwithansk98.exception.NotTrustedUserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filter that checks whether a user is trusted before processing a command further.
 *
 * @author Anton Skripin
 */
@Component
@Order(1000)
public class CheckTrustedUserFilter implements IFilter {


    @Value("${bot.verified-user}")
    private long verifiedUser;

    @Override
    public void filter(FilterParameters parameters) {
        if (parameters.userId().equals(verifiedUser)) {
            return;
        }

        throw new NotTrustedUserException("Access denied.");
    }
}
