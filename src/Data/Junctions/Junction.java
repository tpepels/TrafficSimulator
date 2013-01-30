package Data.Junctions;

import Data.Car;
import Data.Lane;
import Data.Road;
import GUI.SimulationPanel;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Lukas
 */
public abstract class Junction implements Serializable {

    protected Road[] outgoingRoads, incomingRoads; // Roads that start and end in this junction
    private double[][] junctionlength;            // Distances to go from each start road to each end node
    private double maxSpeedCars, maxSpeedTrucks;
    protected int nodeNr;
    private JunctionType type;
    protected static int speedLimitDistance = 40;
    protected int[] speedLimits = {6, 4, 2};          // The speed limit distance is split in 4 parts
    private int gridX, gridY, gridSize;
    private double averageSpeed = 0;
    private int noOfCars = 0;
    public double timeStandingStill = 0;
    public String name = "";

    public Junction(JunctionType type, int nodeNr, int gridX, int gridY, int gridSize) {
        this.type = type;
        this.outgoingRoads = new Road[4];
        this.incomingRoads = new Road[4];
        this.junctionlength = new double[outgoingRoads.length][incomingRoads.length];
        this.nodeNr = nodeNr;
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridSize = gridSize;
    }

    public void clearOutgoingRoads(Road road, int junctionNr) {
        for (int i = 0; i < outgoingRoads.length; i++) {
            if (outgoingRoads[i] != null && outgoingRoads[i].getEndnode() == junctionNr) {
                outgoingRoads[i] = null;
                break;
            }
        }

    }

    public void clearIncomingRoads(Road road, int junctionNr) {
        for (int i = 0; i < incomingRoads.length; i++) {
            if (incomingRoads[i] != null && incomingRoads[i].getStartnode() == junctionNr) {
                incomingRoads[i] = null;
                break;
            }
        }
    }

    public int verticalLanes() {
        int total = 0;
        if (outgoingRoads[0] != null) {
            total += outgoingRoads[0].getLanes().size();
            if (incomingRoads[0] != null) {
                total += incomingRoads[0].getLanes().size();
            }
        } else if (outgoingRoads[2] != null) {
            total += outgoingRoads[2].getLanes().size();
            if (incomingRoads[2] != null) {
                total += incomingRoads[2].getLanes().size();
            }
        }
        return total;
    }

    public int horizontalLanes() {
        int total = 0;
        if (outgoingRoads[1] != null) {
            total += outgoingRoads[1].getLanes().size();
            if (incomingRoads[1] != null) {
                total += incomingRoads[1].getLanes().size();
            }
        } else if (outgoingRoads[3] != null) {
            total += outgoingRoads[3].getLanes().size();
            if (incomingRoads[3] != null) {
                total += incomingRoads[3].getLanes().size();
            }
        }
        return total;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getNodeNr() {
        return nodeNr;
    }

    public Road getNextRoadForCar(Car c) {
        int node = c.getPath().poll();

        for (Road r : incomingRoads) {
            if (r != null) {
                if (r.getEndnode() == node) {
                    return r;
                }
            }
        }

        for (Road r : outgoingRoads) {
            if (r != null) {
                if (r.getStartnode() == node) {
                    return r;
                }
            }
        }

        return null;
    }

    public Road peekNextRoadForCar(Car c) {
        int node = c.getPath().peek();

        for (Road r : incomingRoads) {
            if (r != null) {
                if (r.getEndnode() == node) {
                    return r;
                }
            }
        }

        for (Road r : outgoingRoads) {
            if (r != null) {
                if (r.getStartnode() == node) {
                    return r;
                }
            }
        }

        return null;
    }

    public void update(double timeInterval) {
        for (Road road : this.getIncomingRoads()) {
            if (road != null) {
                if (road.getLanes().size() != 1) {
                    road.handleLC(this, false);
                }
            }
        }
    }

    public void updateLane(double timeInterval, Lane l, Road r) {
        // setSpeedLimits(r, l);                            // These should only be set if applicable.
        boolean gate;
        Car prevCar = null;
        for (Car cCar : l.getCars()) {
            if (cCar.isIsGate()) {
                gate = true;
            }
            if(!cCar.isIsGate()) {
                noOfCars++;
            }
            if (prevCar != null && !cCar.isIsGate() && (cCar.getPosition() + cCar.getLength() + cCar.getMinSpace() < l.getLength())) {
                cCar.drive(prevCar.getVelocity(), (prevCar.getPosition() - cCar.getPosition() - cCar.getLength()), false, timeInterval);
            }
            prevCar = cCar;
            averageSpeed += cCar.getSpeed();
            timeStandingStill += cCar.getTimeStandingStill();
        }
    }

    public void resetAvSpeedStats() {
        noOfCars = 0;
        averageSpeed = 0;
        timeStandingStill = 0;
    }

    public void updateFirst(double timeInterval, Road cRoad, Lane l, Road nextRoad, Car fCar) {
        int oldIndex = 0;
        for (int i = 0; i < cRoad.getLanes().size(); i++) {
            if (cRoad.getLanes().get(i) == l) {
                oldIndex = i;
            }
        }

        int nextIndex = 0;
        if (fCar.getPath().size() > 0) {
            Junction nextJ = SimulationPanel.getJunctionList().get(nextRoad.getEndnode());
            nextIndex = nextRoad.nextPreferredLane(nextJ, fCar);
        } else {
            double highestPos = 0;
            int bestLane = 0;
            for (int i = 0; i < nextRoad.getLanes().size(); i++) {
                if (!nextRoad.getLanes().get(i).getCars().isEmpty()) {
                    double lastCarPosition = nextRoad.getLanes().get(i).getCars().getLast().getPosition();

                    if (lastCarPosition > highestPos) {
                        bestLane = i;
                        highestPos = lastCarPosition;
                    }
                } else {
                    bestLane = i;
                    break;
                }
            }
            nextIndex = bestLane;
        }

        Lane next = nextRoad.getLanes().get(nextIndex);
        if (!(nextRoad == null) && fCar.getdVelocity() > 0) {

            if (next.getCars().size() != 0) {
                Car lastCarOnNextRoad = next.getCars().getLast();
                double distance = (l.getLength() - fCar.getPosition() - fCar.getLength()) + lastCarOnNextRoad.getPosition();
                fCar.drive(lastCarOnNextRoad.getVelocity(), distance, false, timeInterval);
            }
        }

        if (fCar.getPosition() + fCar.getLength() >= l.getLength() - l.getExtraEndLength()) {
            if (!(nextRoad == null)) {
                if (next.getCars().size() == 0 || next.getCars().getLast().getPosition() >= fCar.getLength() + fCar.getMinSpace()) {
                    //System.out.print("Enter next road!");
                    if(fCar.getAcceleration() > 0 && next.getCars().size() > 0) {
                        fCar.drive(next.getCars().getLast().getVelocity(), next.getLength()-next.getCars().getLast().getPosition(), true, timeInterval);
                    }
                    next.addCar(fCar);
                    fCar.setPosition(0);

                    if (fCar.isTruck()) {
                        fCar.setdVelocity(nextRoad.getMaxSpeedTrucks());
                    } else {
                        fCar.setdVelocity(nextRoad.getMaxSpeedCars());
                    }
                    fCar.setName("");
                    l.getCars().removeFirst();
                } else {
                    fCar.setdVelocity(0);
                    LinkedList<Integer> path = (LinkedList<Integer>) fCar.getPath();
                    path.add(0, nextRoad.getEndnode());
                }
            }
        }
    }

    protected void resetSpeedLimits(Road r) {
        for (Lane l : r.getLanes()) {
            for (Car c : l.getCars()) {
                if (c.isTruck()) {
                    c.setdVelocity(r.getMaxSpeedTrucks());
                } else {
                    c.setdVelocity(r.getMaxSpeedCars());
                }
            }
        }
    }

    protected void setSpeedLimits(Road r) {
        for (Lane l : r.getLanes()) {
            setSpeedLimits(r, l);
        }
    }

    protected void setSpeedLimits(Road r, Lane l) {

        double[] positions = null;

        if (speedLimitDistance > 0) {
            int zones = speedLimits.length;
            double length = speedLimitDistance / (double) zones;
            positions = new double[zones];

            for (int i = 0; i < zones; i++) {
                positions[i] = l.getLength() - (length * (zones - i));
            }
        }

        for (Car cCar : l.getCars()) {
            if (cCar.isIsGate()) // Skip any gatecar.
            {
                continue;
            }

            if (speedLimitDistance > 0 && cCar.getPosition() > positions[0]) {
                if (cCar.getPosition() > positions[0]) {
                    for (int i = positions.length - 1; i >= 0; i--) {
                        if (cCar.getPosition() > positions[i]) {
                            cCar.setdVelocity(speedLimits[i]);
                        }
                    }
                }
            }
        }
    }

    public JunctionType getType() {
        return type;
    }

    public void addIncomingRoad(int position, Road road) {
        outgoingRoads[position] = road;
    }

    public void addIncomingRoad(Road road) {
//        startRoads.add(road);
    }

    public void addOutgoingRoad(int position, Road road) {
        incomingRoads[position] = road;
    }

    public void addOutgoingRoad(Road road) {
//        endRoads.add(road);
    }

    public void addGate(Road road) {
        Car car = new Car();
        car.setPosition(road.getLength()); // It should be added at the end of the road.
        for (Lane l : road.getLanes()) {
            l.getCars().add(0, car);
        }
    }

    public void addGate(Lane l) {
        Car car = new Car();
        car.setPosition(l.getLength());
        l.getCars().add(0, car);
    }

    public void deleteGate(Lane l) {
        if (l.getCars().getFirst().isIsGate()) {
            l.getCars().removeFirst();
        }
    }

    public void deleteGate(Road road) {
        for (Lane l : road.getLanes()) {
            if (l.getCars().size() > 0) {
                if (l.getCars().getFirst().isIsGate()) {
                    l.getCars().removeFirst();  // The gate-car is always the first one.
                }
            }
        }
    }

    public void addGate(Road road, Lane l) {
        Car car = new Car();
        car.setPosition(road.getLength()); // It should be added at the end of the road.
        l.getCars().add(0, car);
    }

    public void deleteGate(Road road, Lane l) {
        if (l.getCars().size() > 0) {
            if (l.getCars().getFirst().isIsGate()) {
                l.getCars().removeFirst();  // The gate-car is always the first one.
            }
        }
    }

    public Road[] getOutgoingRoads() {
        return incomingRoads;
    }

    /**
     * @return the speedLimitDistance
     */
    public int getSpeedLimitDistance() {
        return speedLimitDistance;
    }

    /**
     * @param speedLimitDistance the speedLimitDistance to set
     */
    public void setSpeedLimitDistance(int speedLimitDistance) {
        this.speedLimitDistance = speedLimitDistance;
    }

    public Road[] getIncomingRoads() {
        return outgoingRoads;
    }

    public double getAverageSpeed() {
        double ret = averageSpeed;
        averageSpeed = 0;
        return ret;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getTimeStandingStill() {
        double ret = timeStandingStill;
        timeStandingStill = 0;
        return ret;
    }

    public void setTimeStandingStill(double timeStandingStill) {
        this.timeStandingStill = timeStandingStill;
    }

    public int getNoOfCars() {
        int ret = noOfCars;
        noOfCars = 0;
        return ret;
    }

    public void setNoOfCars(int noOfCars) {
        this.noOfCars = noOfCars;
    }
}
