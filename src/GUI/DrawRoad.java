package GUI;

import Data.Junctions.Junction;

/**
 *
 * @author rik_claessens
 */
public class DrawRoad {

    private int[] outgoing = new int[]{-1, -1}, incoming = new int[]{-1, -1};
    private Junction outgoingJunction, incomingJunction;

    public Junction getIncomingJunction() {
        return incomingJunction;
    }

    public void setIncomingJunction(Junction incomingJunction) {
        this.incomingJunction = incomingJunction;
    }

    public void setIncoming(int[] incoming) {
        this.incoming = incoming;
    }

    public void setOutgoing(int[] outgoing) {
        this.outgoing = outgoing;
    }

    public int[] getIncoming() {
        return incoming;
    }

    public int[] getOutgoing() {
        return outgoing;
    }

    public Junction getOutgoingJunction() {
        return outgoingJunction;
    }

    public void setOutgoingJunction(Junction junction) {
        this.outgoingJunction = junction;
    }

    public double getLength() {
        return Math.max(Math.abs(outgoingJunction.getGridX() - incomingJunction.getGridX()), Math.abs(outgoingJunction.getGridY() - incomingJunction.getGridY()));
    }

    public Direction getOutgoingDirection() {
        if (outgoingJunction.getGridX() == incomingJunction.getGridX()) {
            if (outgoingJunction.getGridY() < incomingJunction.getGridY()) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        } else if (outgoingJunction.getGridX() < incomingJunction.getGridX()) {
            return Direction.EAST;
        } else {
            return Direction.WEST;
        }
    }
    
    public Direction getIncomingDirection() {
        if (outgoingJunction.getGridX() == incomingJunction.getGridX()) {
            if (outgoingJunction.getGridY() < incomingJunction.getGridY()) {
                return Direction.SOUTH;
            } else {
                return Direction.NORTH;
            }
        } else if (outgoingJunction.getGridX() < incomingJunction.getGridX()) {
            return Direction.WEST;
        } else {
            return Direction.EAST;
        }
    }
}
