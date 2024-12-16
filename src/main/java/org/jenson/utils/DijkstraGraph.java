package org.jenson.utils;

import java.util.*;
import java.util.function.BiFunction;

public class DijkstraGraph<T> {
    private final Map<Pair<Integer, Integer>, DijkstraNode<T>> nodes = new HashMap<>();

    public Map<Pair<Integer, Integer>, DijkstraNode<T>> getNodes() {
        return nodes;
    }

    public static <T> DijkstraGraph<T> ofArrayUtils(ArrayUtils<T> map, BiFunction<T, T, Integer> stepSizeCalculator, BiFunction<T, T, Boolean> travelCondition) {
        DijkstraGraph<T> graph = new DijkstraGraph<>();
        List<Pair<Integer, Integer>> allCoordinates = map.allCoordinates();

        allCoordinates.forEach(coordinate -> {
            graph.nodes.put(coordinate, new DijkstraNode<>(map.get(coordinate.getFirst(), coordinate.getSecond())));
        });
        graph.nodes.forEach((coordinate, node) -> {
            int row = coordinate.getFirst();
            int col = coordinate.getSecond();

            T neighbour = map.northOrNull(row, col);
            if (neighbour != null && travelCondition.apply(node.position, neighbour)) {
                node.adjacentNodes.put(graph.nodes.get(Pair.of(row-1, col)), stepSizeCalculator.apply(node.position, neighbour));
            }
            neighbour = map.eastOrNull(row, col);
            if (neighbour != null && travelCondition.apply(node.position, neighbour)) {
                node.adjacentNodes.put(graph.nodes.get(Pair.of(row, col+1)), stepSizeCalculator.apply(node.position, neighbour));
            }
            neighbour = map.southOrNull(row, col);
            if (neighbour != null && travelCondition.apply(node.position, neighbour)) {
                node.adjacentNodes.put(graph.nodes.get(Pair.of(row+1, col)), stepSizeCalculator.apply(node.position, neighbour));
            }
            neighbour = map.westOrNull(row, col);
            if (neighbour != null && travelCondition.apply(node.position, neighbour)) {
                node.adjacentNodes.put(graph.nodes.get(Pair.of(row, col-1)), stepSizeCalculator.apply(node.position, neighbour));
            }
        });
        return graph;
    }

    public void calculateShortestPathFromSource(DijkstraNode<T> source) {
        source.distance = 0;

        Set<DijkstraNode<T>> settledNodes = new HashSet<>();
        Set<DijkstraNode<T>> unsettledNodes = new HashSet<>();
        DijkstraNode<T> currentNode;
        DijkstraNode<T> adjacentNode;
        int edgeWeight;

        unsettledNodes.add(source);
        do {
            currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            if (currentNode != null) {
                for (Map.Entry<DijkstraNode<T>, Integer> entry : currentNode.adjacentNodes.entrySet()) {
                    adjacentNode = entry.getKey();
                    edgeWeight = entry.getValue();
                    if (!settledNodes.contains(adjacentNode)) {
                        calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                        unsettledNodes.add(adjacentNode);
                    }
                }
                settledNodes.add(currentNode);
            }
        } while (!unsettledNodes.isEmpty());
    }

    private DijkstraNode<T> getLowestDistanceNode(Set<DijkstraNode<T>> unsettledNodes) {
        DijkstraNode<T> lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (DijkstraNode<T> node : unsettledNodes) {
            if (node.distance < lowestDistance) {
                lowestDistance = node.distance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(DijkstraNode<T> evaluationNode, int edgeWeight, DijkstraNode<T> sourceNode) {
        int sourceDistance = sourceNode.distance;
        if (sourceDistance + edgeWeight < evaluationNode.distance) {
            evaluationNode.distance = sourceDistance + edgeWeight;
        }
    }

    public static class DijkstraNode<T> {
        private final T position;
        private int distance = Integer.MAX_VALUE;
        private final Map<DijkstraNode<T>, Integer> adjacentNodes = new HashMap<>();

        public DijkstraNode(T position) {
            this.position = position;
        }

        public Map<DijkstraNode<T>, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }

        public T getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return position + ":" + distance;
        }
    }
}