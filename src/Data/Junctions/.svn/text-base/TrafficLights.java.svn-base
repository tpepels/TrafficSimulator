/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Junctions;

import Data.*;
import GUI.Direction;
import TLStrategies.TLStrategy;

/**
 *
 * @author rik_claessens
 */
public class TrafficLights extends Junction {

    private TLStrategy tlStrategy;
    private TrafficLight[][] trafficLights;
    private int brakeDistance = 10;
    private boolean trafficLightsMade = false;

    public static void setSpeedLimitDist(int distance) {
        speedLimitDistance = distance;
    }

    public TrafficLights(int nodeNr, int gridX, int gridY, int gridSize, TLStrategy tlStrategy) {
        super(JunctionType.TrafficLights, nodeNr, gridX, gridY, gridSize);
        this.tlStrategy = tlStrategy;

    }

    @Override
    public void update(double timeInterval) {
        super.update(timeInterval);
        super.resetAvSpeedStats();
        for (Road r : getIncomingRoads()) {
            if (r != null) {
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
                    }
                }
            }
        }
        tlStrategy.controlTL(getIncomingRoads(), trafficLights, timeInterval, this);
    }

    public void resetSpeedLimits(Road r) {
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

    public void setSpeedLimits(Road r) {
        for (Lane l : r.getLanes()) {
            setSpeedLimits(r, l);
        }
    }

    public void setSpeedLimits(Road r, Lane l) {

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
                            break;
                        }
                    }
                }
            } else {
                // No different speedlimits apply, just use the road's default limits.
                if (cCar.isTruck()) {
                    cCar.setdVelocity(r.getMaxSpeedTrucks());
                } else {
                    cCar.setdVelocity(r.getMaxSpeedCars());
                }
            }
        }
    }

    public TrafficLight[][] getTrafficLights() {
        return trafficLights;
    }

    public void createTrafficLights() {
        trafficLights = new TrafficLight[4][];
        for (int i = 0; i < outgoingRoads.length; i++) {
            if (outgoingRoads[i] != null) {
                trafficLights[i] = new TrafficLight[outgoingRoads[i].getLanes().size()];
                for (int j = 0; j < outgoingRoads[i].getLanes().size(); j++) {
                    trafficLights[i][j] = new TrafficLight(Direction.values()[i], TrafficLightStatus.RED);
                    addGate(outgoingRoads[i], outgoingRoads[i].getLanes().get(j));
                }
            }
        }
        trafficLightsMade = true;
    }

    public boolean trafficLightsMade() {
        return trafficLightsMade;
    }

    public void addGate(Road road, Lane l) {
        Car car = new Car();
        car.setPosition(road.getLength()); // It should be added at the end of the road.
        l.getCars().add(0, car);
    }

    public void addGate(Road road) {
        for (Lane lane : road.getLanes()) {
            addGate(road, lane);
        }
    }

    public void deleteGate(Road road) {
        for (Lane lane : road.getLanes()) {
            deleteGate(road, lane);
        }
    }

    public void deleteGate(Road road, Lane l) {
        if (l.getCars().size() > 0) {
            if (l.getCars().getFirst().isIsGate()) {
                l.getCars().removeFirst();  // The gate-car is always the first one.
            }
        }
    }
}
