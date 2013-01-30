/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Data.Junctions.TrafficLightStatus;
import GUI.Direction;
import java.io.Serializable;

/**
 *
 * @author rik_claessens
 */
public class TrafficLight implements Serializable{
    private TrafficLightStatus trafficLightStatus;
    private Direction direction;
    
    public TrafficLight(Direction dir, TrafficLightStatus status){
        this.direction = dir;
        this.trafficLightStatus = status;
    }
    
    public void setTrafficLightStatus(TrafficLightStatus status){
        this.trafficLightStatus = status;
    }
    
    public void cycleTrafficLightStatus(){
        switch (trafficLightStatus){
            case RED:
                this.trafficLightStatus = TrafficLightStatus.GREEN;
                break;
            case ORANGE:
                this.trafficLightStatus = TrafficLightStatus.RED;
                break;
            case GREEN:
                this.trafficLightStatus = TrafficLightStatus.ORANGE;
                break;
        }
    }
    
    public TrafficLightStatus getLightStatus(){
        return trafficLightStatus;
    }
    
    public Direction getDirection(){
        return direction;
    }
}
