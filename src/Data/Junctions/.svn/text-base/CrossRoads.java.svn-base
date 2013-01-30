package Data.Junctions;

import Data.Car;
import Data.Lane;
import Data.Road;
import java.util.*;

public class CrossRoads extends Junction {

    private LinkedList<Car> carQueue; //Queues of cars which want to go in one of the 4 directions

    
    public CrossRoads(int nodeNr, Road[] startRoads, Road[] endRoads, int gridX, int gridY, int gridSize) {
        super(JunctionType.CrossRoads, nodeNr, gridX, gridY, gridSize);
        for (int i = 0; i < startRoads.length; i++) {
            addIncomingRoad(i, startRoads[i]);
        }
        for (int i = 0; i < endRoads.length; i++) {
            addOutgoingRoad(i, endRoads[i]);
        }
        carQueue =  new LinkedList();
        
    }

    public CrossRoads(int nodeNr, int gridX, int gridY, int gridSize) {
        super(JunctionType.CrossRoads, nodeNr, gridX, gridY, gridSize);
    }

    public void update(double timeInterval) {
        super.update(timeInterval);
        boolean corner = false;
        int counter = 0;
        for (int i = 0; i < getIncomingRoads().length; i++) {
            if (getIncomingRoads()[i] == null && getOutgoingRoads()[i] == null) {
                counter++;
            }
            if(counter == 2)
                corner = true;
        }
        if (!corner) {
//            super.update(timeInterval);
            int newInd = 0;
            int oldInd = 0;
            for (Road r : getIncomingRoads()) {
                if (r != null) {
                    setSpeedLimits(r);
                    double avSpeed = 0;
                    int noOfLanes = r.getLanes().size();
                    for (Lane l : r.getLanes()) {
                        if (l.getCars().size() > 0) {
                            Car fCar = l.getCars().getFirst();
                            if (!fCar.isIsGate() && fCar.getPosition() + fCar.getLength() >= (l.getLength() - fCar.getMinSpace())) {
                                // The front of the car reaches the end of the road within its minimal spacing                                
                                if (fCar.getPath().isEmpty()) {
                                    l.deleteCar(fCar);
                                } else {
                                    Road nextRoad = getNextRoadForCar(fCar);
                                    Lane next = new Lane(0); // dummy
                                    for(int i = 0 ; i < r.getLanes().size() ; i++) {
                                        if(r.getLanes().get(i) ==l)
                                            next = nextRoad.getLanes().get(i);
                                    }
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
                                    Road lRoad = getIncomingRoads()[(oldInd + 1) % 4];
                                    Road sRoad = getIncomingRoads()[(oldInd + 2) % 4];
                                    Road rRoad = getIncomingRoads()[(oldInd + 3) % 4];
                                    Road cRoad = getIncomingRoads()[oldInd];
                                    Lane rLane = new Lane(0); // dummy
                                    Lane sLane = new Lane(0); // dummy
                                    if (rRoad != null) {
                                        rLane = rRoad.getLaneWithFirstCar();
                                    }
                                    if (sRoad!= null) {
                                        sLane = sRoad.getLaneWithFirstCar();
                                    }
                                    Car sCar = null;
                                    Car rCar = null;
                                    if (sLane != null) {
                                        if (sLane.getCars().size() > 0) {
                                            sCar = sRoad.getFirstCar();
                                        }
                                    }
                                    if (rLane!=null) {
                                        if(rLane.getCars().size() > 0) {
                                            rCar = rRoad.getFirstCar();
                                        }
                                    }
                                    double rGap = 0;
                                    double sGap = 0;
                                    if (cRoad.getFirstCar() != null) {
                                        if (rCar != null) {
                                            rGap = (rCar.getVelocity() * ((cRoad.getLength() - cRoad.getFirstCar().getPosition() - cRoad.getFirstCar().getLength()) / cRoad.getFirstCar().getVelocity()));
                                        }
                                        if (sCar != null) {
                                            sGap = (sCar.getVelocity() * ((cRoad.getLength() - cRoad.getFirstCar().getPosition() - cRoad.getFirstCar().getLength()) / cRoad.getFirstCar().getVelocity()));
                                        }
                                    }
                                    if(next.getCars().size() > 0) {
                                        if (next.getCars().getLast().getPosition() < fCar.getLength() + fCar.getMinSpace()) {
                                            // the car does not fit on the next road
                                            if (!r.getFirstCar().isIsGate()) {
//                                                addGate(l);
                                                //carQueue.add(fCar);
                                            }
                                        }
                                    }
                                    if (newInd == (oldInd + 3) % 4) {
                                        System.out.println("GO RIGHT");
                                        // I wanna go right -- no problem
                                        updateFirst(timeInterval, r, l, nextRoad, fCar);
                                    } else if (newInd == (oldInd + 1) % 4) {
                                        System.out.println("GO LEFT");
                                        // I wanna go left
                                        if (rCar != null) {
                                            //First car from the right is at the end of the road---                            
                                            if ((!rCar.isIsGate() && rCar.getPosition() + rCar.getLength() >= (rRoad.getLength() - rCar.getMinSpace() - rGap))
                                                    //-and it wants to go either straight(=current left) or left (current road)
                                                    && (this.peekNextRoadForCar(rCar) == lRoad || this.peekNextRoadForCar(rCar) == cRoad)) {
                                                //Let those cars pass first
                                                updateFirst(timeInterval, rRoad, rLane, this.getNextRoadForCar(rCar), rCar);
                                            } else {
                                                //Its free lets go
                                                updateFirst(timeInterval, r, l, nextRoad, fCar);
                                            }
                                        } else if (sCar != null) {
                                            //First car coming from straight is at the end of the road---
                                            if ((!sCar.isIsGate() && sCar.getPosition() + sCar.getLength() >= (sRoad.getLength() - sCar.getMinSpace() - sGap))
                                                    //-and it wants to go either right(=current left) or straight (current road)                             
                                                    && (this.peekNextRoadForCar(sCar) == lRoad || this.peekNextRoadForCar(rCar) == cRoad)) {
                                                //Also let those pass
                                                updateFirst(timeInterval, sRoad, sLane, this.getNextRoadForCar(sCar), rCar);
                                            } else {
                                                //Its free lets go
                                                updateFirst(timeInterval, r, l, nextRoad, fCar);
                                            }
                                        } else {
                                            //Its free lets go
                                            updateFirst(timeInterval, r, l, nextRoad, fCar);
                                        }
                                    } else if (newInd == (oldInd + 2) % 4) {
                                        System.out.println("GO STRAIGHT");
                                        if (rCar != null) {
                                            // I wanna go straight
                                            if (!rCar.isIsGate() && rCar.getPosition() + rCar.getLength() >= (getIncomingRoads()[(oldInd + 3) % 4].getLength() - rCar.getMinSpace() - rGap)) {
                                                //Let cars from the right pass first
                                                updateFirst(timeInterval, rRoad, l, this.getNextRoadForCar(rCar), rCar);
                                            } else {
                                                //No car from the right
                                                updateFirst(timeInterval, r, l, nextRoad, fCar);
                                            }
                                        } else {
                                            //No car from the right
                                            updateFirst(timeInterval, r, l, nextRoad, fCar);
                                        }
                                    }
                                }
                            } else if (!fCar.isIsGate()) {
                                fCar.drive(0, 0, true, timeInterval); // This is the first car on the road
                            }
                            super.updateLane(timeInterval, l, r);
                            // Continue with the rest of le cars
                        }
                    }
                }
            }
        } else {
            for (Road r : getIncomingRoads()) {
                if (r != null) {
                    double avSpeed = 0;
                    int noOfLanes = r.getLanes().size();
                    for (Lane l : r.getLanes()) {
                        if (l.getCars().size() != 0) {
                            Car fCar = l.getCars().getFirst();
                            if (!fCar.isIsGate() && fCar.getPosition() + fCar.getLength() >= (r.getLength() - fCar.getMinSpace())) {
                                if (fCar.getPath().isEmpty()) {
                                    l.deleteCar(fCar);
                                } else {
                                    super.updateFirst(timeInterval, r, l, this.getNextRoadForCar(l.getCars().getFirst()), l.getCars().getFirst());
                                }
                            } else {
                                fCar.drive(0, 0, true, timeInterval);
                            }
                            super.updateLane(timeInterval, l, r);
                            avSpeed += l.getAverageSpeed();
                        }
                    }
                }
            }
        }
    }
}
