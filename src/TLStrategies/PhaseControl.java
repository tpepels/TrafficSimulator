/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TLStrategies;

import Data.Junctions.TrafficLightStatus;
import Data.Junctions.TrafficLights;
import Data.Lane;
import Data.Road;
import Data.TrafficLight;
import GUI.Direction;
import GUI.Turn;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author rik_claessens
 */
public class PhaseControl implements TLStrategy, Serializable {

    private static int greenTime = 5000;
    private static int orangeTime = 2000;
    private double currentTime = 0;
    // dont use DOUBLERIGHT or DOUBLELEFT turns, works only for the intersection of the map of Maastricht
    private TLPhase[] phases = new TLPhase[]{
        // t = 10
        new TLPhase(new Direction[]{Direction.SOUTH, Direction.SOUTH}, new Turn[]{Turn.LEFT, Turn.STRAIGHT}, 6000),
        // t = 18 
        new TLPhase(new Direction[]{Direction.SOUTH, Direction.SOUTH, Direction.EAST}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 3000),
        // t = 23
        new TLPhase(new Direction[]{Direction.EAST}, new Turn[]{Turn.RIGHT}, 6000),
        //t = 31
        new TLPhase(new Direction[]{Direction.NORTH}, new Turn[]{Turn.LEFTDOUBLE}, 9000),
        // t = 42
        new TLPhase(new Direction[]{Direction.NORTH, Direction.NORTH}, new Turn[]{Turn.LEFTDOUBLE, Turn.RIGHTDOUBLE}, 0),
        // t = 44
        new TLPhase(new Direction[]{Direction.NORTH}, new Turn[]{Turn.RIGHTDOUBLE}, 0),
        // t = 46 < should be 45 but orange time of 2 seconds does not allow me to make it like that
        new TLPhase(new Direction[]{Direction.NORTH, Direction.EAST}, new Turn[]{Turn.RIGHTDOUBLE, Turn.LEFT}, 5000),
        // t = 53
        new TLPhase(new Direction[]{Direction.NORTH, Direction.EAST, Direction.EAST}, new Turn[]{Turn.RIGHTDOUBLE, Turn.LEFT, Turn.STRAIGHT}, 13000),
        // t = 68
        new TLPhase(new Direction[]{Direction.EAST, Direction.EAST}, new Turn[]{Turn.LEFT, Turn.STRAIGHT}, 0),
        // t = 70
        new TLPhase(new Direction[]{Direction.EAST}, new Turn[]{Turn.STRAIGHT}, 13000),
        // t = 85
        new TLPhase(new Direction[]{Direction.EAST, Direction.WEST}, new Turn[]{Turn.STRAIGHT, Turn.STRAIGHT}, 2000),
        // t = 89
        new TLPhase(new Direction[]{Direction.WEST}, new Turn[]{Turn.STRAIGHT}, 4000),
        // t = 91
        new TLPhase(new Direction[]{Direction.WEST}, new Turn[]{Turn.STRAIGHT}, 0),
        // t = 93
        new TLPhase(new Direction[]{Direction.WEST, Direction.WEST}, new Turn[]{Turn.STRAIGHT, Turn.LEFT}, 0),
        // t = 95 < should be 94
        new TLPhase(new Direction[]{Direction.WEST, Direction.WEST, Direction.SOUTH}, new Turn[]{Turn.STRAIGHT, Turn.LEFT, Turn.RIGHT}, 9000),
        // t = 106
        new TLPhase(new Direction[]{Direction.WEST, Direction.WEST, Direction.SOUTH, Direction.WEST}, new Turn[]{Turn.STRAIGHT, Turn.LEFT, Turn.RIGHT, Turn.RIGHT}, 7000),
        // t = 115
        new TLPhase(new Direction[]{Direction.WEST, Direction.WEST, Direction.SOUTH}, new Turn[]{Turn.STRAIGHT, Turn.LEFT, Turn.RIGHT}, 0),
        // t = 117
        new TLPhase(new Direction[]{Direction.WEST, Direction.WEST}, new Turn[]{Turn.STRAIGHT, Turn.LEFT}, 4000),
        // t = 1
        new TLPhase(new Direction[]{Direction.WEST}, new Turn[]{Turn.LEFT}, 1000),
        // t = 4
        new TLPhase(6000)
    };
    private int currentPhaseIndex = 0;
    private TLPhase currentPhase = phases[currentPhaseIndex];
    private boolean greenLightOn = true;
    private boolean firstPhase = true;

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

        if (firstPhase) {
            firstPhase = false;
            currentTime = 0;
            greenLightOn = true;
            // Get the lights from the first phase
            TrafficLight[] phaseLights = getPhaseLights(currentPhase, trafficLights);
            Lane[] phaseLanes = getPhaseLanes(currentPhase, roads);
            // set them to green
            for (int i = 0; i < phaseLights.length; i++) {
                int roadIndex = phaseLights[i].getDirection().ordinal();
                setLight(phaseLights[i], TrafficLightStatus.GREEN, roads[roadIndex], tlJunction, phaseLanes[i]);
            }
            return;
        }
        // increase currentTime
        currentTime += timeInterval;
        if (isRedPhase(currentPhase, trafficLights)) {
            if (currentTime - currentPhase.getGreenTime() > 0) {
                // get the next phase, it's corresponding lights and lanes
                currentTime = 0;
                currentPhase = incrementPhase();
                TrafficLight[] phaseLights = getPhaseLights(currentPhase, trafficLights);
                Lane[] phaseLanes = getPhaseLanes(currentPhase, roads);
                // set them to green
                for (int i = 0; i < phaseLights.length; i++) {
                    int roadIndex = phaseLights[i].getDirection().ordinal();
                    setLight(phaseLights[i], TrafficLightStatus.GREEN, roads[roadIndex], tlJunction, phaseLanes[i]);
                }
            }
        } // if there is currently a green light on
        else if (greenLightOn) {

            // and if green interval has elapsed
            if (currentTime - currentPhase.getGreenTime() > 0) {
                // reset the timer and set greenLightOn to false
                currentTime = 0;
                greenLightOn = false;
                // get the lights which are green
                TrafficLight[] phaseLights = getPhaseLights(currentPhase, trafficLights);
                // get the lights which are green in the next phase
                TrafficLight[] nextPhaseLights = getPhaseLights(getnextTLPhase(), trafficLights);
                // get the lanes corresponding to those lights
                Lane[] phaseLanes = getPhaseLanes(currentPhase, roads);
                // set all traffic lights which are not in the next phase to orange
                for (int i = 0; i < phaseLights.length; i++) {
                    if (isRedPhase(getnextTLPhase(), trafficLights)
                            || !isInNextPhase(phaseLights[i], nextPhaseLights)) {
                        int roadIndex = phaseLights[i].getDirection().ordinal();
                        setLight(phaseLights[i], TrafficLightStatus.ORANGE, roads[roadIndex], tlJunction, phaseLanes[i]);
                    }
                }
            }
        } // else if there is no green light on and the orange time has elpased 
        else if (currentTime - orangeTime > 0) {
            // reset the timer and set greenLightOn to true
            currentTime = 0;
            greenLightOn = true;
            // get the current phase lights
            TrafficLight[] phaseLights = getPhaseLights(currentPhase, trafficLights);
            // and their corresponding lanes
            Lane[] phaseLanes = getPhaseLanes(currentPhase, roads);
            // set all traffic lights to red which are orange
            for (int i = 0; i < phaseLights.length; i++) {
                if (phaseLights[i].getLightStatus() == TrafficLightStatus.ORANGE) {
                    int roadIndex = phaseLights[i].getDirection().ordinal();
                    setLight(phaseLights[i], TrafficLightStatus.RED, roads[roadIndex], tlJunction, phaseLanes[i]);
                }
            }
            // get the next phase, it's corresponding lights and lanes
            currentPhase = incrementPhase();
            if (!isRedPhase(currentPhase, trafficLights)) {
                phaseLights = getPhaseLights(currentPhase, trafficLights);
                phaseLanes = getPhaseLanes(currentPhase, roads);
                // set them to green
                for (int i = 0; i < phaseLights.length; i++) {
                    int roadIndex = phaseLights[i].getDirection().ordinal();
                    setLight(phaseLights[i], TrafficLightStatus.GREEN, roads[roadIndex], tlJunction, phaseLanes[i]);
                }
            }
        }
    }

    private TrafficLight[] getPhaseLights(TLPhase phase, TrafficLight[][] trafficLights) {
        ArrayList<TrafficLight> phaseLightList = new ArrayList<TrafficLight>();
        if (phase.getDirection() == null) {
            return null;
        }
        for (int i = 0; i < phase.getDirection().length; i++) {
            switch (phase.getDirection()[i]) {
                case NORTH:
                    switch (trafficLights[0].length) {
                        case 0:
                            break;
                        case 1:
                            phaseLightList.add(trafficLights[0][0]);
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[0][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[0][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[0][1]);
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[0][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[0][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[0][2]);
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[0][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[0][1]);
                                    phaseLightList.add(trafficLights[0][2]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[0][3]);
                                    break;
                                case RIGHTDOUBLE:
                                    phaseLightList.add(trafficLights[0][2]);
                                    phaseLightList.add(trafficLights[0][3]);
                                    break;
                                case LEFTDOUBLE:
                                    phaseLightList.add(trafficLights[0][0]);
                                    phaseLightList.add(trafficLights[0][1]);
                                    break;
                            }
                            break;
                    }
                    break;
                case WEST:
                    switch (trafficLights[1].length) {
                        case 0:
                            break;
                        case 1:
                            phaseLightList.add(trafficLights[1][0]);
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[1][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[1][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[1][1]);
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[1][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[1][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[1][2]);
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[1][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[1][1]);
                                    phaseLightList.add(trafficLights[1][2]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[1][3]);
                                    break;
                            }
                            break;
                    }
                    break;
                case SOUTH:
                    switch (trafficLights[2].length) {
                        case 0:
                            break;
                        case 1:
                            phaseLightList.add(trafficLights[2][0]);
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[2][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[2][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[2][1]);
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[2][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[2][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[2][2]);
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[2][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[2][1]);
                                    phaseLightList.add(trafficLights[2][2]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[2][3]);
                                    break;
                            }
                            break;
                    }
                    break;
                case EAST:
                    switch (trafficLights[3].length) {
                        case 0:
                            break;
                        case 1:
                            phaseLightList.add(trafficLights[3][0]);
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[3][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[3][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[3][1]);
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[3][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[3][1]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[3][2]);
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[3][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[3][1]);
                                    phaseLightList.add(trafficLights[3][2]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[3][3]);
                                    break;
                            }
                            break;
                        case 5:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLightList.add(trafficLights[3][0]);
                                    break;
                                case STRAIGHT:
                                    phaseLightList.add(trafficLights[3][1]);
                                    phaseLightList.add(trafficLights[3][2]);
                                    phaseLightList.add(trafficLights[3][3]);
                                    break;
                                case RIGHT:
                                    phaseLightList.add(trafficLights[3][4]);
                                    break;
                            }
                            break;

                    }
                    break;
            }
        }
        TrafficLight[] phaseLights = new TrafficLight[phaseLightList.size()];
        for (int i = 0; i < phaseLights.length; i++) {
            phaseLights[i] = phaseLightList.get(i);
        }
        return phaseLights;
    }

    private Lane[] getPhaseLanes(TLPhase phase, Road[] roads) {
        ArrayList<Lane> phaseLaneList = new ArrayList<Lane>();
        for (int i = 0; i < phase.getDirection().length; i++) {
            switch (phase.getDirection()[i]) {
                case NORTH:
                    switch (roads[0].getLanes().size()) {
                        case 0:
                            break;
                        case 1:
                            phaseLaneList.add(roads[0].getLanes().get(0));
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[0].getLanes().get(1));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[0].getLanes().get(0));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[0].getLanes().get(0));
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[0].getLanes().get(2));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[0].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[0].getLanes().get(0));
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[0].getLanes().get(3));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[0].getLanes().get(2));
                                    phaseLaneList.add(roads[0].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[0].getLanes().get(0));
                                    break;
                                case RIGHTDOUBLE:
                                    phaseLaneList.add(roads[0].getLanes().get(1));
                                    phaseLaneList.add(roads[0].getLanes().get(0));
                                    break;
                                case LEFTDOUBLE:
                                    phaseLaneList.add(roads[0].getLanes().get(3));
                                    phaseLaneList.add(roads[0].getLanes().get(2));
                                    break;
                            }
                            break;
                    }
                    break;
                case WEST:
                    switch (roads[1].getLanes().size()) {
                        case 0:
                            break;
                        case 1:
                            phaseLaneList.add(roads[1].getLanes().get(0));
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[1].getLanes().get(1));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[1].getLanes().get(0));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[1].getLanes().get(0));
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[1].getLanes().get(2));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[1].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[1].getLanes().get(0));
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[1].getLanes().get(3));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[1].getLanes().get(2));
                                    phaseLaneList.add(roads[1].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[1].getLanes().get(0));
                                    break;
                            }
                            break;
                    }
                    break;
                case SOUTH:
                    switch (roads[2].getLanes().size()) {
                        case 0:
                            break;
                        case 1:
                            phaseLaneList.add(roads[2].getLanes().get(0));
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[2].getLanes().get(0));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[2].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[2].getLanes().get(1));
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[2].getLanes().get(0));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[2].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[2].getLanes().get(2));
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[2].getLanes().get(0));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[2].getLanes().get(1));
                                    phaseLaneList.add(roads[2].getLanes().get(2));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[2].getLanes().get(3));
                                    break;
                            }
                            break;
                    }
                    break;
                case EAST:
                    switch (roads[3].getLanes().size()) {
                        case 0:
                            break;
                        case 1:
                            phaseLaneList.add(roads[3].getLanes().get(0));
                            break;
                        case 2:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[3].getLanes().get(0));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(1));
                                    break;
                            }
                            break;
                        case 3:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[3].getLanes().get(0));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(1));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(2));
                                    break;
                            }
                            break;
                        case 4:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[3].getLanes().get(0));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(1));
                                    phaseLaneList.add(roads[3].getLanes().get(2));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(3));
                                    break;
                            }
                            break;
                        case 5:
                            switch (phase.getTurn()[i]) {
                                case LEFT:
                                    phaseLaneList.add(roads[3].getLanes().get(0));
                                    break;
                                case STRAIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(1));
                                    phaseLaneList.add(roads[3].getLanes().get(2));
                                    phaseLaneList.add(roads[3].getLanes().get(3));
                                    break;
                                case RIGHT:
                                    phaseLaneList.add(roads[3].getLanes().get(4));
                                    break;
                            }
                            break;
                    }
                    break;
            }
        }
        Lane[] phaseLanes = new Lane[phaseLaneList.size()];
        for (int i = 0; i < phaseLanes.length; i++) {
            phaseLanes[i] = phaseLaneList.get(i);
        }
        return phaseLanes;
    }

    private boolean isInNextPhase(TrafficLight light, TrafficLight[] nLights) {
        for (int i = 0; i < nLights.length; i++) {
            if (light == nLights[i]) {
                return true;
            }
        }
        return false;
    }

    private TLPhase getnextTLPhase() {
        return phases[(currentPhaseIndex + 1) % phases.length];
    }

    private TLPhase incrementPhase() {
        currentPhaseIndex = (currentPhaseIndex + 1) % phases.length;
        return phases[currentPhaseIndex];
    }

    private void setLight(TrafficLight trafficLight, TrafficLightStatus status, Road r, TrafficLights tlJunction, Lane lane) {
        trafficLight.setTrafficLightStatus(status);
        switch (status) {
            case RED:
                tlJunction.addGate(r, lane);
                break;
            case ORANGE:
                tlJunction.setSpeedLimits(r, lane);
            case GREEN:
                tlJunction.deleteGate(r, lane);
                tlJunction.deleteGate(r, lane);
                break;
        }
    }

    private boolean isRedPhase(TLPhase cPhase, TrafficLight[][] trafficLights) {
        return getPhaseLights(cPhase, trafficLights) == null;
    }
    
    public void setPhases (TLPhase[] phases){
        this.phases = phases;
    }
}
