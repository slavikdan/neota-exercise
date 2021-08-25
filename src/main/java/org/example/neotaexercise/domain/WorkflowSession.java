/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.domain;


/**
 * @author Daniel Slavik
 */
public class WorkflowSession {
    private final String currentNode;

    private final String definitionId;

    private boolean beingProcessed;

    public WorkflowSession(final String currentNode, final String definitionId) {
        this.currentNode = currentNode;
        this.definitionId = definitionId;
        this.beingProcessed = false;
    }

    public String getCurrentNode() {
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
