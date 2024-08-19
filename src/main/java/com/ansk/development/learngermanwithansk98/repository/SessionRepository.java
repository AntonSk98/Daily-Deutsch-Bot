package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.domain.Session;
import com.ansk.development.learngermanwithansk98.service.Action;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;

@Component
public class SessionRepository {

    private Session session;

    public Session getSessionOrDefault(Supplier<Action> initAction) {
        if (Objects.isNull(session)) {
            this.session = Session.of(initAction.get());
        }
        return getSession();
    }

    public Session getSession() {

        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
