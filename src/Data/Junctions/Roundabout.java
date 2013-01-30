/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Junctions;

import Data.Road;

/**
 *
 * @author Lukas
 */
public class Roundabout extends Junction{
    
    private Road round;
    
    public Roundabout(int nodeNr, Road[] startRoads, Road[] endRoads, int gridX, int gridY, int gridSize) {
        super(JunctionType.Roundabout, nodeNr, gridX, gridY, gridSize);
        for (int i = 0; i < startRoads.length; i++) {
            addIncomingRoad(i, startRoads[i]);
        }
        for (int i = 0; i < endRoads.length; i++) {
            addIncomingRoad(i, endRoads[i]);
        }
        round = new Road(0, 1, (getIncomingRoads().length + getOutgoingRoads().length)*5, 10, 5, 2);
    }
    
    public Roundabout(int nodeNr, int gridX, int gridY, int gridSize) {
        super(JunctionType.Roundabout, nodeNr, gridX, gridY, gridSize);
    }
    
    @Override
    public void update(double timeInterval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
