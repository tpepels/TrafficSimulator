package Data.Junctions;

import Data.*;

/**
 *
 * @author Lukas
 */
public class TJunction extends Junction {

    public TJunction(int nodeNr, Road[] startRoads, Road[] endRoads, int gridX, int gridY, int gridSize) {
        super(JunctionType.TJunction, nodeNr, gridX, gridY, gridSize);
        for (int i = 0; i < startRoads.length; i++) {
            addIncomingRoad(i, startRoads[i]);
        }
        for (int i = 0; i < endRoads.length; i++) {
            addOutgoingRoad(i, endRoads[i]);
        }
    }

    public TJunction(int nodeNr, int gridX, int gridY, int gridSize) {
        super(JunctionType.TJunction, nodeNr, gridX, gridY, gridSize);
    }

    public void update(double timeInterval) {
        super.update(timeInterval);
        int newInd = 0;
        int oldInd = 0;
        for (Road r : getIncomingRoads()) {
            if (r != null) {
                double avSpeed = 0; 
                int noOfLanes = r.getLanes().size();
                for (Lane l : r.getLanes()) {
                    if (l.getCars().size() > 0) {
                        Car fCar = l.getCars().getFirst();
                        if (!fCar.isIsGate() && fCar.getPosition() + fCar.getLength() >= (r.getLength() - fCar.getMinSpace())) {
                            // The front of the car reaches the end of the road within its minimal spacing
                            Road nextRoad = getNextRoadForCar(fCar);
                            // Check for the index of the current road
                            for (int i = 0; i < getIncomingRoads().length; i++) {
                                if (getIncomingRoads()[i] == r) {
                                    oldInd = i;
                                    break;
                                }
                            }
                            //Check for the index of the next road
                            for (int i = 0; i < getOutgoingRoads().length; i++) {
                                if (getOutgoingRoads()[i] == nextRoad) {
                                    newInd = i;
                                    break;
                                }
                            }
                            Road rRoad = getIncomingRoads()[(oldInd + 2) % 3];
                            Lane rLane = rRoad.getLaneWithFirstCar();
                            Car rCar = rRoad.getFirstCar();

                            if (newInd == (oldInd + 3) % 4) {
                                // I wanna go right (or straight, depending on the Junction)-- no problem
                                updateFirst(timeInterval, r, l, nextRoad, fCar);
                            } else if (newInd == (oldInd + 1) % 4) {
                                if (rCar != null) {
                                    // I wanna go left (or straight, depending on the Junction)
                                    //First car from the right(or straight) is at the end of the road---                            
                                    if (!rCar.isIsGate() && rCar.getPosition() + rCar.getLength() >= (rRoad.getLength() - rCar.getMinSpace())) {
                                        //Let those cars pass first
                                        updateFirst(timeInterval, rRoad, rLane, this.getNextRoadForCar(rCar), rCar);
                                    }
                                } else {
                                    //Its free lets go
                                    updateFirst(timeInterval, r, l, nextRoad, fCar);
                                }
                            }
                        } else if (!fCar.isIsGate()) {
                            fCar.drive(0, 0, true, timeInterval); // This is the first car on the road
                        }
                        super.updateLane(timeInterval, l, r);
                        avSpeed += l.getAverageSpeed();
                        // Continue with the rest of le cars
                    }
                    r.setAverageSpeed(avSpeed / noOfLanes);
                }
            }
        }
    }
}
