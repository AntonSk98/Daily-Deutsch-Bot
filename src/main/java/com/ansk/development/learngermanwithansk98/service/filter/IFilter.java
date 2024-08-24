package com.ansk.development.learngermanwithansk98.service.filter;

/**
 * Interface that is used to filter an incoming message / command from a client.
 *
 * @author Anton Skripin
 */
public interface IFilter {
    void filter(FilterParameters parameters);
}
