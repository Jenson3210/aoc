package org.jenson.y2024.d16;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Pair;
import org.jenson.utils.Reader;

import java.util.*;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(16);

        ArrayUtils<Field> map = ArrayUtils.positionAwareMap(Reader.readFileLineByLineToText(2024, 16).toList(), Field::new);
        Field start = map.cells().filter(field -> field.value == 'S').findFirst().orElseThrow();
        DirectionalDijkstraGraph graph = DirectionalDijkstraGraph.ofArrayUtils(map);
        graph.calculateShortestPathFromSource(graph.getNodes().get(Pair.of(Pair.of(start.x(), start.y()), DirectionalDijkstraGraph.Direction.EAST)));
        HashSet<DirectionalDijkstraGraph.DijkstraNode> paths = graph.getNodes().values().stream().filter(node -> node.position.value == 'E').min(Comparator.comparing(DirectionalDijkstraGraph.DijkstraNode::getDistance)).map(node -> {
            HashSet<DirectionalDijkstraGraph.DijkstraNode> bestPathNodes = new HashSet<>();
            node.collectBestPathNodes(graph, bestPathNodes);
            return bestPathNodes;
        }).orElseThrow();

        long count = paths.stream().map(DirectionalDijkstraGraph.DijkstraNode::getPosition).distinct().count();
        System.out.println(count);
    }

    private record Field(int x, int y, char value) {
        public boolean isWall() {
            return value == '#';
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static class DirectionalDijkstraGraph {
        private final Map<Pair<Pair<Integer, Integer>, Direction>, DijkstraNode> nodes = new HashMap<>();

        public Map<Pair<Pair<Integer, Integer>, Direction>, DijkstraNode> getNodes() {
            return nodes;
        }

        public static DirectionalDijkstraGraph ofArrayUtils(ArrayUtils<Field> map) {
            DirectionalDijkstraGraph graph = new DirectionalDijkstraGraph();
            List<Pair<Integer, Integer>> allCoordinates = map.allCoordinates();

            allCoordinates.forEach(coordinate -> {
                for (Direction value : Direction.values()) {
                    graph.nodes.put(Pair.of(coordinate, value), new DijkstraNode(map.get(coordinate.getFirst(), coordinate.getSecond())));
                }
            });
            graph.nodes.forEach((coordinate, node) -> {
                int row = coordinate.getFirst().getFirst();
                int col = coordinate.getFirst().getSecond();
                Direction direction = coordinate.getSecond();

                Field neighbour = map.northOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.SOUTH) {
                    if (direction == Direction.NORTH) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row - 1, col), Direction.NORTH)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row - 1, col), Direction.NORTH)), 1001);
                    }
                }
                neighbour = map.eastOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.WEST) {
                    if (direction == Direction.EAST) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col + 1), Direction.EAST)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col + 1), Direction.EAST)), 1001);
                    }
                }
                neighbour = map.southOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.NORTH) {
                    if (direction == Direction.SOUTH) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row + 1, col), Direction.SOUTH)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row + 1, col), Direction.SOUTH)), 1001);
                    }
                }
                neighbour = map.westOrNull(row, col);
                if (neighbour != null && !neighbour.isWall() && direction != Direction.EAST) {
                    if (direction == Direction.WEST) {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col - 1), Direction.WEST)), 1);
                    } else {
                        node.adjacentNodes.put(graph.nodes.get(Pair.of(Pair.of(row, col - 1), Direction.WEST)), 1001);
                    }
                }
            });
            return graph;
        }

        public void calculateShortestPathFromSource(DijkstraNode source) {
            source.distance = 0;

            Set<DijkstraNode> settledNodes = new HashSet<>();
            Set<DijkstraNode> unsettledNodes = new HashSet<>();
            DijkstraNode currentNode;
            DijkstraNode adjacentNode;
            int edgeWeight;

            unsettledNodes.add(source);
            do {
                currentNode = getLowestDistanceNode(unsettledNodes);
                unsettledNodes.remove(currentNode);
                if (currentNode != null) {
                    for (Map.Entry<DijkstraNode, Integer> entry : currentNode.adjacentNodes.entrySet()) {
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

        private DijkstraNode getLowestDistanceNode(Set<DijkstraNode> unsettledNodes) {
            DijkstraNode lowestDistanceNode = null;
            int lowestDistance = Integer.MAX_VALUE;
            for (DijkstraNode node : unsettledNodes) {
                if (node.distance < lowestDistance) {
                    lowestDistance = node.distance;
                    lowestDistanceNode = node;
                }
            }
            return lowestDistanceNode;
        }

        private void calculateMinimumDistance(DijkstraNode evaluationNode, int edgeWeight, DijkstraNode sourceNode) {
            int sourceDistance = sourceNode.distance;
            if (sourceDistance + edgeWeight < evaluationNode.distance) {
                evaluationNode.distance = sourceDistance + edgeWeight;
            }
        }

        private static class DijkstraNode {
            private final Field position;
            private int distance = Integer.MAX_VALUE;
            private final Map<DijkstraNode, Integer> adjacentNodes = new HashMap<>();

            public DijkstraNode(Field position) {
                this.position = position;
            }

            public Field getPosition() {
                return position;
            }

            public int getDistance() {
                return distance;
            }

            @Override
            public String toString() {
                return position + ":" + distance;
            }

            public void collectBestPathNodes(DirectionalDijkstraGraph graph, Set<DijkstraNode> bestPathNodes) {
                if (bestPathNodes.contains(this)) {
                    return;
                }
                bestPathNodes.add(this);
                if (this.getDistance() == 0) {
                    return;
                }
                graph.getNodes().values().stream()
                        .filter(node -> node.adjacentNodes.keySet().stream().anyMatch(key -> key.position.x() == position.x() && key.position.y() == position.y()))
                        .filter(node -> node.getDistance() == this.getDistance() - 1 || node.getDistance() == this.getDistance() - 1001)
                        .forEach(node -> node.collectBestPathNodes(graph, bestPathNodes));
            }
        }

        public enum Direction {
            NORTH, EAST, SOUTH, WEST
        }
    }
}
