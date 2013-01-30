package Data.Junctions;

import Data.Car;
import Data.Lane;
import Data.Road;

/**
 * Connects two roads
 * @author Tom Pepels
 */
public class Interconnection extends Junction {

    private Car gate;
    private int positionOnroad;

    public Interconnection(int nodeNr, Road startRoad, Road endRoad, int gridX, int gridY, int gridSize) {
        super(JunctionType.Interconnection, nodeNr, gridX, gridY, gridSize);
        gate = new Car();
        addIncomingRoad(0, startRoad);
        addOutgoingRoad(0, endRoad);
        // super.addGate(gate, 0, 100);
    }

    public Interconnection(int nodeNr, int gridX, int gridY, int gridSize) {
        super(JunctionType.Interconnection, nodeNr, gridX, gridY, gridSize);
    }

    public void update(double timeInterval) {
        for (Road r : getIncomingRoads()) {
            if (r != null) {
                for (Lane l : r.getLanes()) {
//                    super.updateFirst(timeInterval, r, l, l.getCars().getFirst().getNextRoad(), l.getCars().getFirst());
                    super.updateLane(timeInterval, l, r);
                }
            }
        }
    }
}
