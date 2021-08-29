/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.domain;


import java.util.Optional;


/**
 * @author Daniel Slavik
 */
public class WorkflowSession {
    private Optional<String> currentNode;

    private final String definitionId;

    private boolean beingProcessed;

    public WorkflowSession(final String definitionId) {
        this.currentNode = Optional.empty();
        this.definitionId = definitionId;
        this.beingProcessed = false;
    }

    public WorkflowSession setCurrentNode(String currentNode) {
        this.currentNode = Optional.ofNullable(currentNode);
        return this;
    }

    public Optional<String> getCurrentNode() {
        return currentNode;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public synchronized void release() {
        beingProcessed = false;
    }

    public synchronized boolean tryToAllocate() {
        if (beingProcessed) {
            return false;
        }

        beingProcessed = true;
        return true;
    }

    public synchronized boolean isBeingProcessed() {
        return beingProcessed;
    }

}
