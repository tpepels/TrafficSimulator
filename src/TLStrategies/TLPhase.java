/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TLStrategies;

import GUI.Direction;
import GUI.Turn;
import java.io.Serializable;

/**
 *
 * @author rik_claessens
 */
public class TLPhase implements Serializable {

    private Direction[] directions;
    private Turn turns[];
    private int greenTime;

    public TLPhase(Direction[] directions, Turn[] turns, int greenTime) {
        this.directions = directions;
        this.turns = turns;
        this.greenTime = greenTime;
    }

    public TLPhase(int greenTime) {
        this.greenTime = greenTime;
    }

    public Direction[] getDirection() {
        return directions;
    }

    public int getGreenTime() {
        return greenTime;
    }

    public Turn[] getTurn() {
        return turns;
    }
}
