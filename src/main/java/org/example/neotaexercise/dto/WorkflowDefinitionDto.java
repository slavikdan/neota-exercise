/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Daniel Slavik
 */
public class WorkflowDefinitionDto {

    private final Source source;

    @JsonCreator
    public WorkflowDefinitionDto(
        @JsonProperty("source") final Source source
    ) {
        this.source = source;
    }

    public Source getSource() {
        return source;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}
        if (obj == this) {return true;}
        if (obj.getClass() != getClass()) {
            return false;
        }
        WorkflowDefinitionDto rhs = (WorkflowDefinitionDto) obj;
        return new EqualsBuilder()
            .append(this.source, rhs.source)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(source)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("source", source)
            .toString();
    }


    public static class Source {
        private final Map<String, Node> nodes;

        private final Map<String, Lane> lanes;

        private final Map<String, Link> links;

        @JsonCreator
        public Source(
            @JsonProperty("nodes") final Map<String, Node> nodes,
            @JsonProperty("lanes") final Map<String, Lane> lanes,
            @JsonProperty("links") final Map<String, Link> links
        ) {
            this.nodes = nodes;
            this.lanes = lanes;
            this.links = links;
        }

        public Map<String, Node> getNodes() {
            return nodes;
        }

        public Map<String, Lane> getLanes() {
            return lanes;
        }

        public Map<String, Link> getLinks() {
            return links;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {return false;}
            if (obj == this) {return true;}
            if (obj.getClass() != getClass()) {
                return false;
            }
            Source rhs = (Source) obj;
            return new EqualsBuilder()
                .append(this.nodes, rhs.nodes)
                .append(this.lanes, rhs.lanes)
                .append(this.links, rhs.links)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                .append(nodes)
                .append(lanes)
                .append(links)
                .toHashCode();
        }


        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append("nodes", nodes)
                .append("lanes", lanes)
                .append("links", links)
                .toString();
        }
    }

    public static class Node {

        private final String id;

        private final Type type;

        private final String name;

        @JsonCreator
        public Node(
            @JsonProperty("id") final String id,
            @JsonProperty("type") final Type type,
            @JsonProperty("name") final String name
        ) {
            this.id = id;
            this.type = type;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public Type getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {return false;}
            if (obj == this) {return true;}
            if (obj.getClass() != getClass()) {
                return false;
            }
            Node rhs = (Node) obj;
            return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.type, rhs.type)
                .append(this.name, rhs.name)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                .append(id)
                .append(type)
                .append(name)
                .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .append("name", name)
                .toString();
        }


        public enum Type {


            START_NODE("StartNode"),
            TASK_NODE("TaskNode"),
            END_NODE("EndNode"),
            NOP("NOP");

            private final String name;

            Type(String name) {
                this.name = name;
            }

            @JsonValue
            public String getName() {
                return name;
            }
        }


    }

    public static class Lane {
        private final String id;

        private final String name;

        private final List<String> nodes;

        @JsonCreator
        public Lane(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name,
            @JsonProperty("nodes") final List<String> nodes
        ) {
            this.id = id;
            this.name = name;
            this.nodes = nodes;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getNodes() {
            return nodes;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {return false;}
            if (obj == this) {return true;}
            if (obj.getClass() != getClass()) {
                return false;
            }
            Lane rhs = (Lane) obj;
            return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.name, rhs.name)
                .append(this.nodes, rhs.nodes)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(nodes)
                .toHashCode();
        }


        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("nodes", nodes)
                .toString();
        }
    }

    public static class Link {
        private final String id;

        private final String from;

        private final String to;

        @JsonCreator
        public Link(
            @JsonProperty("id") final String id,
            @JsonProperty("from") final String from,
            @JsonProperty("to") final String to
        ) {
            this.id = id;
            this.from = from;
            this.to = to;
        }

        public String getId() {
            return id;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {return false;}
            if (obj == this) {return true;}
            if (obj.getClass() != getClass()) {
                return false;
            }
            Link rhs = (Link) obj;
            return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.from, rhs.from)
                .append(this.to, rhs.to)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                .append(id)
                .append(from)
                .append(to)
                .toHashCode();
        }


        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append("id", id)
                .append("from", from)
                .append("to", to)
                .toString();
        }
    }

}
