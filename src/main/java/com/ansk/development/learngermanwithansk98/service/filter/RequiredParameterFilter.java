package com.ansk.development.learngermanwithansk98.service.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3000)
public class RequiredParameterFilter implements IFilter {
    @Override
    public void filter(FilterParameters parameters) {

    }
}
