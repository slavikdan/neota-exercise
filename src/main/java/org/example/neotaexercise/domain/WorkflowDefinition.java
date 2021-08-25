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

    //TODO for all this there should be validation
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

    public boolean isNextNodeInDifferentLine(final String nodeId) {
        return !lanes.get(nodeId).getId().equals(lanes.get(lines.get(nodeId)).getId());
    }

    public WorkflowDefinitionDto.Lane getLaneInFrontOfNode(final String nodeId) {
        return lanes.get(lines.get(nodeId));
    }
}
