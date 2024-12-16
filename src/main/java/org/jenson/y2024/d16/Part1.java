package org.jenson.y2024.d16;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Pair;
import org.jenson.utils.Reader;

import java.util.*;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(16);

        ArrayUtils<Field> map = ArrayUtils.positionAwareMap(Reader.readFileLineByLineToText(2024, 16).toList(), Field::new);
        Field start = map.cells().filter(field -> field.value == 'S').findFirst().orElseThrow();
        DirectionalDijkstraGraph<Field> graph = DirectionalDijkstraGraph.ofArrayUtils(map);
        graph.calculateShortestPathFromSource(graph.getNodes().get(Pair.of(Pair.of(start.x(), start.y()), DirectionalDijkstraGraph.Direction.EAST)));
        graph.getNodes().values().stream().filter(node -> node.position.value == 'E').map(node -> node.distance).min(Integer::compareTo).ifPresent(System.out::println);
    }

    private record Field(int x, int y, char value) {
        public boolean isWall() {
            return value == '#';
        }
    }

    private static class DirectionalDijkstraGraph<T> {
        private final Map<Pair<Pair<Integer, Integer>, Direction>, DijkstraNode<T>> nodes = new HashMap<>();

        public Map<Pair<Pair<Integer, Integer>, Direction>, DijkstraNode<T>> getNodes() {
            return nodes;
        }

        public static DirectionalDijkstraGraph<Field> ofArrayUtils(ArrayUtils<Field> map) {
            DirectionalDijkstraGraph<Field> graph = new DirectionalDijkstraGraph<>();
            List<Pair<Integer, Integer>> allCoordinates = map.allCoordinates();

            allCoordinates.forEach(coordinate -> {
                for (Direction value : Direction.values()) {
                    graph.nodes.put(Pair.of(coordinate, value), new DijkstraNode<>(map.get(coordinate.getFirst(), coordinate.getSecond())));
                }
            });
            graph.nodes.forEach((coordinate, node) -> {
                int row = coordinate.getFirst().getFirst();
                int col = coordinate.getFirst().getSecond();
                Direction direction = coordinate.getSecond();

                Field neighbour = map.northOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.SOUTH) {
                    if (direction == Direction.NORTH) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row-1, col), Direction.NORTH)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row-1, col), Direction.NORTH)), 1001);
                    }
                }
                neighbour = map.eastOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.WEST) {
                    if (direction == Direction.EAST) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col+1), Direction.EAST)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col+1), Direction.EAST)), 1001);
                    }
                }
                neighbour = map.southOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.NORTH) {
                    if (direction == Direction.SOUTH) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row+1, col), Direction.SOUTH)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row+1, col), Direction.SOUTH)), 1001);
                    }
                }
                neighbour = map.westOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.EAST) {
                    if (direction == Direction.WEST) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col-1), Direction.WEST)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col-1), Direction.WEST)), 1001);
                    }
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

        private static class DijkstraNode<T> {
            private final T position;
            private int distance = Integer.MAX_VALUE;
            private final Map<DijkstraNode<T>, Integer> adjacentNodes = new HashMap<>();

            public DijkstraNode(T position) {
                this.position = position;
            }

            @Override
            public String toString() {
                return position + ":" + distance;
            }
        }

        public enum Direction {
            NORTH, EAST, SOUTH, WEST
        }
    }
}
