/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dijkstra;

import Data.Junctions.Junction;
import Data.Road;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Cliff
 */
public class Dijkstra {

    private static int numberOfRoads, numberOfJunctions;
    private static ArrayList<DijkstraNode> nodes = new ArrayList<DijkstraNode>();
    private static int[][] A;
    private static double[][] B, C;
    public static ArrayList<Junction> junctions;

    /**
     * 
     * @param junctions
     * @param roads 
     */
    public static void constructMatrices(ArrayList<Junction> junctions) {
        //Clear all shitz
        nodes.clear();
        numberOfRoads = 0;
        numberOfJunctions = 0;

        //Setup sandvich
        int noJunctions = junctions.size();
        if (noJunctions > 0) {
            A = new int[noJunctions][noJunctions];
            B = new double[noJunctions][noJunctions];
            C = new double[noJunctions][noJunctions];

            int counter = 0;
            for (Junction junction : junctions) {
                for (Road incRoad : junction.getIncomingRoads()) {
                    if (incRoad != null) {
                        A[incRoad.getStartnode()][incRoad.getEndnode()] = incRoad.getLanes().size();
                        B[incRoad.getStartnode()][incRoad.getEndnode()] = incRoad.getLength();
                        C[incRoad.getStartnode()][incRoad.getEndnode()] = incRoad.getMaxSpeedCars();
                    }
                }
                for (Road outRoad : junction.getOutgoingRoads()) {
                    if (outRoad != null) {
                        A[outRoad.getStartnode()][outRoad.getEndnode()] = outRoad.getLanes().size();
                        B[outRoad.getStartnode()][outRoad.getEndnode()] = outRoad.getLength();
                        C[outRoad.getStartnode()][outRoad.getEndnode()] = outRoad.getMaxSpeedCars();
                    }
                }
            }

            int sizeM = A.length;
            int sizeN = A[0].length;

            for (int i = 0; i < sizeM; i++) {
                nodes.add(new DijkstraNode(i));
            }

            counter = 0;
            for (int i = 0; i < sizeM; i++) {
                for (int j = 0; j < sizeN; j++) {
                    if (A[i][j] != 0) {
                        counter++;
                        nodes.get(i).addEdge(nodes.get(j), B[i][j]);
                    }
                }
            }
            Dijkstra.numberOfRoads = counter;
            Dijkstra.numberOfJunctions = junctions.size();
        }
    }

    /**
     * 
     * @param A
     * @return 
     */
    private static int countRoads(int[][] A) {
        int count = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (A[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     *
     * @param startNode Source node.
     * @param endNode Target node.
     * @param flag True = time , false = shortest
     * @return
     */
    public static Queue<Integer> shortestPath(int startNode, int endNode, boolean flag) {
        LinkedList<Integer> path = new LinkedList<Integer>();
        ArrayList<DijkstraNode> unsettledNodes = (ArrayList<DijkstraNode>) Dijkstra.nodes.clone();
        double[] distance = new double[nodes.size()];
        int[] previous = new int[nodes.size()];

        for (int i = 0; i < nodes.size(); i++) {
            distance[i] = Integer.MAX_VALUE;
            previous[i] = -1;
        }

        distance[startNode] = 0;

        while (!unsettledNodes.isEmpty()) {
            DijkstraNode evaluationNode = Dijkstra.nodeLowestDistance(distance, unsettledNodes);
            if (evaluationNode == null) {
                return null;
            }
            if (distance[evaluationNode.getNodeNumber()] == Integer.MAX_VALUE) {
                return null;
            }
            unsettledNodes.remove(evaluationNode);
            for (int j = 0; j < evaluationNode.getEndNodes().size(); j++) {
                int evaluatedNeighbourNumber = evaluationNode.getEndNodes().get(j).getNodeNumber();
                int evaluationNodeNumber = evaluationNode.getNodeNumber();
                if (flag == true) {
                    double altDistance = distance[evaluationNodeNumber] + (evaluationNode.getDistance(j) / Dijkstra.C[evaluationNodeNumber][evaluatedNeighbourNumber]);
                    if (altDistance < distance[evaluatedNeighbourNumber]) {
                        distance[evaluatedNeighbourNumber] = altDistance;
                        previous[evaluatedNeighbourNumber] = evaluationNode.getNodeNumber();
                    }
                } else {
                    double altDistance = distance[evaluationNodeNumber] + evaluationNode.getDistance(j);
                    if (altDistance < distance[evaluatedNeighbourNumber]) {
                        distance[evaluatedNeighbourNumber] = altDistance;
                        previous[evaluatedNeighbourNumber] = evaluationNode.getNodeNumber();
                    }
                }
            }
        }

        int cNode = endNode;
        path.offer(cNode);
        while (previous[cNode] != startNode) {
            path.add(0, previous[cNode]);
            cNode = previous[cNode];
        }

        return path;
    }

    /**
     * Returns the node with the lowest distance.
     * @return
     */
    public static DijkstraNode nodeLowestDistance(double[] distance, ArrayList<DijkstraNode> unSettledNodes) {
        double minDistance = Integer.MAX_VALUE;
        DijkstraNode node = null;
        for (int i = 0; i < unSettledNodes.size(); i++) {
            double currentDistance = distance[unSettledNodes.get(i).getNodeNumber()];
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                node = unSettledNodes.get(i);
            }
        }
        return node;
    }

    /**
     * @return the numberOfRoads
     */
    public static int getNumberOfRoads() {
        return numberOfRoads;
    }

    /**
     * @return the numberOfJunctions
     */
    public static int getNumberOfJunctions() {
        return numberOfJunctions;
    }
}
