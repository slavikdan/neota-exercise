/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.domain;

import java.util.Map;
import java.util.Optional;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static java.util.stream.Collectors.toMap;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.END_NODE;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.START_NODE;


/**
 * @author Daniel Slavik
 */
public class WorkflowDefinition {

    private final String startingNode;

    private final String endNode;

    private final Map<String, String> lines;

    private final Map<String, WorkflowDefinitionDto.Node> nodes;

    private final Map<String, WorkflowDefinitionDto.Lane> lanes;

    public WorkflowDefinition(final WorkflowDefinitionDto dto) {
        this.lines = dto.getSource().getLinks().values().stream()
            .map(line -> Map.entry(line.getFrom(), line.getTo()))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.nodes = dto.getSource().getNodes();
        this.lanes = dto.getSource().getLanes().values().stream()
            .flatMap(lane -> lane.getNodes().stream()
                .map(node -> Map.entry(node, lane))
            ).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        this.startingNode = dto.getSource().getNodes().values().stream()
            .filter(n -> START_NODE.equals(n.getType()))
            .map(WorkflowDefinitionDto.Node::getId)
            .findAny()
            .orElseThrow();

        this.endNode = dto.getSource().getNodes().values().stream()
            .filter(n -> END_NODE.equals(n.getType()))
            .map(WorkflowDefinitionDto.Node::getId)
            .findAny()
            .orElseThrow();
    }

    public WorkflowDefinition(
        String startingNode,
        String endNode,
        Map<String, String> lines,
        Map<String, WorkflowDefinitionDto.Node> nodes,
        Map<String, WorkflowDefinitionDto.Lane> lanes
    ) {
        this.startingNode = startingNode;
        this.endNode = endNode;
        this.lines = lines;
        this.nodes = nodes;
        this.lanes = lanes;
    }

    public String getStartingNode() {
        return startingNode;
    }

    public String getEndNode() {
        return endNode;
    }

    public Optional<WorkflowDefinitionDto.Node> getNode(final String id) {
        return Optional.ofNullable(nodes.get(id));
    }

    public Optional<String> getNextNode(final String id) {
        return Optional.ofNullable(lines.get(id));
    }

    public Optional<WorkflowDefinitionDto.Lane> getNodeLane(final String id) {
        return Optional.ofNullable(lanes.get(id));
    }

    public boolean isNodeInDifferentLane(final String nodeId1, final String nodeId2) {
        return !lanes.get(nodeId1).getId().equals(lanes.get(nodeId2).getId());
    }
}
