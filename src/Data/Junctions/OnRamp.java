/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Junctions;

import Data.*;

/**
 *
 * @author Cliff
 */
public class OnRamp extends Junction {

    private Road IncomingRoad;
    private Road OutgoingRoad;
    private Car gate;

    public OnRamp(int nodeNr, Road startRoad, Road endRoad, int gridX, int gridY, int gridSize) {
        super(JunctionType.OnRamp, nodeNr, gridX, gridY, gridSize);
        gate = new Car();
        addIncomingRoad(0, startRoad);
        addOutgoingRoad(0, endRoad);
        gate.setPosition(endRoad.getLength() - gate.getLength());
        this.IncomingRoad = startRoad;
        this.OutgoingRoad = endRoad;

        //Add a gate car on the rightmost lane which is standing still, blocking the traffic.
        //Let DLC do the work.
        int oldInd = 0;
        for (int x = 0; x < getIncomingRoads().length; x++) {
            if (getIncomingRoads()[x] != null) {
                if (getIncomingRoads()[x] == startRoad) {
                    oldInd = x;
                    break;
                }
            }
        }
        if (oldInd == 0 | oldInd == 1) {
            startRoad.addCar(gate, startRoad.getLanes().size() - 1, true);
        } else if (oldInd == 0 | oldInd == 1) {
            startRoad.addCar(gate, 0, true);
        }
    }

    public OnRamp(int nodeNr, int gridX, int gridY, int gridSize) {
        super(JunctionType.OnRamp, nodeNr, gridX, gridY, gridSize);
    }

    public void update(double timeInterval) {
        for(Road r: getOutgoingRoads()) {
             if (r != null) {
                r.handleLC(this, false);
             }
        }
        for (Road r : getIncomingRoads()) {
            if (r != null) {
                r.handleLC(this, false);
                for (Lane l : r.getLanes()) {
                    if (l.getCars().size() != 0) {
                        Car fCar = l.getCars().getFirst();
                        if (!fCar.isIsGate() && fCar.getPosition() + fCar.getLength() >= (r.getLength() - fCar.getMinSpace())) {
                            super.updateFirst(timeInterval, r, l, this.getNextRoadForCar(l.getCars().getFirst()), l.getCars().getFirst());
                        } else {
                            fCar.drive(0, 0, true, timeInterval);
                        }
                        super.updateLane(timeInterval, l, r);
                    }
                }
            }
        }
    }
//    @Override
//    public void addIncomingRoad(int i, Road r) {
//        IncomingRoad = r;
//    }
//    @Override
//    public void addOutgoingRoad(int i, Road r) {
//        OutgoingRoad = r;
//    }
}
