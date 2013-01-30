/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TLStrategies;

import Data.*;
import Data.Junctions.TrafficLightStatus;
import Data.Junctions.TrafficLights;
import java.io.Serializable;

/**
 *
 * @author rik_claessens
 */
public class Sotl_request implements TLStrategy, Serializable {

    private static int greenTime = 15000;
    private static int maxGreenTime = 30000;
    private static int orangeTime = 3000;
    private double currentTime = 0;
    private int nextPhase = 0;
    private int threshold = 20;

    public static int getLightInterval() {
        return greenTime;
    }

    public static void setLightInterval(int aLightInterval) {
        greenTime = aLightInterval;
    }

    public static int getOrangeTime() {
        return orangeTime;
    }

    public static void setOrangeTime(int aOrangeTime) {
        orangeTime = aOrangeTime;
    }

    public void controlTL(Road[] roads, TrafficLight[][] trafficLights, double timeInterval, TrafficLights tlJunction) {

        currentTime += timeInterval;

        for (int i = 0; i < trafficLights.length; i++) {
            if (trafficLights[i] != null) {
                if (trafficLights[i][0].getLightStatus() == TrafficLightStatus.GREEN) {
                    for (int j = 1; j < trafficLights.length; j++) {
                        int next = (i + j) % trafficLights.length;
                        if (trafficLights[next] != null && i != next) {
                            if ((thresholdReached(threshold, roads[next])) || currentTime - maxGreenTime > 0) {
                                currentTime = 0;
                                setLights(trafficLights[i], TrafficLightStatus.ORANGE, roads[i], tlJunction);
                                nextPhase = next;
                            }
                        }
                    }

                    break;
                } else if (trafficLights[i][0].getLightStatus() == TrafficLightStatus.ORANGE) {
                    if (currentTime - orangeTime > 0) {
                        currentTime = 0;
                        setLights(trafficLights[i], TrafficLightStatus.RED, roads[i], tlJunction);
                        setLights(trafficLights[nextPhase], TrafficLightStatus.GREEN, roads[nextPhase], tlJunction);
                    } else {
                        tlJunction.setSpeedLimits(roads[i]);
                    }
                    break;
                } else if (i == trafficLights.length - 1) {
                    for (int j = 0; j < trafficLights.length; j++) {
                        if (trafficLights[j] != null) {
                            setLights(trafficLights[j], TrafficLightStatus.GREEN, roads[j], tlJunction);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private boolean thresholdReached(int threshold, Road road) {
        return countCarsOnRoad(road) >= threshold;
    }

    private int countCarsOnRoad(Road road) {
        return road.getNoOfCars();
    }

    private int getNextTrafficLight(TrafficLight[][] trafficLights, int current) {
        for (int i = 1; i < trafficLights.length; i++) {
            if (trafficLights[(current + i) % trafficLights.length] != null) {
                return (current + i) % trafficLights.length;
            }
        }
        return current;
    }

    private void setLights(TrafficLight[] trafficLights, TrafficLightStatus status, Road r, TrafficLights tlJunction) {
        for (TrafficLight light : trafficLights) {
            light.setTrafficLightStatus(status);
        }
        switch (status) {
            case RED:
                tlJunction.addGate(r);
                break;
            case ORANGE:
                tlJunction.setSpeedLimits(r);
            case GREEN:
                tlJunction.deleteGate(r);
                tlJunction.deleteGate(r);
                break;
        }
    }
}
