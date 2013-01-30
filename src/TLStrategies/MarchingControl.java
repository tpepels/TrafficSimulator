package TLStrategies;

import Data.Junctions.TrafficLightStatus;
import Data.Junctions.TrafficLights;
import Data.Road;
import Data.TrafficLight;
import java.io.Serializable;

/**
 *
 * @author rik_claessens
 */
public class MarchingControl implements TLStrategy, Serializable {

    private static int greenTime = 15000;
    private static int orangeTime = 3000;
    private double currentTime = 0;

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
                    if (currentTime - greenTime > 0) {
                        currentTime = 0;
                        setLights(trafficLights[i], TrafficLightStatus.ORANGE, roads[i], tlJunction);
                    }
                    break;
                } else if (trafficLights[i][0].getLightStatus() == TrafficLightStatus.ORANGE) {
                    if (currentTime - orangeTime > 0) {
                        currentTime = 0;
                        setLights(trafficLights[i], TrafficLightStatus.RED, roads[i], tlJunction);
                        setLights(trafficLights[getNextTrafficLight(trafficLights, i)], TrafficLightStatus.GREEN, roads[getNextTrafficLight(trafficLights, i)], tlJunction);
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
