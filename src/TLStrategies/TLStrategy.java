/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TLStrategies;

import Data.*;
import Data.Junctions.TrafficLights;

/**
 *
 * @author rik_claessens
 */
public interface TLStrategy {
    public void controlTL(Road[] roads, TrafficLight[][] trafficLights, double timeInterval, TrafficLights tlJunction);
}
