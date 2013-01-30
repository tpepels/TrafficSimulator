package Data;

import Data.Junctions.Junction;
import GUI.ControlPanel;
import GUI.SimulationPanel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tom Pepels
 */
public class Road implements Serializable {

    private double length;                          // The length of the road in m
    private double extraStartLength, extraEndLength;// The part of the road which is on the junction
    private int startnode, endnode;                 // The starting-and-ending-node of the road in the road-graph
    private double maxSpeedCars;
    private double maxSpeedTrucks;
    private String name;                            // Streetname of the road
    private int noLanes;                              // The number of noLanes this road has.
    private String fromDirection, toDirection;      // The direction of the road, don't know if this should be a string..
    private LinkedList<Lane> lanes;
    private static double LCFactor = 0.8;
    private static double mergeGap = 2;
    private static int totalLC = 0;
    private static double maxDistanceMLCCheck = 500; //Distance to junction at which cars start to MLC
    private double averageSpeed = 0;
    private int noOfCars = 0;
    private boolean drawn;
    public static double speedWeight = 0.1;
    public static double densityWeight = 0.9;
    private static long DLCTreshold = 10000;

    private enum direction {

        LEFT, STRAIGHT, RIGHT
    };

    /**
     * 
     * @param startNode
     * @param endNode
     * @param length
     * @param maxSpeedCars
     * @param maxSpeedTrucks
     * @param lanes 
     */
    public Road(int startNode, int endNode, double length, double maxSpeedCars,
            double maxSpeedTrucks, int lanes) {
        this.startnode = startNode;
        this.endnode = endNode;
        this.length = length;
        extraEndLength = extraStartLength = 5;
        this.maxSpeedCars = maxSpeedCars;
        this.maxSpeedTrucks = maxSpeedTrucks;
        this.noLanes = lanes;
        this.lanes = new LinkedList();
        this.drawn = false;
        for (int i = 0; i < noLanes; i++) {
            this.lanes.add(new Lane(this.length));
        }
    }

    /**
     * 
     * @param car
     * @param initial 
     */
    public void addCar(Car car, boolean initial) {
        addCar(car, 0, initial);
    }

    public boolean drawn() {
        return this.drawn;
    }

    public void setDrawn(boolean d) {
        this.drawn = d;
    }

    /**
     * 
     * @param car
     * @param laneIndex
     * @param initial 
     */
    public void addCar(Car car, int laneIndex, boolean initial) {

        if (initial) {
            car.setPosition(extraStartLength);

            for (Car cCar : this.getLanes().get(laneIndex).getCars()) {
                // Put the cars one meter apart initially.
                cCar.setPosition(cCar.getPosition() + car.getLength() + 2);
            }
        }
        getLanes().get(laneIndex).addCar(car);
    }

    /**
     * 
     * @param car 
     */
    public void removeCar(Car car) {
        for (Lane lane : this.lanes) {
            lane.deleteCar(car);
        }
    }

    private enum LCBlock {

        FREE, FRONT, CENTER, REAR;
    }

    public LCBlock checkSpaceForLC(Car car, Lane newLane) {
        //Gap acceptance - check if there is room for a LC
        double curCarPos = car.getPosition();

        double max = curCarPos + mergeGap + car.getLength();
        double min = curCarPos - mergeGap;
        LCBlock room = LCBlock.FREE;

        for (Car c : newLane.getCars()) {
            if (!c.isIsGate()) {
                double pos = c.getPosition();
                double len = c.getLength();

                if (pos <= max && pos >= min) {
                    room = LCBlock.FRONT;
                }

                if (max <= pos + len && max >= pos) {
                    room = LCBlock.CENTER;
                }

                if (min >= pos && min <= pos + len) {
                    room = LCBlock.REAR;
                }
            }
        }
        return room;
    }

    public int getListPositionNextLane(Car car, Lane newLane) {
        int listPosition = 0;

        double curCarPos = car.getPosition();
        double max = curCarPos + mergeGap + car.getLength();
        double min = curCarPos - mergeGap;

        List<Car> cars = newLane.getCars();

        for (int q = cars.size() - 1; q >= 0; q--) {
            double pos = newLane.getCar(q).getPosition();
            if (pos > max) {
                if (q - 1 >= 0) {
                    Car evCar = cars.get(q - 1);
                    double posNext = evCar.getPosition() + evCar.getLength();
                    if (min < posNext) {
                        listPosition = q + 1;
                        break;
                    }
                } else {
                    listPosition = q + 1;
                    break;
                }
            }
        }

        if (listPosition == 0 && !cars.isEmpty()) {
            if (cars.get(0).isIsGate()) {
                listPosition++;
            } else {
                // System.out.println("There can be only one!");
            }
        }

        return listPosition;
    }

    /**
     * 
     */
    public void handleLC(Junction caller, boolean onlyDLC) {
        //Calculate density for lanes.
        Lane bestLane = null;

        //Loop through lanes
        noOfCars = 0;
        for (int i = 0; i < this.getLanes().size(); i++) {
            Lane lane = this.getLanes().get(i);
            //Per lane, loop through each car.
            //if (lane != bestLane) {
            noOfCars += lane.getCars().size();
            for (int j = 0; j < lane.getCars().size(); j++) {
                Car curCar = lane.getCar(j);

                if (curCar.isIsGate() || curCar.getVelocity() < 0.5 || curCar.getLastLC() + DLCTreshold > System.currentTimeMillis()) {
                    continue;
                }

                //Check for MLC
                if (((this.getLength() - curCar.getPosition()) <= getMLCDistance()) && onlyDLC == false && !curCar.isIsGate()) {

                    //Determine target road and direction
                    int upcomingJunctionNumber;
                    List path = new ArrayList(curCar.getPath());
                    upcomingJunctionNumber = Integer.parseInt(path.get(0).toString());

                    int oldInd = -1;
                    int newInd = -1;
                    for (int x = 0; x < caller.getIncomingRoads().length; x++) {
                        if (caller.getIncomingRoads()[x] != null) {
                            if (caller.getIncomingRoads()[x] == this) {
                                oldInd = x;
                                break;
                            }
                        }
                    }

                    //Look further ahead when junction has only 1 outgoing road (OnRamp, OffRamp).
                    int count = 0;
                    for (Road r : caller.getOutgoingRoads()) {
                        if (r != null) {
                            count++;
                        }
                    }
                    while (count == 1) {
                        if (count == 1) { // Only 1 outgoing, no LC required, look at next junction
                            List<Junction> list = SimulationPanel.getJunctionList();
                            for (int u = 0; u < list.size(); u++) {
                                for (int x = 0; x < caller.getOutgoingRoads().length; x++) {
                                    if (caller.getOutgoingRoads()[x] != null) {
                                        if (caller.getOutgoingRoads()[x].endnode == list.get(u).getNodeNr()) {
                                            caller = list.get(u);
                                            break;
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        count = 0;
                        for (Road r : caller.getOutgoingRoads()) {
                            if (r != null) {
                                count++;
                            }
                        }
                    }

                    for (int x = 0; x < caller.getOutgoingRoads().length; x++) {
                        if (caller.getOutgoingRoads()[x] != null) {
                            if (caller.getOutgoingRoads()[x].getEndnode() == upcomingJunctionNumber) {
                                newInd = x;
                                break;
                            }
                        }
                    }
                    if (oldInd != -1 && newInd != -1 && !curCar.isIsGate()) {
                        Road lRoad = caller.getIncomingRoads()[(oldInd + 1) % 4];
                        Road sRoad = caller.getIncomingRoads()[(oldInd + 2) % 4];
                        Road rRoad = caller.getIncomingRoads()[(oldInd + 3) % 4];
                        Road cRoad = caller.getIncomingRoads()[oldInd];

                        Road targetRoad = null;
                        int targetDirection = 0;
                        int targetLane = i;
                        if ((oldInd + 1) % 4 == newInd) {
                            targetDirection = direction.LEFT.ordinal();
                            //curCar.setName("LEFT");
                            targetRoad = lRoad;
                            if (oldInd == 0 || oldInd == 1) {
                                if (i != (this.getLanes().size() - 1)) {
                                    targetLane = i + 1;
                                } else {
                                    targetLane = i;
                                }
                            } else if (oldInd == 2 || oldInd == 3) {
                                if (i != 0) {
                                    targetLane = i - 1;
                                } else {
                                    targetLane = i;
                                }
                            }
                        } else if ((oldInd + 2) % 4 == newInd) {
                            targetDirection = direction.STRAIGHT.ordinal();
                            //curCar.setName("STRAIGHT");
                            targetRoad = sRoad;
                            if (this.lanes.size() == 1) {
                                targetLane = i;
                            } else if (this.lanes.size() == 2) {
                                if (oldInd == 0 || oldInd == 1) {
                                    targetLane = 0;
                                } else if (oldInd == 2 || oldInd == 3) {
                                    targetLane = 1;
                                }
                            } else if (this.lanes.size() == 3) {
                                targetLane = 1;
                            } else if (this.lanes.size() == 4) {
                                if (oldInd == 0 || oldInd == 1) {
                                    if (i == 0 || i == 1) {
                                        targetLane = 1;
                                    } else if (i == 2 || i == 3) {
                                        targetLane = 2;
                                    }
                                } else if (oldInd == 2 || oldInd == 3) {
                                    if (i == 0 || i == 1) {
                                        targetLane = 1;
                                    } else if (i == 2 || i == 3) {
                                        targetLane = 2;
                                    }
                                }
                            } else if (this.lanes.size() == 5) {
                                if (i == 0) {
                                    targetLane = i + 1;
                                } else if (i == lanes.size() - 1) {
                                    targetLane = lanes.size() - 2;
                                } else if (i == 1) {
                                    if (this.lanes.get(2).getCars().size() < Road.LCFactor * this.lanes.get(i).getCars().size() && checkSpaceForLC(curCar, this.getLanes().get(2)) == LCBlock.FREE) {
                                            targetLane = 2;
                                    } else {
                                        targetLane = i;
                                    }
                                } else if (i == 2) {
                                    if (this.lanes.get(1).getCars().size() < Road.LCFactor * this.lanes.get(i).getCars().size() && checkSpaceForLC(curCar, this.getLanes().get(1)) == LCBlock.FREE) {
                                        targetLane = 1;
                                    } else if (this.lanes.get(3).getCars().size() < Road.LCFactor * this.lanes.get(i).getCars().size() && checkSpaceForLC(curCar, this.getLanes().get(3)) == LCBlock.FREE) {
                                        targetLane = 3;
                                    } else {
                                        targetLane = i;
                                    }
                                } else if (i == 3) {
                                    if (this.lanes.get(2).getCars().size() < Road.LCFactor * this.lanes.get(i).getCars().size() && checkSpaceForLC(curCar, this.getLanes().get(2)) == LCBlock.FREE) {
                                        targetLane = 2;
                                    } else {
                                        targetLane = i;
                                    }
                                }
                            }
                        } else if ((oldInd + 3) % 4 == newInd) {
                            targetDirection = direction.RIGHT.ordinal();
                            //curCar.setName("RIGHT");
                            targetRoad = rRoad;
                            if (oldInd == 0 || oldInd == 1) {
                                if (i != 0) {
                                    targetLane = i - 1;
                                } else {
                                    targetLane = i;
                                }
                            } else if (oldInd == 2 || oldInd == 3) {
                                if (i != (this.getLanes().size() - 1)) {
                                    targetLane = i + 1;
                                } else {
                                    targetLane = i;
                                }
                            }
                        }

                        // The car should change lanes
                        if (this.getLanes().get(targetLane) != lane) {

                            Lane bestAdjLane = this.getLanes().get(targetLane);
                            // Is there any room available on the selected lane?
                            LCBlock room = checkSpaceForLC(curCar, bestAdjLane);

                            if (room == LCBlock.FREE) {
                                curCar.setName("");
                                // Get the list position of the car on the desired lane
                                int listPosition = this.getListPositionNextLane(curCar, bestAdjLane);

                                if (!curCar.isIsGate()) {
                                    bestAdjLane.addCar(listPosition, curCar);
                                    lane.deleteCar(j);
                                    Road.totalLC += 1;
                                    ControlPanel.setTotalLC(true);
//                                    System.out.println("MLC");
                                    curCar.setLastLC(System.currentTimeMillis());
                                }
                            } else {
                                curCar.setName("Can't MLC to lane " + targetLane);
                                // Speed up a little, see if we can get it next time.
                                if (curCar.getdVelocity() > 0) {
                                    if (room == LCBlock.CENTER || room == LCBlock.REAR) {
                                        curCar.setdVelocity(curCar.getdVelocity() + 1);
                                    } else if (room == LCBlock.FRONT && curCar.getdVelocity() > this.maxSpeedTrucks - 5) {
                                        curCar.setdVelocity(curCar.getdVelocity() - 1);
                                    }
                                }
                            }
                        }
                    }
                } else{ //Else DLC
                    //Get the best lane out of the adjacent lanes
                    int[] adjacentLanes = this.getAdjacentLanes(i, this.getLanes().size());

                    Lane bestAdjLane = lane;

                    double evalAlt = Integer.MAX_VALUE;
                    double evalCur = Integer.MAX_VALUE;

                    for (int k = 0; k < adjacentLanes.length; k++) {
                        Lane curLane = this.getLanes().get(adjacentLanes[k]);
                        curLane.calculateValue();
                        evalAlt = (this.speedWeight * ((1 / curLane.calculateAvgSpeed()))) + (this.densityWeight * curLane.getDensity());

                        if (evalAlt < Road.LCFactor * evalCur && Math.abs(curCar.getdVelocity() - curCar.getVelocity()) > 0) {
                            bestAdjLane = curLane;
                            evalCur = evalAlt;
                        }
                    }

                    if (bestAdjLane != lane) {
                        // Is there any room available on the selected lane?
                        LCBlock room = checkSpaceForLC(curCar, bestAdjLane);

                        if (room == LCBlock.FREE) {
                            // Get the list position of the car on the desired lane
                            int listPosition = this.getListPositionNextLane(curCar, bestAdjLane);

                            if (!curCar.isIsGate()) {
                                bestAdjLane.addCar(listPosition, curCar);
                                lane.deleteCar(j);
                                Road.totalLC += 1;
                                ControlPanel.setTotalLC(true);
                                long nonsense = curCar.getLastLC();
                                long nonsense2 = System.currentTimeMillis();
                                curCar.setLastLC(System.currentTimeMillis());                              
//                                System.out.println("DLC");
                            }
                        }
                    }
                }
            }
        }
    }

    public int evaluateBestAdjLane(Lane lane, int curLaneIndex, Car curCar) {
        int[] adjacentLanes = this.getAdjacentLanes(curLaneIndex, this.getLanes().size());
        int bestAdjLaneIndex = curLaneIndex;
        Lane bestAdjLane = lane;
        double evalAlt = Integer.MAX_VALUE;
        double evalCur = Integer.MAX_VALUE;
        for (int k = 0; k < adjacentLanes.length; k++) {
            Lane curLane = this.getLanes().get(adjacentLanes[k]);
            curLane.calculateValue();
            evalAlt = (this.speedWeight * ((1 / curLane.calculateAvgSpeed()))) + (this.densityWeight * curLane.getDensity());

            if (evalAlt < Road.LCFactor * evalCur) {
                bestAdjLaneIndex = adjacentLanes[k];
                if (bestAdjLaneIndex == 3) {
                    int nonsense = 4;
                }
                bestAdjLane = curLane;
                evalCur = evalAlt;
            }
        }

        return bestAdjLaneIndex;
    }

    public double getMLCDistance() {
        double distance = this.length;
        for (Lane l : lanes) {
            if (!l.getCars().isEmpty()) {
                if (!l.getCars().getFirst().isIsGate()) {
                    double lastPos = length - l.getCars().getLast().getPosition();
                    if (lastPos < distance) {
                        distance = lastPos;
                    }
                } else {
                    //       System.out.println("maxDistance");
                    return maxDistanceMLCCheck;
                }
            } else {
                //     System.out.println("maxDistance");
                return maxDistanceMLCCheck;
            }
        }
        // System.out.println(distance + maxDistanceMLCCheck);
        return (distance + maxDistanceMLCCheck);
    }

    /**
     * 
     * @param lane
     * @param maxLanes
     * @return 
     */
    public int[] getAdjacentLanes(int lane, int maxLanes) {
        if (maxLanes == 1) {
            return new int[]{lane};
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < maxLanes; i++) {
            list.add(i);
        }

        if (lane == 0) {
            return new int[]{list.get(1)};
        } else if (lane == list.size() - 1) {
            return new int[]{list.get(list.size() - 2)};
        } else {
            return new int[]{list.get(lane - 1), list.get(lane + 1)};
        }
    }

    /**
     * @return the maxSpeedCars
     */
    public double getMaxSpeedCars() {
        return maxSpeedCars;
    }

    /**
     * @return the maxSpeedTrucks
     */
    public double getMaxSpeedTrucks() {
        return maxSpeedTrucks;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @return the startnode
     */
    public int getStartnode() {
        return startnode;
    }

    /**
     * @return the endnode
     */
    public int getEndnode() {
        return endnode;
    }

    /**
     * 
     * @return First car of the road, null if no such element 
     */
    public Car getFirstCar() {
        Car fCar = null;
        for (Lane l : lanes) {
            if (l != null) {
                if (l.getCars().size() > 0) {
                    fCar = l.getCars().getFirst();
                    break;
                }
            }
        }

        for (Lane l : lanes) {
            if (fCar != null) {
                if (l.getCars().size() > 0) {
                    if (l.getCars().getFirst().getPosition() > fCar.getPosition()) {
                        fCar = l.getCars().getFirst();
                    }
                }
            }
        }
        return fCar;
    }

    /**
     * 
     * @return the lane which has the first car on it, null if no such element 
     */
    public Lane getLaneWithFirstCar() {
        Lane fLane = null;
        Car fCar = null;
        for (Lane l : lanes) {
            if (l != null) {
                if (l.getCars().size() > 0) {
                    fLane = l;
                    fCar = fLane.getCars().getFirst();
                    break;
                }
            }
        }
        for (Lane l : lanes) {
            if (l != null) {
                if (l.getCars().size() > 0) {
                    if (l.getCars().getFirst().getPosition() > fCar.getPosition()) {
                        fLane = l;
                    }
                }
            }

        }
        return fLane;
    }

    /**
     * @return the lanes
     */
    public LinkedList<Lane> getLanes() {
        return lanes;
    }

    /**
     * @return the LCFactor
     */
    public static double getLCFactor() {
        return LCFactor;
    }

    /**
     * @param aLCFactor the LCFactor to set
     */
    public static void setLCFactor(double aLCFactor) {
        LCFactor = aLCFactor;
    }

    /**
     * @return the mergeGap
     */
    public static double getMergeGap() {
        return mergeGap;
    }

    /**
     * @param aMergeGap the mergeGap to set
     */
    public static void setMergeGap(double aMergeGap) {
        mergeGap = aMergeGap;
    }

    /**
     * @return the totalLC
     */
    public static int getTotalLC() {
        return totalLC;
    }

    /**
     * @param aTotalLC the totalLC to set
     */
    public static void setTotalLC(int aTotalLC) {
        totalLC = aTotalLC;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getNoOfCars() {
        return noOfCars;
    }

      public int getNoOfCarsNearEnd(int distance){
        int cars = 0;
        for (Lane lane : this.getLanes()){
            for (Car car : lane.getCars()){
                if (car.getPosition() + car.getLength() >= this.length - distance){
                    cars++;
                }
                break;
            }
        }
        return cars;
    }
      
    public void setNoOfCars(int noOfCars) {
        this.noOfCars = noOfCars;
    }

    public int nextPreferredLane(Junction caller, Car car) {
        int targetLane = 0;
        //for (int i = 0; i < this.getLanes().size(); i++) {
        //Lane lane = this.getLanes().get(i);
        Car curCar = car;
        List path = new ArrayList(curCar.getPath());
        int upcomingJunctionNumber = Integer.parseInt(path.get(0).toString());

        int oldInd = -1;
        int newInd = -1;
        for (int x = 0; x < caller.getIncomingRoads().length; x++) {
            if (caller.getIncomingRoads()[x] != null) {
                if (caller.getIncomingRoads()[x] == this) {
                    oldInd = x;
                    break;
                }
            }
        }

        //Look further ahead when junction has only 1 outgoing road (OnRamp, OffRamp).
        int count = 0;
        for (Road r : caller.getOutgoingRoads()) {
            if (r != null) {
                count++;
            }
        }

        int iterations = 0;
        while (count == 1) {
            if (count == 1) { // Only 1 outgoing, no LC required, look at next junction
                List<Junction> list = SimulationPanel.getJunctionList();
                boolean found = false;
                for (int u = 0; u < list.size(); u++) {
                    if (found == true) {
                        break;
                    }
                    for (int x = 0; x < caller.getOutgoingRoads().length; x++) {
                        if (caller.getOutgoingRoads()[x] != null) {
                            if (caller.getOutgoingRoads()[x].endnode == list.get(u).getNodeNr()) {
                                caller = list.get(u);
                                iterations++;
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }

            count = 0;
            for (Road r : caller.getOutgoingRoads()) {
                if (r != null) {
                    count++;
                }
            }
        }

        for (int x = 0; x < caller.getOutgoingRoads().length; x++) {
            if (caller.getOutgoingRoads()[x] != null) {
                if (caller.getOutgoingRoads()[x].getEndnode() == path.get(iterations)) {
                    newInd = x;
                    break;
                }
            }
        }

        if (oldInd != -1 && newInd != -1) {
            Road lRoad = caller.getIncomingRoads()[(oldInd + 1) % 4];
            Road sRoad = caller.getIncomingRoads()[(oldInd + 2) % 4];
            Road rRoad = caller.getIncomingRoads()[(oldInd + 3) % 4];
            Road cRoad = caller.getIncomingRoads()[oldInd];

            Road targetRoad = null;
            int targetDirection = 0;
            //targetLane = i;
            if ((oldInd + 1) % 4 == newInd) {
                targetDirection = direction.LEFT.ordinal();
                targetRoad = lRoad;
                if (oldInd == 0 || oldInd == 1) {
                    targetLane = (this.getLanes().size() - 1);
                } else if (oldInd == 2 || oldInd == 3) {
                    targetLane = 0;
                }
            } else if ((oldInd + 2) % 4 == newInd) {
                targetDirection = direction.STRAIGHT.ordinal();
                targetRoad = sRoad;
                if (this.lanes.size() == 1) {
                    targetLane = 0;
                } else if (this.lanes.size() == 2) {
                    if (oldInd == 0 || oldInd == 1) {
                        targetLane = 0;
                    } else if (oldInd == 2 || oldInd == 3) {
                        targetLane = 1;
                    }
                } else if (this.lanes.size() == 3) {
                    targetLane = 1;
                } else if (this.lanes.size() == 4) {
                    if (oldInd == 0 || oldInd == 1) {
                        //if (i == 0 || i == 1) {
                        targetLane = 2;
                        /*} else if (i == 2 || i == 3) {
                        targetLane = 1;
                        }*/
                    } else if (oldInd == 2 || oldInd == 3) {
                        //if (i == 0 || i == 1) {
                        targetLane = 1;
                        /*} else if (i == 2 || i == 3) {
                        targetLane = 2;
                        }*/
                    }
                }
            } else if ((oldInd + 3) % 4 == newInd) {
                targetDirection = direction.RIGHT.ordinal();
                targetRoad = rRoad;
                if (oldInd == 0 || oldInd == 1) {
                    targetLane = 0;
                } else if (oldInd == 2 || oldInd == 3) {
                    targetLane = (this.getLanes().size() - 1);
                }
            }

            if ("willempie".equals(caller.name)) {
                System.out.println("Direction: " + targetDirection + " lane " + targetLane);
            }
            return targetLane;
        }
        return -1;
    }
}
