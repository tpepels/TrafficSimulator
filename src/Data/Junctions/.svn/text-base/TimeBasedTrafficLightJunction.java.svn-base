package Data.Junctions;

import Data.*;

public class TimeBasedTrafficLightJunction extends Junction {

    public static int getLightInterval() {
        return lightInterval;
    }

    public static void setLightInterval(int aLightInterval) {
        lightInterval = aLightInterval;
    }

    public static int getOrangeTime() {
        return orangeTime;
    }

    public static void setOrangeTime(int aOrangeTime) {
        orangeTime = aOrangeTime;
    }

    public static void setSpeedLimitDist(int distance) {
        speedLimitDistance = distance;
    }
    private TrafficLightStatus[] lightStatus = {TrafficLightStatus.RED,
        TrafficLightStatus.RED, TrafficLightStatus.RED, TrafficLightStatus.RED};
    private int orangeLight = -1;       // The light that is currently orange
    private long orangeLightTime = -1;   // The time the current light went orange
    private static int lightInterval = 15000;  // The time in ms that any of the lights is green.
    private static int orangeTime = 3000;      // The time in ms that the lights remain orange.
    private long currentTime = 0;       // If the currentTime % lightInterval == 0 something should happen!
    private boolean roadAdded = false;
    private int nextGreen = -1;
    private static int[] orangeSpeedLimits = {10, 8, 6, 4};

    public TimeBasedTrafficLightJunction(int nodeNr, int gridX, int gridY, int gridSize) {
        super(JunctionType.TrafficLights, nodeNr, gridX, gridY, gridSize);
        this.speedLimits = orangeSpeedLimits;
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

        currentTime += timeInterval;
        if (currentTime % lightInterval == 0) {
            switchLightStatus();
        }

        if (orangeLight > -1) { // one of the lights is currently orange
            Road orangeRoad = getIncomingRoads()[orangeLight];
            setSpeedLimits(orangeRoad);

            // The orange light should be switched to red.
            if ((currentTime - orangeTime) >= orangeLightTime) {

                addGate(orangeRoad); // Add gatecars on the road
                lightStatus[orangeLight] = TrafficLightStatus.RED;
                orangeLight = -1; // There is no orange light;

                if (nextGreen > -1) {
                    lightStatus[nextGreen] = TrafficLightStatus.GREEN;
                    this.resetSpeedLimits(getIncomingRoads()[nextGreen]);
                    this.deleteGate(getIncomingRoads()[nextGreen]);
                }
            }
        }
    }

    private void switchLightStatus() {
        Road[] r = getIncomingRoads();
        boolean switchNext = false;
        int firstRoad = 5;
        for (int i = 0; i < r.length; i++) {
            if (r[i] != null) {

                if (i < firstRoad) {
                    firstRoad = i;
                }

                if (lightStatus[i] == TrafficLightStatus.GREEN) {
                    lightStatus[i] = TrafficLightStatus.ORANGE;
                    orangeLight = i;
                    orangeLightTime = currentTime;
                    switchNext = true;
                } else if (switchNext) {
                    nextGreen = i;
                    return;
                }
            }
        }

        // The light of the first road should become green
        if (switchNext) {
            nextGreen = firstRoad;
        }
    }

    public TrafficLightStatus[] getLightStatus() {
        return lightStatus;
    }

    @Override
    public void addIncomingRoad(int position, Road road) {
        super.addIncomingRoad(position, road);
        if (!roadAdded) {
            lightStatus[position] = TrafficLightStatus.GREEN;
            roadAdded = true;
        } else {
            this.setSpeedLimits(road);
            this.addGate(road);
        }
    }
}
