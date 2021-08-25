/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.db;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.example.neotaexercise.domain.WorkflowDefinition;


/**
 * @author Daniel Slavik
 */
public class WorkflowRepository {
    private final Map<String, WorkflowDefinition> cache;

    public WorkflowRepository() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void store(final String id, final WorkflowDefinition definition) {
        cache.put(id, definition);
    }

    public Optional<WorkflowDefinition> getDefinition(final String id) {
        return Optional.ofNullable(cache.get(id));
    }

    public Set<String> getDefinitionIds() {
        return cache.keySet();
    }
}
