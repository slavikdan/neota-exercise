/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.db;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.example.neotaexercise.domain.WorkflowSession;


/**
 * @author Daniel Slavik
 */
public class WorkflowSessionRepository {
    private final Map<String, WorkflowSession> cache;

    public WorkflowSessionRepository() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void store(final String id, final WorkflowSession session) {
        cache.put(id, session);
    }

    public Optional<WorkflowSession> getSession(final String id) {
        return Optional.ofNullable(cache.get(id));
    }

    public Set<String> getAllSessions() {
        return cache.keySet();
    }

    public boolean existById(final String id) {
        return cache.containsKey(id);
    }
}
