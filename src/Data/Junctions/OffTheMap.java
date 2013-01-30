package Data.Junctions;

import Data.*;

/**
 *
 * @author Lukas
 */
public class OffTheMap extends Junction {

    public OffTheMap(int nodeNr, int gridX, int gridY, int gridSize) {
        super(JunctionType.Exit, nodeNr, gridX, gridY, gridSize);
    }

    public void update(double timeInterval) {
        super.resetAvSpeedStats();
        for (Road r : getOutgoingRoads()) {
            if (r != null) {
                r.handleLC(this, false);
            }
        }
        // Tom : Do le normal lane update Cliff: ME GUSTA
        for (Road r : getIncomingRoads()) {
            if (r != null) {
                r.handleLC(this, false);
                for (Lane l : r.getLanes()) {
                    if (l.getCars().size() > 0) {
                        Car fCar = l.getCars().getFirst();
                        if (!fCar.isIsGate() && fCar.getPosition() + fCar.getLength() >= (l.getLength() - fCar.getMinSpace())) {
                            // The front of the car reaches the end of the road within its minimal spacing
                            if (fCar.getPath().isEmpty()) {
                                l.deleteCar(fCar);
                            } else {
                                super.updateFirst(timeInterval, r, l, getNextRoadForCar(fCar), fCar);
                            }
                        } else {
                            fCar.drive(0, 0, true, timeInterval);
                        }
                        super.updateLane(timeInterval, l, r);
                    }
                }
            }
        }
    }
}
