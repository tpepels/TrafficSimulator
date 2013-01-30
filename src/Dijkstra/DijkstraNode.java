/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dijkstra;

import java.util.ArrayList;

/**
 *
 * @author Cliff
 */
public class DijkstraNode {

    private int nodeNumber;
    private ArrayList<DijkstraNode> endNodes;
    private ArrayList<Double> distance;

    /**
     * 
     * @param nodeNumber
     */
    public DijkstraNode(int nodeNumber) {
        this.nodeNumber = nodeNumber;
        this.endNodes = new ArrayList<DijkstraNode>();
        this.distance = new ArrayList<Double>();
    }

    /**
     *
     * @param endNodes
     * @param distance
     */
    public void addEdge(DijkstraNode endNode, double distance) {
        this.endNodes.add(endNode);
        this.distance.add(distance);
    }

    /**
     * @return the nodeNumber
     */
    public int getNodeNumber() {
        return nodeNumber;
    }

    /**
     * @return the endNodes
     */
    public ArrayList<DijkstraNode> getEndNodes() {
        return endNodes;
    }

    /**
     * @return the distance
     */
    public double getDistance(int i) {
        return distance.get(i);
    }
}
