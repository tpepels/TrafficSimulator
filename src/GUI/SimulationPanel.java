package GUI;

import Data.Car;
import Data.Junctions.*;
import Data.Road;
import Data.TrafficLight;
import TLStrategies.MarchingControl;
import TLStrategies.PhaseControl;
import TLStrategies.Sotl_phaseControl;
import TLStrategies.Sotl_platoonControl;
import TLStrategies.Sotl_request;
import TLStrategies.TLPhase;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JOptionPane;

public class SimulationPanel extends javax.swing.JPanel {

    // -- Brush for dashed lines
    private final static float dash1[] = {5.0f}; // Settings for the dashed line between roads
    private final static BasicStroke dashed = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f, dash1, 0.0f);
    // --
    private final double scale = 3.5; // One <gridspace> is <scale> meters, to get the scalingfactor: (gridSpace / scale)
    private int gridSpace = 12; // The spacing of the grid in pixels.
    private LinkedList<Car> cars;
    private final int[] mouseCoor = new int[4];
    private int width, height, paintWidth, mapWidth, mapHeight;
    private Image mapImage;
    private Graphics bufferGraphics;
    private Image offscreen;
    private final int[] paintCorner = new int[]{0, 0};
    public static ArrayList<Integer> exitNodes = new ArrayList<Integer>(); // oops, bad design, don't care, sorry
    private ArrayList<ComboItem> junctionTypeList = new ArrayList<ComboItem>();
    private ArrayList<ComboItem> objectTypeList = new ArrayList<ComboItem>();
    private ArrayList<ComboItem> carSpeedList = new ArrayList<ComboItem>();
    private ArrayList<ComboItem> truckSpeedList = new ArrayList<ComboItem>();
    private ArrayList<ComboItem> laneList = new ArrayList<ComboItem>();
    private ArrayList<ComboItem> controlList = new ArrayList<ComboItem>();
    private ArrayList<Color> colorList = new ArrayList<Color>();
    private static ArrayList<Junction> junctionList = new ArrayList<Junction>();
    private ArrayList<Road> roadList = new ArrayList<Road>();
    //private ArrayList<GridPoint> gridPoints = new ArrayList<GridPoint>();
    private GridPoint[][] gridPoints;
    private final Color gridPointColor = Color.LIGHT_GRAY;
    private final Color gridPointHighLightColor = Color.RED;
    private GridPoint mouseGridPoint;
    private DrawRoad drawRoad = new DrawRoad();
    private boolean paintMode = true;
    private Junction selectedJunction;
    private int tempObjGridSize = 1;
    private boolean paintTimestandingStill = false;
    private boolean paintAverageSpeed = false;
    private boolean paintSpeed = false;
    private boolean[] carStatistics;

    /** Creates new form SimulationPanel */
    public SimulationPanel(int w, int h, int mapWidth, int mapHeight) {
        initComponents();
        final int[] paintArea = new int[]{w, h};
        this.width = w - 1;
        this.height = h - 1;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        //final int blockWidth = 30;
        setupJunctionList();
        setupCarSpeedList();
        setupTruckSpeedList();
        setupLaneList();
        setupCarStatistics();
        setupControlList();
        gridPoints = new GridPoint[mapWidth / gridSpace][mapHeight / gridSpace];
        setupGrid();

        final boolean moved = false;
        final int mapW = mapWidth;
        final int mapH = mapHeight;

        class paintCorner implements MouseMotionListener {

            public void mouseDragged(MouseEvent e) {

                paintCorner[0] -= e.getX() - mouseCoor[0];
                paintCorner[1] -= e.getY() - mouseCoor[1];
                paintCorner[0] = Math.max(0, Math.min(paintCorner[0], mapW - paintArea[0]));
                paintCorner[1] = Math.max(0, Math.min(paintCorner[1], mapH - paintArea[1]));

                mouseCoor[0] = e.getX();
                mouseCoor[1] = e.getY();
                repaint();
            }

            public void mouseMoved(MouseEvent e) {
                if (e.getX() > 0 && e.getY() > 0) {
                    int[] nearestGridPoint = getNearestGridPoint(e.getX(), e.getY());
                    if (nearestGridPoint[0] >= 0 && nearestGridPoint[1] >= 0) {
                        setMouseGridPoint(gridPoints[nearestGridPoint[0]][nearestGridPoint[1]]);
                    }
                    repaint();
                }
            }
        }

        class mapPaintListener implements MouseListener {

            public void mouseClicked(MouseEvent me) {

                if (me.isShiftDown() && selectedJunction != null) {
                    System.out.println("Deleting junction");
                    deleteSelectedJunction();
                    repaint();

                } else {
                    int[] nearestGridPoint = getNearestGridPoint(me.getX(), me.getY());
                    Junction junction = junctionClicked(me.getX(), me.getY());
                    if (paintMode) {
                        if (junction != null) {
                            if (drawRoad.getOutgoingJunction() != null && junction.getGridX() == drawRoad.getOutgoingJunction().getGridX() && junction.getGridY() == drawRoad.getOutgoingJunction().getGridY()) {
                                drawRoad = new DrawRoad();
                                selectedJunction = null;
                            } else if (junction.getGridX() == drawRoad.getOutgoing()[0] ^ junction.getGridY() == drawRoad.getOutgoing()[1]) {
                                drawRoad.setIncoming(new int[]{junction.getGridX(), junction.getGridY()});
                                drawRoad.setIncomingJunction(junction);

                                /* if (junction.getType() == JunctionType.TrafficLights) {
                                System.out.println("Incoming is traffic light and direction = " + drawRoad.getOutgoingDirection().toString());
                                } else if (drawRoad.getOutgoingJunction().getType() == JunctionType.TrafficLights) {
                                System.out.println("Outgoing is traffic light");
                                }*/

                                addRoad(drawRoad.getOutgoingJunction().getNodeNr(), drawRoad.getIncomingJunction().getNodeNr());
                                selectedJunction = null;
                            } else {
                                drawRoad.setOutgoing(new int[]{junction.getGridX(), junction.getGridY()});
                                drawRoad.setOutgoingJunction(junction);
                                selectedJunction = junction;
                            }

                            /*
                            
                            Road road = new Road(mapW, width, width, mapWidth, mapWidth, mapW);
                            break;
                            
                            int junction = getJunctionType();
                            if (junction != -1) {
                            list.add(new tempObj(blockWidth, (nearestGridPoint[0] + 1) * gridSpace, (nearestGridPoint[1] + 1) * gridSpace, colorList.get(junction)));
                            repaint();
                            }*/

                        } else {
                            int junctionType = getJunctionType();
                            System.out.println(junctionType);
                            if (junctionType != -1) {

                                switch (junctionType) {
                                    case 0:
                                        getJunctionList().add(new CrossRoads(getJunctionList().size(), nearestGridPoint[0], nearestGridPoint[1], tempObjGridSize));
                                        break;
                                    case 1:
                                        PhaseControl control = new PhaseControl();
                                        TLPhase[] phases = new TLPhase[]{
                                            new TLPhase(new Direction[]{Direction.NORTH, Direction.NORTH, Direction.NORTH}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 10000),
                                            new TLPhase(new Direction[]{Direction.EAST, Direction.EAST, Direction.EAST}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 10000),
                                            new TLPhase(new Direction[]{Direction.SOUTH, Direction.SOUTH, Direction.SOUTH}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 10000),
                                            new TLPhase(new Direction[]{Direction.WEST, Direction.WEST, Direction.WEST}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 10000)
                                        };
                                        control.setPhases(phases);
                                        getJunctionList().add(new TrafficLights(getJunctionList().size(), nearestGridPoint[0], nearestGridPoint[1], tempObjGridSize, new MarchingControl()));
                                        break;
                                    case 2:
                                        int carRate = Integer.parseInt(JOptionPane.showInputDialog("Car spawn rate: ", 12));
                                        int truckRate = Integer.parseInt(JOptionPane.showInputDialog("Truck spawn rate: ", 1));
                                        getJunctionList().add(new Spawn(getJunctionList().size(), nearestGridPoint[0], nearestGridPoint[1], tempObjGridSize, carRate, truckRate));
                                        exitNodes.add(getJunctionList().size() - 1);
                                        break;
                                    case 3:
                                        getJunctionList().add(new OnRamp(getJunctionList().size(), nearestGridPoint[0], nearestGridPoint[1], tempObjGridSize));
                                        break;
                                    case 4:
                                        System.out.println("Creating offramp");
                                        getJunctionList().add(new OffRamp(getJunctionList().size(), nearestGridPoint[0], nearestGridPoint[1], tempObjGridSize));
                                        break;
                                    case 5:
                                        getJunctionList().add(new OffTheMap(getJunctionList().size(), nearestGridPoint[0], nearestGridPoint[1], tempObjGridSize));
                                        exitNodes.add(junctionList.size() - 1);
                                        break;
                                }
                            }
                        }
                    }
                    repaint();
                }
            }

            public void mousePressed(MouseEvent me) {
                mouseCoor[0] = me.getX();
                mouseCoor[1] = me.getY();
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }
        }
        this.addMouseListener(new mapPaintListener());
        this.addMouseMotionListener(new paintCorner());
//        class Wheel implements MouseWheelListener {
//
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                int moved = e.getWheelRotation();
//                if (moved < 0) { // up
//                    gridSpace++;
//                    if(paintMode) {
//                        gridPoints = new GridPoint[mapW / gridSpace][mapH / gridSpace];
//                        setupGrid();
//                    }
//                    repaint();
//                } else { // down
//                    gridSpace--;
//                    if(paintMode) {
//                        gridPoints = new GridPoint[mapW / gridSpace][mapH / gridSpace];
//                        setupGrid();
//                    }
//                    repaint();
//                }
//            }
//        }
//        this.addMouseWheelListener(new Wheel());
    }

    public void makeSmallMap() {
        //Le Junctions
        Spawn WillemAlexanderwegSpawn = new Spawn(0, 203, 0, gridSpace, 11, 1);
        Spawn FranciscusRomanuswegSpawn = new Spawn(1, 203, 67, gridSpace, 7, 1);

        /*
        PhaseControl control = new PhaseControl();
        
        // marching control with variable phases
        TLPhase[] phases = new TLPhase[]{
        new TLPhase(new Direction[]{Direction.NORTH, Direction.NORTH, Direction.NORTH}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 7000),
        new TLPhase(new Direction[]{Direction.EAST, Direction.EAST, Direction.EAST}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 15000),
        new TLPhase(new Direction[]{Direction.SOUTH, Direction.SOUTH, Direction.SOUTH}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 5000),
        new TLPhase(new Direction[]{Direction.WEST, Direction.WEST, Direction.WEST}, new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT}, 20000)
        };
        
        control.setPhases(phases);
         */

        //Sotl_phaseControl control = new Sotl_phaseControl();
        PhaseControl control = new PhaseControl();
        TrafficLights NorderwegViaduct = new TrafficLights(2, 203, 34, gridSpace, control);
        Spawn MeerssenViaduct = new Spawn(3, 279, 34, gridSpace, 24, 2);
        Spawn brug = new Spawn(4, 0, 34, gridSpace, 32, 3);
        OffRamp Willem1 = new OffRamp(5, 203, 8, gridSpace);
        OffRamp Willem2 = new OffRamp(6, 203, 15, gridSpace);
        OffRamp Willem3 = new OffRamp(7, 203, 22, gridSpace);
        Willem3.name = "willempie!";
        OffRamp Franciscus = new OffRamp(8, 203, 57, gridSpace);

        //Le roads
        Road ViaductWestLeft = new Road(3, 2, 250, 50 / 3.6, 50 / 3.6, 4);
        Road ViaductWestRight = new Road(2, 3, 250, 50 / 3.6, 50 / 3.6, 3);
        Road FranciscusRomanusUp = new Road(1, 8, 15, 50 / 3.6, 50 / 3.6, 2);
        Road FranciscusRomanusUp2 = new Road(8, 2, 70, 50 / 3.6, 50 / 3.6, 3);
        Road FranciscusRomanusDown = new Road(2, 1, 100, 50 / 3.6, 50 / 3.6, 1);
        Road WillemAlexanderDown = new Road(0, 5, 28, 50 / 3.6, 50 / 3.6, 1);
//        Road WillemAlexanderDown = new Road(0, 2, 100, 50 / 3.6, 50 / 3.6, 4);
        Road WillemAlexanderDown1 = new Road(5, 6, 25, 50 / 3.6, 50 / 3.6, 2);
        Road WillemAlexanderDown2 = new Road(6, 7, 25, 50 / 3.6, 50 / 3.6, 3);
        Road WillemAlexanderDown3 = new Road(7, 2, 40, 50 / 3.6, 50 / 3.6, 4);
        Road WillwmAlexanderUp = new Road(2, 0, 100, 50 / 3.6, 50 / 3.6, 1);
        Road NorderbrugEastLeft = new Road(2, 4, 700, 70 / 3.6, 70 / 3.6, 2);
        Road NorderbrugEastRight = new Road(4, 2, 700, 70 / 3.6, 70 / 3.6, 5);
//        Road NorderbrugEastRight5 = new Road(8, 2, 55, 70 / 3.6, 70 / 3.6, 5); 
        //Le connections
        Willem1.addIncomingRoad(0, WillemAlexanderDown);
        Willem2.addIncomingRoad(0, WillemAlexanderDown1);
        Willem3.addIncomingRoad(0, WillemAlexanderDown2);
        Willem1.addOutgoingRoad(2, WillemAlexanderDown1);
        Willem2.addOutgoingRoad(2, WillemAlexanderDown2);
        Willem3.addOutgoingRoad(2, WillemAlexanderDown3);
        Franciscus.addIncomingRoad(2, FranciscusRomanusUp);
        Franciscus.addOutgoingRoad(0, FranciscusRomanusUp2);
//        OffBrug.addIncomingRoad(3,NorderbrugEastRight);
//        OffBrug.addOutgoingRoad(1, NorderbrugEastRight5);
        brug.addIncomingRoad(1, NorderbrugEastLeft);
        brug.addOutgoingRoad(1, NorderbrugEastRight);
//        NorderwegViaduct.addIncomingRoad(0, WillemAlexanderDown);
        NorderwegViaduct.addIncomingRoad(0, WillemAlexanderDown3);
        NorderwegViaduct.addIncomingRoad(1, ViaductWestLeft);
        NorderwegViaduct.addIncomingRoad(2, FranciscusRomanusUp2);
        NorderwegViaduct.addIncomingRoad(3, NorderbrugEastRight);
        NorderwegViaduct.addOutgoingRoad(0, WillwmAlexanderUp);
        NorderwegViaduct.addOutgoingRoad(1, ViaductWestRight);
        NorderwegViaduct.addOutgoingRoad(2, FranciscusRomanusDown);
        NorderwegViaduct.addOutgoingRoad(3, NorderbrugEastLeft);
        WillemAlexanderwegSpawn.addIncomingRoad(2, WillwmAlexanderUp);
        WillemAlexanderwegSpawn.addOutgoingRoad(2, WillemAlexanderDown);
        FranciscusRomanuswegSpawn.addIncomingRoad(0, FranciscusRomanusDown);
        FranciscusRomanuswegSpawn.addOutgoingRoad(0, FranciscusRomanusUp);
        MeerssenViaduct.addIncomingRoad(3, ViaductWestRight);
        MeerssenViaduct.addOutgoingRoad(3, ViaductWestLeft);
        getJunctionList().add(WillemAlexanderwegSpawn);
        getJunctionList().add(FranciscusRomanuswegSpawn);
        getJunctionList().add(NorderwegViaduct);
        getJunctionList().add(MeerssenViaduct);
        getJunctionList().add(brug);
        getJunctionList().add(Willem1);
        getJunctionList().add(Willem2);
        getJunctionList().add(Willem3);
        getJunctionList().add(Franciscus);
//        junctionList.add(OffBrug);


        for (int i = 0; i < 15; i++) {
            exitNodes.add(brug.getNodeNr());
            exitNodes.add(MeerssenViaduct.getNodeNr());
        }

        exitNodes.add(WillemAlexanderwegSpawn.getNodeNr());
        exitNodes.add(FranciscusRomanuswegSpawn.getNodeNr());

        exitNodes.add(WIDTH);
    }

    public void makeMap() {
        //Le Junctions
        Spawn StatensingelSpawn = new Spawn(0, 0, 34, gridSpace, 6, 0);
        Spawn CapergerwegSpawn = new Spawn(1, 28, 6, gridSpace, 16, 1);
        Spawn WillemAlexanderwegSpawn = new Spawn(2, 367, 6, gridSpace, 10, 1);
        Spawn MeerssenerwegNorthSpawn = new Spawn(3, 443, 6, gridSpace, 0, 0);
        Spawn ViaductwegSpawn = new Spawn(4, 476, 34, gridSpace, 24, 2);
        Spawn MeerssenwegSouthSpawn = new Spawn(5, 443, 67, gridSpace, 0, 0);
        Spawn FranciscusRomanuswegSpawn = new Spawn(6, 367, 67, gridSpace, 7, 1);
        TrafficLights CaberFrontenStaten = new TrafficLights(7, 28, 34, gridSpace, new PhaseControl());
        TrafficLights NorderwegViaduct = new TrafficLights(8, 367, 34, gridSpace, new PhaseControl());
        TrafficLights MeerssenViaduct = new TrafficLights(9, 443, 34, gridSpace, new PhaseControl());
        CrossRoads onRamp = new CrossRoads(10, 164, 34, gridSpace);
        //Le roads
        Road ViaductEastLeft = new Road(4, 9, 100, 50 / 3.6, 50 / 3.6, 3);      //Check Speed Limits
        Road ViaductEastRight = new Road(9, 4, 100, 50 / 3.6, 50 / 3.6, 3);
        Road ViaductWestLeft = new Road(9, 8, 250, 50 / 3.6, 50 / 3.6, 3);
        Road ViaductWestRight = new Road(8, 9, 250, 50 / 3.6, 50 / 3.6, 3);
        Road MeerssenerwegSouthUp = new Road(5, 9, 100, 50 / 3.6, 50 / 3.6, 2);
        Road MeerssenerwegSouthDown = new Road(9, 5, 100, 50 / 3.6, 50 / 3.6, 2);
        Road MeerssenerwegNorthUp = new Road(9, 3, 100, 50 / 3.6, 50 / 3.6, 2);
        Road MeerssenerwegNorthDown = new Road(3, 9, 100, 50 / 3.6, 50 / 3.6, 2);
        Road FranciscusRomanusUp = new Road(6, 8, 100, 50 / 3.6, 50 / 3.6, 4);
        Road FranciscusRomanusDown = new Road(8, 6, 100, 50 / 3.6, 50 / 3.6, 1);
        Road WillemAlexanderDown = new Road(2, 8, 100, 50 / 3.6, 50 / 3.6, 4);
        Road WillwmAlexanderUp = new Road(8, 2, 100, 50 / 3.6, 50 / 3.6, 1);
        Road NorderbrugEastLeft = new Road(8, 10, 700, 70 / 3.6, 70 / 3.6, 2);
        Road NorderbrugEastRight = new Road(10, 8, 700, 70 / 3.6, 70 / 3.6, 2);
        Road NorderbrugWestLeft = new Road(10, 7, 450, 70 / 3.6, 70 / 3.6, 2);
        Road NorderbrugWestRight = new Road(7, 10, 450, 70 / 3.6, 70 / 3.6, 2);
        Road StatensingelUp = new Road(0, 7, 100, 70 / 3.6, 70 / 3.6, 2);
        Road StatensingelDown = new Road(7, 0, 100, 70 / 3.6, 70 / 3.6, 2);
        Road CabergerwegUp = new Road(7, 1, 100, 70 / 3.6, 70 / 3.6, 2);
        Road CabergerwegDown = new Road(1, 7, 100, 70 / 3.6, 70 / 3.6, 3);
        //Le connections
        StatensingelSpawn.addOutgoingRoad(1, StatensingelUp);
        StatensingelSpawn.addIncomingRoad(1, StatensingelDown);
        CaberFrontenStaten.addIncomingRoad(3, StatensingelUp);
        CaberFrontenStaten.addIncomingRoad(0, CabergerwegDown);
        CaberFrontenStaten.addIncomingRoad(1, NorderbrugWestLeft);
        CaberFrontenStaten.addOutgoingRoad(3, StatensingelDown);
        CaberFrontenStaten.addOutgoingRoad(0, CabergerwegUp);
        CaberFrontenStaten.addOutgoingRoad(1, NorderbrugWestRight);
        CapergerwegSpawn.addIncomingRoad(2, CabergerwegUp);
        CapergerwegSpawn.addOutgoingRoad(2, CabergerwegDown);
        onRamp.addIncomingRoad(3, NorderbrugWestRight);
        onRamp.addIncomingRoad(1, NorderbrugEastLeft);
        onRamp.addOutgoingRoad(3, NorderbrugWestLeft);
        onRamp.addOutgoingRoad(1, NorderbrugEastRight);
        NorderwegViaduct.addIncomingRoad(0, WillemAlexanderDown);
        NorderwegViaduct.addIncomingRoad(1, ViaductWestLeft);
        NorderwegViaduct.addIncomingRoad(2, FranciscusRomanusUp);
        NorderwegViaduct.addIncomingRoad(3, NorderbrugEastRight);
        NorderwegViaduct.addOutgoingRoad(0, WillwmAlexanderUp);
        NorderwegViaduct.addOutgoingRoad(1, ViaductWestRight);
        NorderwegViaduct.addOutgoingRoad(2, FranciscusRomanusDown);
        NorderwegViaduct.addOutgoingRoad(3, NorderbrugEastLeft);
        WillemAlexanderwegSpawn.addIncomingRoad(2, WillwmAlexanderUp);
        WillemAlexanderwegSpawn.addOutgoingRoad(2, WillemAlexanderDown);
        FranciscusRomanuswegSpawn.addIncomingRoad(0, FranciscusRomanusDown);
        FranciscusRomanuswegSpawn.addOutgoingRoad(0, FranciscusRomanusUp);
        MeerssenViaduct.addIncomingRoad(0, MeerssenerwegNorthDown);
        MeerssenViaduct.addIncomingRoad(1, ViaductEastLeft);
        MeerssenViaduct.addIncomingRoad(2, MeerssenerwegSouthUp);
        MeerssenViaduct.addIncomingRoad(3, ViaductWestRight);
        MeerssenViaduct.addOutgoingRoad(0, MeerssenerwegNorthUp);
        MeerssenViaduct.addOutgoingRoad(1, ViaductEastRight);
        MeerssenViaduct.addOutgoingRoad(2, MeerssenerwegSouthDown);
        MeerssenViaduct.addOutgoingRoad(3, ViaductWestLeft);
        MeerssenerwegNorthSpawn.addIncomingRoad(2, MeerssenerwegNorthUp);
        MeerssenerwegNorthSpawn.addOutgoingRoad(2, MeerssenerwegNorthDown);
        ViaductwegSpawn.addIncomingRoad(3, ViaductEastRight);
        ViaductwegSpawn.addOutgoingRoad(3, ViaductEastLeft);
        MeerssenwegSouthSpawn.addIncomingRoad(0, MeerssenerwegSouthDown);
        MeerssenwegSouthSpawn.addOutgoingRoad(0, MeerssenerwegSouthUp);
        getJunctionList().add(StatensingelSpawn);
        exitNodes.add(getJunctionList().size() - 1);
        getJunctionList().add(CapergerwegSpawn);
        exitNodes.add(getJunctionList().size() - 1);
        getJunctionList().add(WillemAlexanderwegSpawn);
        exitNodes.add(getJunctionList().size() - 1);
        getJunctionList().add(MeerssenerwegNorthSpawn);
        exitNodes.add(getJunctionList().size() - 1);
        getJunctionList().add(ViaductwegSpawn);
        exitNodes.add(getJunctionList().size() - 1);
        getJunctionList().add(MeerssenwegSouthSpawn);
        exitNodes.add(getJunctionList().size() - 1);
        getJunctionList().add(FranciscusRomanuswegSpawn);
        exitNodes.add(getJunctionList().size() - 1);
        getJunctionList().add(CaberFrontenStaten);
        getJunctionList().add(NorderwegViaduct);
        getJunctionList().add(MeerssenViaduct);
        getJunctionList().add(onRamp);
    }

    public void makeTrafficLightJunction(int noOfLanes) {
        Spawn spawnNorth = new Spawn(0, 45, 5, gridSpace, 15, 2);
        getJunctionList().add(spawnNorth);
        exitNodes.add(getJunctionList().size() - 1);
        Spawn spawnWest = new Spawn(1, 5, 45, gridSpace, 15, 2);
        getJunctionList().add(spawnWest);
        exitNodes.add(getJunctionList().size() - 1);
        Spawn spawnSouth = new Spawn(2, 45, 85, gridSpace, 10, 2);
        getJunctionList().add(spawnSouth);
        exitNodes.add(getJunctionList().size() - 1);
        Spawn spawnEast = new Spawn(3, 85, 45, gridSpace, 15, 2);
        getJunctionList().add(spawnEast);
        exitNodes.add(getJunctionList().size() - 1);
        TrafficLights trafficLights = new TrafficLights(4, 45, 45, gridSpace, new Sotl_platoonControl());
        getJunctionList().add(trafficLights);

//        // North > trafficlights
//        Road ntl = new Road(spawnNorth.getNodeNr(), trafficLights.getNodeNr(), (trafficLights.getGridY() - spawnNorth.getGridY())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(ntl);
//        spawnNorth.addOutgoingRoad(2, ntl);
//        trafficLights.addIncomingRoad(0, ntl);
//        
//        //TrafficLights > North
//        Road tln = new Road(trafficLights.getNodeNr(), spawnNorth.getNodeNr(), (trafficLights.getGridY() - spawnNorth.getGridY())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(tln);
//        trafficLights.addOutgoingRoad(0, tln);
//        spawnNorth.addIncomingRoad(2, tln);
//        
//        // South > trafficlights
//        Road stl = new Road(spawnSouth.getNodeNr(), trafficLights.getNodeNr(), (spawnSouth.getGridY() - trafficLights.getGridY())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(ntl);
//        spawnSouth.addOutgoingRoad(0, stl);
//        trafficLights.addIncomingRoad(2, stl);
//        
//        //TrafficLights > South
//        Road tls = new Road(trafficLights.getNodeNr(), spawnNorth.getNodeNr(), (spawnSouth.getGridY() - trafficLights.getGridY())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(tls);
//        trafficLights.addOutgoingRoad(2, tls);
//        spawnSouth.addIncomingRoad(0, tls);
//        
//        // West > trafficlights
//        Road wtl = new Road(spawnWest.getNodeNr(), trafficLights.getNodeNr(), (trafficLights.getGridX() - spawnWest.getGridX())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(wtl);
//        spawnWest.addOutgoingRoad(1, wtl);
//        trafficLights.addIncomingRoad(3, wtl);
//        
//        //TrafficLights > West
//        Road tlw = new Road(trafficLights.getNodeNr(), spawnWest.getNodeNr(), (trafficLights.getGridX() - spawnWest.getGridX())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(tlw);
//        trafficLights.addOutgoingRoad(3, tlw);
//        spawnWest.addIncomingRoad(1, tlw);
//        
//        // East > trafficlights
//        Road etl = new Road(spawnEast.getNodeNr(), trafficLights.getNodeNr(), (spawnEast.getGridX() - trafficLights.getGridX())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(etl);
//        spawnEast.addOutgoingRoad(3, etl);
//        trafficLights.addIncomingRoad(1, etl);
//        
//        //TrafficLights > East
//        Road tle = new Road(trafficLights.getNodeNr(), spawnEast.getNodeNr(), (spawnEast.getGridX() - trafficLights.getGridX())  * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
//        roadList.add(tle);
//        trafficLights.addOutgoingRoad(1, tle);
//        spawnEast.addIncomingRoad(3, tle);

        // North > trafficlights
        int length = Math.max(Math.abs(trafficLights.getGridX() - spawnNorth.getGridX()), Math.abs(trafficLights.getGridY() - spawnNorth.getGridY()));
        Road ntl = new Road(spawnNorth.getNodeNr(), trafficLights.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(ntl);
        spawnNorth.addOutgoingRoad(2, ntl);
        trafficLights.addIncomingRoad(0, ntl);

        //TrafficLights > North
        Road tln = new Road(trafficLights.getNodeNr(), spawnNorth.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(tln);
        trafficLights.addOutgoingRoad(0, tln);
        spawnNorth.addIncomingRoad(2, tln);

        // South > trafficlights
        length = Math.max(Math.abs(trafficLights.getGridX() - spawnSouth.getGridX()), Math.abs(trafficLights.getGridY() - spawnSouth.getGridY()));
        Road stl = new Road(spawnSouth.getNodeNr(), trafficLights.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(stl);
        spawnSouth.addOutgoingRoad(0, stl);
        trafficLights.addIncomingRoad(2, stl);

        //TrafficLights > South
        Road tls = new Road(trafficLights.getNodeNr(), spawnSouth.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(tls);
        trafficLights.addOutgoingRoad(2, tls);
        spawnSouth.addIncomingRoad(0, tls);

        // West > trafficlights
        length = Math.max(Math.abs(trafficLights.getGridX() - spawnWest.getGridX()), Math.abs(trafficLights.getGridY() - spawnWest.getGridY()));
        Road wtl = new Road(spawnWest.getNodeNr(), trafficLights.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(wtl);
        spawnWest.addOutgoingRoad(1, wtl);
        trafficLights.addIncomingRoad(3, wtl);

        //TrafficLights > West
        Road tlw = new Road(trafficLights.getNodeNr(), spawnWest.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(tlw);
        trafficLights.addOutgoingRoad(3, tlw);
        spawnWest.addIncomingRoad(1, tlw);

        // East > trafficlights
        length = Math.max(Math.abs(trafficLights.getGridX() - spawnEast.getGridX()), Math.abs(trafficLights.getGridY() - spawnEast.getGridY()));
        Road etl = new Road(spawnEast.getNodeNr(), trafficLights.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(etl);
        spawnEast.addOutgoingRoad(3, etl);
        trafficLights.addIncomingRoad(1, etl);

        //TrafficLights > East
        Road tle = new Road(trafficLights.getNodeNr(), spawnEast.getNodeNr(), length * scale, 50 / 3.6, 50 / 3.6, noOfLanes);
        roadList.add(tle);
        trafficLights.addOutgoingRoad(1, tle);
        spawnEast.addIncomingRoad(3, tle);

        setJunctionList(getJunctionList());

        System.out.println(exitNodes.size());
        System.out.println();
    }

    private void setupCarStatistics() {
        carStatistics = new boolean[]{paintTimestandingStill, paintAverageSpeed};
    }

    public void setPaintTimeStandingStill(boolean bool) {
        if (bool) {
            disableCarStatistics();
        }
        paintTimestandingStill = bool;
    }

    public void setPaintAverageSpeed(boolean bool) {
        if (bool) {
            disableCarStatistics();
        }
        paintAverageSpeed = bool;
    }

    public void setPaintSpeed(boolean bool) {
        if (bool) {
            disableCarStatistics();
        }
        paintSpeed = bool;
    }

    private void disableCarStatistics() {
        for (boolean bool : carStatistics) {
            bool = false;
        }
    }

    private void deleteSelectedJunction() {
        for (int i = 0; i < selectedJunction.getIncomingRoads().length; i++) {
            if (selectedJunction.getIncomingRoads()[i] != null) {
                int selectedIndex = selectedJunction.getIncomingRoads()[i].getStartnode();
                for (Junction junction : getJunctionList()) {
                    if (junction.getNodeNr() == selectedIndex) {
                        junction.clearOutgoingRoads(selectedJunction.getIncomingRoads()[i], selectedJunction.getNodeNr());
                    }
                }
                roadList.remove(selectedJunction.getIncomingRoads()[i]);
            }
        }
        for (int i = 0; i < selectedJunction.getOutgoingRoads().length; i++) {
            if (selectedJunction.getOutgoingRoads()[i] != null) {
                int selectedIndex = selectedJunction.getOutgoingRoads()[i].getEndnode();
                for (Junction junction : getJunctionList()) {
                    if (junction.getNodeNr() == selectedIndex) {
                        junction.clearIncomingRoads(selectedJunction.getOutgoingRoads()[i], selectedJunction.getNodeNr());
                    }
                }
                roadList.remove(selectedJunction.getOutgoingRoads()[i]);
            }
        }
        getJunctionList().remove(selectedJunction);
        drawRoad = new DrawRoad();
        selectedJunction = null;
    }

    private void setMouseGridPoint(GridPoint gridPoint) {
        mouseGridPoint.setColor(gridPointColor);
        mouseGridPoint = gridPoint;
        mouseGridPoint.setColor(gridPointHighLightColor);
    }

    private Junction junctionClicked(int x, int y) {
        for (Junction junction : getJunctionList()) {
            if (contains(new int[]{x, y}, new int[]{(junction.getGridX() + 1) * gridSpace - paintCorner[0], (junction.getGridY() + 1) * gridSpace - paintCorner[1], junction.getGridSize() * gridSpace, junction.getGridSize() * gridSpace})) {
                return junction;
            }
        }
        return null;
    }

    /*
     * Detects if a point is inside a container
     * point[0]: x of the point
     * point[1]: y of the point
     * container[0]: x of the container
     * container[1]: y of the container
     * container[2]: width of the container
     * container[3]: height of the container
     */
    private boolean contains(int[] point, int[] container) {
        return point[0] >= container[0] && point[0] <= container[0] + container[2] && point[1] >= container[1] && point[1] <= container[1] + container[3];
    }

    private void addRoad(int startJunction, int endJunction) {
        Road road = new Road(startJunction, endJunction, drawRoad.getLength() * scale, getMaxSpeedCars() / 3.6, getMaxSpeedTrucks() / 3.6, getNumberOfLanes());
        roadList.add(road);
        drawRoad.getOutgoingJunction().addOutgoingRoad(drawRoad.getIncomingDirection().ordinal(), road);
        drawRoad.getIncomingJunction().addIncomingRoad(drawRoad.getOutgoingDirection().ordinal(), road);
        drawRoad = new DrawRoad();
    }

    private int[] getNearestGridPoint(int x, int y) {
        x += (gridSpace / 2) + paintCorner[0];
        y += (gridSpace / 2) + paintCorner[1];
        int i = x - ((x / gridSpace) * gridSpace) > x - (((x / gridSpace) - 1) * gridSpace) ? x / gridSpace : x / gridSpace - 1;
        int j = y - ((y / gridSpace) * gridSpace) > y - (((y / gridSpace) - 1) * gridSpace) ? y / gridSpace : y / gridSpace - 1;
        return new int[]{i, j};
    }

    private void setupGrid() {
        for (int i = gridSpace; i <= mapWidth; i += gridSpace) {
            for (int j = gridSpace; j <= mapHeight; j += gridSpace) {
                gridPoints[i / gridSpace - 1][j / gridSpace - 1] = new GridPoint(i - 1, j - 1, 2, gridPointColor);
            }
        }
        mouseGridPoint = gridPoints[0][0];
    }

    private void setupJunctionList() {
        junctionTypeList.add(new ComboItem("Crossroads", junctionTypeList.size()));
        colorList.add(Color.BLUE);
        junctionTypeList.add(new ComboItem("Traffic-lights", junctionTypeList.size()));
        colorList.add(Color.MAGENTA);
        junctionTypeList.add(new ComboItem("Spawn", junctionTypeList.size()));
        colorList.add(Color.GREEN);
        junctionTypeList.add(new ComboItem("OnRamp", junctionTypeList.size()));
        colorList.add(Color.PINK);
        junctionTypeList.add(new ComboItem("OffRamp", junctionTypeList.size()));
        colorList.add(Color.GRAY);
        junctionTypeList.add(new ComboItem("Exit", junctionTypeList.size()));
        colorList.add(Color.RED);
    }

    private void setupCarSpeedList() {
        int[] speeds = new int[]{50, 80, 30, 100, 120, 15, 60, 70, 90, 130};
        for (int i = 0; i < speeds.length; i++) {
            carSpeedList.add(new ComboItem("" + speeds[i], speeds[i]));
        }
    }

    private void setupTruckSpeedList() {
        int[] speeds = new int[]{50, 80, 30, 100, 120, 15, 60, 70, 90, 130};
        for (int i = 0; i < speeds.length; i++) {
            truckSpeedList.add(new ComboItem("" + speeds[i], speeds[i]));
        }
    }

    private void setupLaneList() {
        int[] lanes = new int[]{1, 2, 3, 4};
        for (int i = 0; i < lanes.length; i++) {
            laneList.add(new ComboItem("" + lanes[i], lanes[i]));
        }
    }

    private int getMaxSpeedCars() {
        ComboItem speed = (ComboItem) JOptionPane.showInputDialog(
                this,
                "Max speed",
                "Max speed",
                JOptionPane.PLAIN_MESSAGE,
                null,
                carSpeedList.toArray(),
                carSpeedList.toArray()[0]);
        if (speed == null) {
            return -1;
        }
        return speed.getId();
    }

    private int getMaxSpeedTrucks() {
        ComboItem speed = (ComboItem) JOptionPane.showInputDialog(
                this,
                "Max speed",
                "Max speed",
                JOptionPane.PLAIN_MESSAGE,
                null,
                truckSpeedList.toArray(),
                truckSpeedList.toArray()[0]);
        if (speed == null) {
            return -1;
        }
        return speed.getId();
    }

    private int getNumberOfLanes() {
        ComboItem lane = (ComboItem) JOptionPane.showInputDialog(
                this,
                "Number of lanes",
                "Number of lanes",
                JOptionPane.PLAIN_MESSAGE,
                null,
                laneList.toArray(),
                laneList.toArray()[0]);
        if (lane == null) {
            return -1;
        }
        return lane.getId();
    }

    private int getJunctionType() {
        ComboItem junctionType = (ComboItem) JOptionPane.showInputDialog(
                this,
                "Junction Type",
                "Junction Type",
                JOptionPane.PLAIN_MESSAGE,
                null,
                junctionTypeList.toArray(),
                junctionTypeList.toArray()[0]);
        if (junctionType == null) {
            return -1;
        }
        return junctionType.getId();
    }

    /**
     * paints the map with all junctions and roads
     */
    public final void paintMap() {
        int verLanes1 = 0;
        int verLanes2 = 0;
        int horLanes1 = 0;
        int horLanes2 = 0;

        Graphics2D g2 = (Graphics2D) mapImage.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, mapWidth, mapHeight);
        g2.setColor(Color.black);
        g2.drawRect(1, 1, this.mapWidth - 20, mapHeight - 20);

        //Drawing le roads
        for (Junction j : getJunctionList()) {
//            if (j.getType() == JunctionType.CrossRoads || j.getType() == JunctionType.TrafficLights) {
            paintRoadsOfJunction(j, g2);
//            }
        }

        //Drawing le junctions        
        for (Junction j : getJunctionList()) {
            // Do not remove traffic lights here!
            if (j.getType() == JunctionType.CrossRoads || j.getType() == JunctionType.TrafficLights || j.getType() == JunctionType.Spawn) {
                for (int i = 0; i < j.getIncomingRoads().length; i++) {
                    if (j.getIncomingRoads()[i] != null) {
                        if (i == 0 || i == 2) {
                            if (j.getIncomingRoads()[i] != null) {
                                if (verLanes1 < j.getIncomingRoads()[i].getLanes().size()) {
                                    verLanes1 = j.getIncomingRoads()[i].getLanes().size();
                                }
                            }
                            if (j.getOutgoingRoads()[i] != null) {
                                if (verLanes2 < j.getOutgoingRoads()[i].getLanes().size()) {
                                    verLanes2 = j.getOutgoingRoads()[i].getLanes().size();
                                }
                            }
                        } else {
                            if (j.getIncomingRoads()[i] != null) {
                                if (horLanes1 < j.getIncomingRoads()[i].getLanes().size()) {
                                    horLanes1 = j.getIncomingRoads()[i].getLanes().size();
                                }
                            }
                            if (j.getOutgoingRoads()[i] != null) {
                                if (horLanes2 < j.getOutgoingRoads()[i].getLanes().size()) {
                                    horLanes2 = j.getOutgoingRoads()[i].getLanes().size();
                                }
                            }
                        }
                    }
                }
                ArrayList<Direction> dir = new ArrayList<Direction>();
                boolean corner = false;
                int directions = 4;
                for (int i = 0; i < j.getIncomingRoads().length; i++) {
                    if (j.getIncomingRoads()[i] == null && j.getOutgoingRoads()[i] == null) {
                        corner = true;
                        directions--;
                    }
                }
                for (int i = 0; i < j.getIncomingRoads().length; i++) {
                    if (j.getIncomingRoads()[i] != null || j.getOutgoingRoads()[i] != null) {
                        if (i == 0) {
                            dir.add(Direction.NORTH);
                        } else if (i == 1) {
                            dir.add(Direction.WEST);
                        } else if (i == 2) {
                            dir.add(Direction.SOUTH);
                        } else if (i == 3) {
                            dir.add(Direction.EAST);
                        }
                    }
                }
                if (directions == 2) {
                    Direction help;
                    if (dir.get(0) == Direction.NORTH || dir.get(0) == Direction.SOUTH) {
                        help = dir.get(0);
                        dir.add(0, dir.get(1));
                        dir.add(1, help);
                    }

                    paintCorner(g2, dir.get(0), dir.get(1), j.getGridX() * gridSpace, j.getGridY() * gridSpace, horLanes1, horLanes2);
                } else if (directions == 4) {
                    paintCrossRoad(g2, verLanes1, verLanes2, horLanes1, horLanes2, j.getGridX() * gridSpace, j.getGridY() * gridSpace);
                } else if (directions == 3) {
                    int missing = -1;
                    for (int i = 0; i < j.getIncomingRoads().length; i++) {
                        if (j.getIncomingRoads()[i] == null) {
                            missing = i;
                            break;
                        }
                    }
                    if (!(j.getIncomingRoads()[0] == null) && !(j.getOutgoingRoads()[0] == null)) {
                        verLanes1 = j.getIncomingRoads()[0].getLanes().size();
                        verLanes2 = j.getOutgoingRoads()[0].getLanes().size();
                    } else if (!(j.getIncomingRoads()[2] == null) && !(j.getOutgoingRoads()[2] == null)) {
                        verLanes1 = j.getIncomingRoads()[2].getLanes().size();
                        verLanes2 = j.getOutgoingRoads()[2].getLanes().size();
                    }
                    if (!(j.getIncomingRoads()[1] == null) && !(j.getOutgoingRoads()[1] == null)) {
                        horLanes1 = j.getIncomingRoads()[1].getLanes().size();
                        horLanes2 = j.getOutgoingRoads()[1].getLanes().size();
                    } else if (!(j.getIncomingRoads()[3] == null) && !(j.getOutgoingRoads()[3] == null)) {
                        horLanes1 = j.getIncomingRoads()[3].getLanes().size();
                        horLanes2 = j.getOutgoingRoads()[3].getLanes().size();
                    }
                    Direction dir1, dir2, dir3;
                    dir1 = dir2 = dir3 = Direction.NORTH;
                    switch (missing) {
                        case 0:
                            dir1 = Direction.WEST;
                            dir2 = Direction.EAST;
                            dir3 = Direction.SOUTH;
                            break;
                        case 1:
                            dir1 = Direction.NORTH;
                            dir2 = Direction.SOUTH;
                            dir3 = Direction.WEST;
                            break;
                        case 2:
                            dir1 = Direction.WEST;
                            dir2 = Direction.EAST;
                            dir3 = Direction.NORTH;
                            break;
                        case 3:
                            dir1 = Direction.NORTH;
                            dir2 = Direction.SOUTH;
                            dir3 = Direction.EAST;
                            break;
                    }
                    paintTRoad(g2, verLanes1, verLanes2, horLanes1, horLanes2, j.getGridX() * gridSpace, j.getGridY() * gridSpace, dir1, dir2, dir3);
                }
            }
            /*
            if (j.getType() == JunctionType.TJunction) {
            int missing = -1;
            for (int i = 0; i < j.getIncomingRoads().length; i++) {
            if (j.getIncomingRoads()[i] == null) {
            missing = i;
            break;
            }
            }
            if (!(j.getIncomingRoads()[0] == null)) {
            verLanes1 = j.getIncomingRoads()[0].getLanes().size();
            verLanes2 = j.getOutgoingRoads()[0].getLanes().size();
            } else {
            verLanes1 = j.getIncomingRoads()[2].getLanes().size();
            verLanes2 = j.getOutgoingRoads()[2].getLanes().size();
            }
            if (!(j.getIncomingRoads()[1] == null)) {
            horLanes1 = j.getIncomingRoads()[1].getLanes().size();
            horLanes2 = j.getOutgoingRoads()[1].getLanes().size();
            } else {
            horLanes1 = j.getIncomingRoads()[3].getLanes().size();
            horLanes2 = j.getOutgoingRoads()[3].getLanes().size();
            }
            Direction dir1, dir2, dir3;
            dir1 = dir2 = dir3 = Direction.NORTH;
            switch (missing) {
            case 0:
            dir1 = Direction.WEST;
            dir2 = Direction.EAST;
            dir3 = Direction.SOUTH;
            break;
            case 1:
            dir1 = Direction.NORTH;
            dir2 = Direction.SOUTH;
            dir3 = Direction.WEST;
            break;
            case 2:
            dir1 = Direction.WEST;
            dir2 = Direction.EAST;
            dir3 = Direction.NORTH;
            break;
            case 3:
            dir1 = Direction.NORTH;
            dir2 = Direction.SOUTH;
            dir3 = Direction.EAST;
            break;
            }
            paintTRoad(g2, verLanes1, verLanes2, horLanes1, horLanes2, j.getGridX() * gridSpace, j.getGridY() * gridSpace, dir1, dir2, dir3);
            } else if (j.getType() == JunctionType.TrafficLights) {
            }*/
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapImage == null) {
            mapImage = createImage(mapWidth, mapHeight);
        }

        offscreen = createImage(width, height);
        bufferGraphics = offscreen.getGraphics();

        Graphics2D g2 = (Graphics2D) bufferGraphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.blue);
        g2.drawRect(0, 0, width - 1, height - 1);

        if (paintMode) {
            for (int i = paintCorner[0] / gridSpace; i < paintCorner[0] / gridSpace + (width / gridSpace + 1); i++) {
                for (int j = paintCorner[1] / gridSpace; j < paintCorner[1] / gridSpace + (height / gridSpace + 1); j++) {
                    g2.setColor(gridPoints[i][j].getColor());
                    g2.fillOval(gridPoints[i][j].getX() - paintCorner[0], gridPoints[i][j].getY() - paintCorner[1], gridPoints[i][j].getSize(), gridPoints[i][j].getSize());
                    if (gridPoints[i][j].getColor() == gridPointHighLightColor) {
                        g2.drawString(i + "," + j, mouseGridPoint.getX() - paintCorner[0], mouseGridPoint.getY() - paintCorner[1]);
                    }
                }
            }

            //Paint the blocks
            for (Junction j : getJunctionList()) {
                g2.setColor(colorList.get(j.getType().ordinal()));
                g2.fillRect((j.getGridX() + 1) * gridSpace - paintCorner[0], (j.getGridY() + 1) * gridSpace - paintCorner[1], gridSpace, gridSpace);
            }

            for (Junction junction : getJunctionList()) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3.0f));
                if (drawRoad.getOutgoingJunction() != null && junction.getGridX() == drawRoad.getOutgoingJunction().getGridX() && junction.getGridY() == drawRoad.getOutgoingJunction().getGridY()) {
                    g2.drawRect((junction.getGridX() + 1) * gridSpace - paintCorner[0], (junction.getGridY() + 1) * gridSpace - paintCorner[1], gridSpace, gridSpace);
                }
                if (junction.getIncomingRoads() != null) {
                    String draw = "";
                    for (int i = 0; i < junction.getIncomingRoads().length; i++) {
                        if (junction.getIncomingRoads()[i] != null) {
                            g2.setColor(Color.black);

                            switch (i) {
                                case 0:
                                    draw += "N";
                                    break;
                                case 1:
                                    draw += "E";
                                    break;
                                case 2:
                                    draw += "S";
                                    break;
                                case 3:
                                    draw += "W";
                                    break;
                            }
                        }
                        draw += " ";
                    }
                    g2.drawString(draw, (junction.getGridX() + 1) * gridSpace - paintCorner[0], (junction.getGridY() + 1) * gridSpace - paintCorner[1]);
                }
            }


        }

        if (!paintMode) {
            g2.drawImage(mapImage, 0 - paintCorner[0], 0 - paintCorner[1], this);

            for (Junction j : getJunctionList()) {
                this.paintCars(g2, j);
                if (j.getType() == JunctionType.TrafficLights) {
                    TrafficLights light = (TrafficLights) j;
                    if (!light.trafficLightsMade()) {
                        light.createTrafficLights();
                    }
                    if (j.getIncomingRoads()[0] != null) {
                        for (int i = 0; i < j.getIncomingRoads()[0].getLanes().size(); i++) {
                            if (light.getTrafficLights()[0] != null) {
                                paintTrafficLight(g2, (j.getGridX() - i) * gridSpace - paintCorner[0], j.getGridY() * gridSpace - paintCorner[1], 0, light.getTrafficLights()[0][i].getLightStatus());
                            }
                        }
                    }
                    if (j.getIncomingRoads()[1] != null) {
                        for (int i = 0; i < j.getIncomingRoads()[1].getLanes().size(); i++) {
                            if (light.getTrafficLights()[1] != null) {
                                paintTrafficLight(g2, (j.getGridX() + j.verticalLanes()) * gridSpace - paintCorner[0], (j.getGridY() - i) * gridSpace - paintCorner[1], 1, light.getTrafficLights()[1][i].getLightStatus());
                            }
                        }
                    }
                    if (j.getIncomingRoads()[2] != null) {
                        for (int i = 0; i < j.getIncomingRoads()[2].getLanes().size(); i++) {
                            if (light.getTrafficLights()[2] != null) {
                                paintTrafficLight(g2, (j.getGridX() + i + j.verticalLanes()) * gridSpace - paintCorner[0], (j.getGridY() + j.horizontalLanes()) * gridSpace - paintCorner[1], 2, light.getTrafficLights()[2][i].getLightStatus());
                            }
                        }
                    }
                    if (j.getIncomingRoads()[3] != null) {
                        for (int i = 0; i < j.getIncomingRoads()[3].getLanes().size(); i++) {
                            if (light.getTrafficLights()[3] != null) {
                                paintTrafficLight(g2, j.getGridX() * gridSpace - paintCorner[0], (j.getGridY() + i + j.horizontalLanes()) * gridSpace - paintCorner[1], 3, light.getTrafficLights()[3][i].getLightStatus());
                            }
                        }
                    }
                }
            }

        }

        g.drawImage(offscreen, 1, 1, this);
//        paintTrafficLight(g, 100, 100, Direction.SOUTH, TrafficLightStatus.ORANGE);
//        paintTrafficLight(g, 130, 130, Direction.WEST, TrafficLightStatus.RED);
//        paintTrafficLight(g, 130, 100, Direction.EAST, TrafficLightStatus.GREEN);
//        paintTrafficLight(g, 100, 130, Direction.NORTH, TrafficLightStatus.ORANGE);
    }

    private void paintRoadRealXY(Graphics g, int sX, int sY, int eX, int eY, int lanes1, int lanes2) {
        sX /= gridSpace;
        sY /= gridSpace;
        eX /= gridSpace;
        eY /= gridSpace;

        paintRoad(g, sX, sY, eX, eY, lanes1, lanes2);
    }

    /**
     * Paints a single road on the map
     * @param g The graphics object
     * @param sX The x startpoint of the road
     * @param sY The y startpoint of the road
     * @param eX The x endpoint of the road
     * @param eY The y endpoint of the road
     * @param lanes The amount of lanes of this road
     * @param laneOffset The amount of lanes this road should shift to allow drawing a road next to it on a 2 way street
     */
    private void paintRoad(Graphics g, int sX, int sY, int eX, int eY, int lanes1, int lanes2) {
        sX *= gridSpace;
        sY *= gridSpace;
        eX *= gridSpace;
        eY *= gridSpace;

        if (sX > eX) {
            int temp = sX;
            sX = eX;
            eX = temp;
        }

        if (sY > eY) {
            int temp = sY;
            sY = eY;
            eY = temp;
        }

        Graphics2D g2d = (Graphics2D) g;
        int lanes = lanes1 + lanes2;
        boolean hor = false;
        double length = 0;
        if (eX != sX) {
            hor = true;
            length = ((double) (eX - sX) / (gridSpace)) * this.scale;
        } else {
            length = ((double) (eY - sY) / (gridSpace)) * this.scale;
        }

        int rWidth = lanes * gridSpace;
        g2d.setStroke(new BasicStroke(rWidth));
        g2d.setColor(Color.darkGray);

        if (hor) {
            g2d.drawString("Length: " + length + " m", sX + (eX - sX) / 2, eY - 2);
            g2d.drawLine(sX + (rWidth / 2), sY + (rWidth / 2), eX - (rWidth / 2), eY + (rWidth / 2));
        } else {
            g2d.drawString("Length: " + length + " m", sX + rWidth + 2, sY + (eY - sY) / 2);
            g2d.drawLine(sX + (rWidth / 2), sY + (rWidth / 2), eX + (rWidth / 2), eY - (rWidth / 2));
        }

        g2d.setColor(Color.white);

        for (int i = 1; i < lanes; i++) {
            int offset = i * gridSpace;

            if (!hor) {
                if (i == lanes2) {
                    g2d.setStroke(new BasicStroke());
                } else {
                    g2d.setStroke(dashed);
                }
                g2d.drawLine(sX + offset, sY, eX + offset, eY);
            } else {
                if (i == lanes1) {
                    g2d.setStroke(new BasicStroke());
                } else {
                    g2d.setStroke(dashed);
                }
                g2d.drawLine(sX, sY + offset, eX, eY + offset);
            }
        }
        g2d.setStroke(new BasicStroke());

        int offset = 1;
        if (!hor) {
            g2d.drawLine(sX + offset, sY, eX + offset, eY);
        } else {
            g2d.drawLine(sX, sY + offset, eX, eY + offset);
        }

        offset = lanes * gridSpace - 2;
        if (!hor) {
            g2d.drawLine(sX + offset, sY, eX + offset, eY);
        } else {
            g2d.drawLine(sX, sY + offset, eX, eY + offset);
        }
    }

    /**
     * paints all incoming roads of a junction
     * @param j the junction
     * @param g2 the graphics object
     */
    public void paintRoadsOfJunction(Junction j, Graphics2D g2) {
        int[] inLanes = {0, 0, 0, 0}, outLanes = {0, 0, 0, 0};
        int horLanes = 0, verLanes = 0;
        for (int i = 0; i < j.getIncomingRoads().length; i++) {
            if (j.getOutgoingRoads()[i] != null) {
                outLanes[i] = j.getOutgoingRoads()[i].getLanes().size();
            }
            if (j.getIncomingRoads()[i] != null) {
                inLanes[i] = j.getIncomingRoads()[i].getLanes().size();
            }
            horLanes = (int) Math.max(inLanes[1] + outLanes[1], inLanes[3] + outLanes[3]);
            verLanes = (int) Math.max(inLanes[0] + outLanes[0], inLanes[2] + outLanes[2]);
        }
        for (int i = 0; i < j.getIncomingRoads().length; i++) {
            if (i == 0 && j.getIncomingRoads()[i] != null && !j.getIncomingRoads()[i].drawn()) {
                j.getIncomingRoads()[i].setDrawn(true);
                if (j.getOutgoingRoads()[i] != null) {
                    j.getOutgoingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, j.getGridX(), (int) (j.getGridY() - j.getIncomingRoads()[i].getLength() / scale), j.getGridX(), j.getGridY(), outLanes[i], inLanes[i]);
            } else if (i == 0 && j.getOutgoingRoads()[i] != null && !j.getOutgoingRoads()[i].drawn()) {
                j.getOutgoingRoads()[i].setDrawn(true);
                if (j.getIncomingRoads()[i] != null) {
                    j.getIncomingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, j.getGridX(), (int) (j.getGridY() - j.getOutgoingRoads()[i].getLength() / scale), j.getGridX(), j.getGridY(), outLanes[i], inLanes[i]);
            } else if (i == 1 && j.getIncomingRoads()[i] != null && !j.getIncomingRoads()[i].drawn()) {
                j.getIncomingRoads()[i].setDrawn(true);
                if (j.getOutgoingRoads()[i] != null) {
                    j.getOutgoingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, j.getGridX() + verLanes, j.getGridY(), (int) (j.getGridX() + j.getIncomingRoads()[i].getLength() / scale) + verLanes, j.getGridY(), inLanes[i], outLanes[i]);
            } else if (i == 1 && j.getOutgoingRoads()[i] != null && !j.getOutgoingRoads()[i].drawn()) {
                j.getOutgoingRoads()[i].setDrawn(true);
                if (j.getIncomingRoads()[i] != null) {
                    j.getIncomingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, j.getGridX() + verLanes, j.getGridY(), (int) (j.getGridX() + j.getOutgoingRoads()[i].getLength() / scale) + verLanes, j.getGridY(), inLanes[i], outLanes[i]);
            } else if (i == 2 && j.getIncomingRoads()[i] != null && !j.getIncomingRoads()[i].drawn()) {
                j.getIncomingRoads()[i].setDrawn(true);
                if (j.getOutgoingRoads()[i] != null) {
                    j.getOutgoingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, j.getGridX(), j.getGridY() + horLanes, j.getGridX(), (int) (j.getGridY() + j.getIncomingRoads()[i].getLength() / scale) + horLanes, inLanes[i], outLanes[i]);
            } else if (i == 2 && j.getOutgoingRoads()[i] != null && !j.getOutgoingRoads()[i].drawn()) {
                j.getOutgoingRoads()[i].setDrawn(true);
                if (j.getIncomingRoads()[i] != null) {
                    j.getIncomingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, j.getGridX(), j.getGridY() + horLanes, j.getGridX(), (int) (j.getGridY() + j.getOutgoingRoads()[i].getLength() / scale) + horLanes, inLanes[i], outLanes[i]);
            } else if (i == 3 && j.getIncomingRoads()[i] != null && !j.getIncomingRoads()[i].drawn()) {
                j.getIncomingRoads()[i].setDrawn(true);
                if (j.getOutgoingRoads()[i] != null) {
                    j.getOutgoingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, (int) (j.getGridX() - j.getIncomingRoads()[i].getLength() / scale), j.getGridY(), j.getGridX(), j.getGridY(), outLanes[i], inLanes[i]);
            } else if (i == 3 && j.getOutgoingRoads()[i] != null && !j.getOutgoingRoads()[i].drawn()) {
                j.getOutgoingRoads()[i].setDrawn(true);
                if (j.getIncomingRoads()[i] != null) {
                    j.getIncomingRoads()[i].setDrawn(true);
                }
                paintRoad(g2, (int) (j.getGridX() - j.getOutgoingRoads()[i].getLength() / scale), j.getGridY(), j.getGridX(), j.getGridY(), outLanes[i], inLanes[i]);
            }
        }
    }

    /*
     * x and y are the pixel coordinates of the left end point of the lane the light should be drawn
     */
    public void paintTrafficLight(Graphics g, int x, int y, int dir, TrafficLightStatus status) {
        Color darkRed = new Color(150, 35, 35);
        Color red = Color.RED;
        Color darkOrange = new Color(200, 115, 20);
        Color orange = Color.YELLOW;
        Color darkGreen = new Color(20, 100, 15);
        Color green = Color.GREEN;
        int lightSize = getOptimalLightSize(gridSpace / 2);
        int margin = ((2 * gridSpace) - (3 * lightSize)) / 4;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int[][] lights = new int[3][3];
        switch (dir) {
            case 2:
                lights[0][0] = x + ((gridSpace - lightSize) / 2);
                lights[0][1] = y + margin;
                lights[0][2] = lightSize;
                lights[1][0] = x + ((gridSpace - lightSize) / 2);
                lights[1][1] = y + lightSize + (2 * margin);
                lights[1][2] = lightSize;
                lights[2][0] = x + ((gridSpace - lightSize) / 2);
                lights[2][1] = y + (2 * lightSize) + (3 * margin);
                lights[2][2] = lightSize;
                g2d.setColor(Color.BLACK);
                g2d.fillRect(lights[0][0], y, lightSize, 2 * gridSpace);
                break;
            case 1:
                lights[0][0] = x + margin;
                lights[0][1] = y - ((2 * gridSpace - lightSize) / 2);
                lights[0][2] = lightSize;
                lights[1][0] = x + lightSize + (2 * margin);
                lights[1][1] = y - ((2 * gridSpace - lightSize) / 2);
                lights[1][2] = lightSize;
                lights[2][0] = x + (2 * lightSize) + (3 * margin);
                lights[2][1] = y - ((2 * gridSpace - lightSize) / 2);
                lights[2][2] = lightSize;
                g2d.setColor(Color.BLACK);
                g2d.fillRect(x, lights[0][1], 2 * gridSpace, lightSize);
                break;
            case 0:
                lights[0][0] = x - ((gridSpace - lightSize) / 2) - lightSize;
                lights[0][1] = y - margin - lightSize;
                lights[0][2] = lightSize;
                lights[1][0] = x - ((gridSpace - lightSize) / 2) - lightSize;
                lights[1][1] = y - (2 * lightSize) - (2 * margin);
                lights[1][2] = lightSize;
                lights[2][0] = x - ((gridSpace - lightSize) / 2) - lightSize;
                lights[2][1] = y - (3 * lightSize) - (3 * margin);
                lights[2][2] = lightSize;
                g2d.setColor(Color.BLACK);
                g2d.fillRect(lights[0][0], y - 2 * gridSpace, lightSize, 2 * gridSpace);
                break;
            case 3:
                lights[0][0] = x - margin - lightSize;
                lights[0][1] = y + ((gridSpace - lightSize) / 2);
                lights[0][2] = lightSize;
                lights[1][0] = x - (2 * lightSize) - (2 * margin);
                lights[1][1] = y + ((gridSpace - lightSize) / 2);
                lights[1][2] = lightSize;
                lights[2][0] = x - (3 * lightSize) - (3 * margin);
                lights[2][1] = y + ((gridSpace - lightSize) / 2);
                lights[2][2] = lightSize;
                g2d.setColor(Color.BLACK);
                g2d.fillRect(x - 2 * gridSpace, lights[0][1], 2 * gridSpace, lightSize);
                break;
        }

        Color[] colors = new Color[3];
        switch (status) {
            case RED:
                colors = new Color[]{red, darkOrange, darkGreen};
                break;
            case ORANGE:
                colors = new Color[]{darkRed, orange, darkGreen};
                break;
            case GREEN:
                colors = new Color[]{darkRed, darkOrange, green};
                break;
        }

        for (int i = 0; i < lights.length; i++) {
            g2d.setColor(colors[i]);
            g2d.fillOval(lights[i][0], lights[i][1], lights[i][2], lights[i][2]);
        }

    }

    public int getOptimalLightSize(int minWidth) {
        for (int size = minWidth; size < gridSpace; size++) {
            if (((gridSpace - size) % 2) == 0) {
                return size;
            }
        }
        return minWidth;
    }

    public void paintCorner(Graphics g, Direction dir1, Direction dir2, int x, int y, int lanesDir1, int lanesDir2) {
        Graphics2D g2d = (Graphics2D) g;

        boolean twoWay = twoWay = lanesDir1 > 0 && lanesDir2 > 0;
        int lanes = lanesDir1 + lanesDir2;
        int cWidth = lanes * gridSpace;

        int xr = x;
        int yr = y;

        if (dir1 == Direction.EAST) {

            if (dir2 == Direction.SOUTH) {
                g2d.setColor(Color.darkGray);
                g2d.fillRect(xr, yr, cWidth, cWidth);
                g2d.setColor(Color.white);
                g2d.setStroke(new BasicStroke());
                g2d.drawLine(xr, yr + 1, xr + cWidth - 2, yr + 1);
                g2d.drawLine(xr + cWidth - 2, yr + 1, xr + cWidth - 2, yr + cWidth);

                for (int i = 1; i < lanes; i++) {
                    if (twoWay && lanesDir2 == i) {
                        g2d.setStroke(new BasicStroke());
                    } else {
                        g2d.setStroke(dashed);
                    }

                    g2d.drawLine(xr, yr + i * gridSpace, (xr + cWidth) - gridSpace * i, (yr + gridSpace * i));
                    g2d.drawLine((xr + cWidth) - gridSpace * i, (yr + gridSpace * i), xr + cWidth - (gridSpace * i), yr + cWidth);

                }
            }
            if (dir2 == Direction.NORTH) {
                g2d.setColor(Color.darkGray);
                g2d.fillRect(xr, yr, cWidth, cWidth);
                g2d.setColor(Color.white);
                g2d.setStroke(new BasicStroke());
                g2d.drawLine(xr, yr + cWidth - 2, xr + cWidth - 2, yr + cWidth - 2);
                g2d.drawLine(xr + cWidth - 2, yr + cWidth - 2, xr + cWidth - 2, yr);

                for (int i = 1; i < lanes; i++) {
                    if (twoWay && lanesDir2 == i) {
                        g2d.setStroke(new BasicStroke());
                    } else {
                        g2d.setStroke(dashed);
                    }
                    g2d.drawLine(xr, yr + (gridSpace * i), xr + gridSpace * i, yr + gridSpace * i);
                    g2d.drawLine(xr + gridSpace * i, yr, xr + gridSpace * i, yr + gridSpace * i);
                }
            }

            if (dir2 == Direction.WEST) {
                paintRoadRealXY(g, x - (3 * gridSpace), y, x + (3 * gridSpace), y, lanesDir1, lanesDir2);
            }
        }

        if (dir1 == Direction.NORTH) {
            if (dir2 == Direction.SOUTH) {
                paintRoadRealXY(g, x, y, x, y + (3 * gridSpace), lanesDir1, lanesDir2);
            }
        }

        if (dir1 == Direction.SOUTH) {
            if (dir2 == Direction.NORTH) {
                paintRoadRealXY(g, x, y - (3 * gridSpace), x, y + (3 * gridSpace), lanesDir1, lanesDir2);
            }
        }

        if (dir1 == Direction.WEST) {

            if (dir2 == Direction.SOUTH) {
                g2d.setColor(Color.darkGray);
                g2d.fillRect(xr, yr, cWidth, cWidth);
                g2d.setColor(Color.white);
                g2d.setStroke(new BasicStroke());
                g2d.drawLine(xr, yr + 1, xr + cWidth - 2, yr + 1);
                g2d.drawLine(xr + 1, yr + cWidth, xr + 1, yr + 2);
                for (int i = 0; i < lanes; i++) {
                    if (twoWay && lanesDir1 == i) {
                        g2d.setStroke(new BasicStroke());
                    } else {
                        g2d.setStroke(dashed);
                    }
                    g2d.drawLine(xr + cWidth, yr + i * gridSpace, xr + gridSpace * i, yr + gridSpace * i);
                    g2d.drawLine(xr + gridSpace * i, yr + gridSpace * i, xr + (gridSpace * i), yr + cWidth);
                }
            }
            if (dir2 == Direction.NORTH) {
                g2d.setColor(Color.darkGray);
                g2d.fillRect(xr, yr, cWidth, cWidth);
                g2d.setColor(Color.white);
                g2d.setStroke(new BasicStroke());
                g2d.drawLine(xr + 2, yr + cWidth - 2, xr + cWidth, yr + cWidth - 2);
                g2d.drawLine(xr + 2, yr + cWidth - 2, xr + 2, yr);
                for (int i = 0; i < lanes; i++) {
                    if (twoWay && lanesDir1 == i) {
                        g2d.setStroke(new BasicStroke());
                    } else {
                        g2d.setStroke(dashed);
                    }
                    g2d.drawLine(xr + cWidth, yr + (gridSpace * i), (xr + cWidth) - gridSpace * i, (yr + gridSpace * i));
                    g2d.drawLine((xr + cWidth) - gridSpace * i, (yr + gridSpace * i), xr + cWidth - (gridSpace) * i, yr);
                }
            }

            if (dir2 == Direction.EAST) {
                paintRoadRealXY(g, x - (3 * gridSpace), y, x + (3 * gridSpace), y, lanesDir1, lanesDir2);
            }
        }
    }

    public void paintCrossRoad(Graphics g, int vertLanes1, int vertLanes2, int horLanes1, int horLanes2, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        int vertLanes = vertLanes1 + vertLanes2;
        int horLanes = horLanes1 + horLanes2;

        int rWidth = (vertLanes1 + vertLanes2) * gridSpace;
        int rHeight = (horLanes1 + horLanes2) * gridSpace;

        g2d.setColor(Color.darkGray);
        g2d.fillRect(x, y, rWidth, rHeight);
        g2d.setColor(Color.white);

        for (int i = 0; i < vertLanes; i++) {
            int offset = i * gridSpace;
            if (i == vertLanes1) {
                g2d.setStroke(new BasicStroke());
            } else {
                g2d.setStroke(dashed);
            }
            g2d.drawLine(x + offset, y, x + offset, y + rHeight);
        }

        for (int i = 0; i < horLanes; i++) {
            int offset = i * gridSpace;
            if (i == horLanes1) {
                g2d.setStroke(new BasicStroke());
            } else {
                g2d.setStroke(dashed);
            }
            g2d.drawLine(x, y + offset, x + rWidth, y + offset);
        }
    }

    public void paintTRoad(Graphics g, int vertLanes1, int vertLanes2, int horLanes1, int horLanes2, int x, int y, Direction dir1, Direction dir2, Direction dir3) {
        Graphics2D g2d = (Graphics2D) g;

        int vertLanes = vertLanes1 + vertLanes2;
        int horLanes = horLanes1 + horLanes2;

        int rWidth = (vertLanes1 + vertLanes2) * gridSpace;
        int rHeight = (horLanes1 + horLanes2) * gridSpace;

        g2d.setColor(Color.darkGray);
        g2d.fillRect(x, y, rWidth, rHeight);

        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke());

        if (dir3 == Direction.EAST) {
            g2d.drawLine(x + 1, y, x + 1, y + rHeight);
        } else if (dir3 == Direction.WEST) {
            g2d.drawLine(x + rWidth - 2, y, x + rWidth - 2, y + rHeight);
        } else if (dir3 == Direction.SOUTH) {
            g2d.drawLine(x, y + 1, x + rWidth, y + 1);
        } else if (dir3 == Direction.NORTH) {
            g2d.drawLine(x, y + rHeight - 2, x + rWidth, y + rHeight - 2);
        }

        for (int i = 1; i < vertLanes; i++) {
            int offset = i * gridSpace;
            if (i == vertLanes1) {
                g2d.setStroke(new BasicStroke());
            } else {
                g2d.setStroke(dashed);
            }

            if (dir3 == Direction.NORTH) {
                int lHeight = horLanes1 * gridSpace;
                //g2d.drawLine(x + offset, y, x + offset, y + lHeight);
            } else if (dir3 == Direction.SOUTH) {
                int lHeight = horLanes1 * gridSpace;
                //g2d.drawLine(x + offset, y + lHeight, x + offset, y + rHeight);
            } else {
                g2d.drawLine(x + offset, y, x + offset, y + rHeight);
            }
        }

        for (int i = 1; i < horLanes; i++) {
            int offset = i * gridSpace;
            if (i == horLanes1) {
                g2d.setStroke(new BasicStroke());
            } else {
                g2d.setStroke(dashed);
            }

            if (dir3 == Direction.WEST) {
                int lWidth = vertLanes1 * gridSpace;
                //g2d.drawLine(x, y + offset, x + lWidth, y + offset);
            } else if (dir3 == Direction.EAST) {
                int lWidth = vertLanes1 * gridSpace;
                //g2d.drawLine(x + lWidth, y + offset, x + rWidth, y + offset);
            } else {
                g2d.drawLine(x, y + offset, x + rWidth, y + offset);
            }
        }
    }

    public void paintCars(Graphics g, Junction j) {
        Graphics2D g2d = (Graphics2D) g;
        double scaling = gridSpace / scale;
        double cWidth = gridSpace / 2; // Don't know about this..
        double cOffset = Math.floor(cWidth / 2);
        if (cOffset == 0) {
            cOffset++;
        }
        int brakeSize = gridSpace / 4; // Don't know about this..

        int vertlanes = j.verticalLanes();
        int horlanes = j.horizontalLanes();

        Random rand = new Random();
        Road[] sRoads = j.getIncomingRoads();
        Road[] eRoads = j.getOutgoingRoads();

        int offset = 0;
        if (sRoads[0] != null) {
            offset = sRoads[0].getLanes().size() * gridSpace;
        }
        int jWidth = offset;
        if (eRoads[0] != null) {
            for (int i = 0; i < eRoads[0].getLanes().size(); i++) {
                for (Car c : eRoads[0].getLanes().get(i).getCars()) {
                    if (c.isIsGate()) {
                        continue;
                    }

                    if (offset == 0) {
                        offset = getJunctionList().get(eRoads[0].getEndnode()).getOutgoingRoads()[2].getLanes().size();
                        offset *= gridSpace;
                    }

                    int overlap = getJunctionList().get(eRoads[0].getEndnode()).horizontalLanes();
                    double x = j.getGridX() * gridSpace + (offset + i * gridSpace) - paintCorner[0];
                    double y = (j.getGridY() + overlap) * gridSpace - (c.getPosition() * scaling) - paintCorner[1];

                    double cHeight = c.getLength() * scaling;

                    g2d.setColor(c.getColor());
                    g2d.fillRect((int) Math.round(x + cOffset), (int) Math.round(y - cHeight), (int) Math.round(cWidth), (int) Math.round(cHeight));
                    g2d.drawString(c.getName(), (int) x, (int) y);
                    if (c.getAcceleration() < 0) {
                        g2d.setColor(Color.red);
                        g2d.fillRect((int) Math.round(x + brakeSize + cOffset), (int) Math.round(y - brakeSize), brakeSize, brakeSize);
                    }
                }
                jWidth += gridSpace;
            }
        }

        offset = 0;
        if (sRoads[1] != null) {
            offset = sRoads[1].getLanes().size() * gridSpace;
        }

        int jHeight = offset;
        if (eRoads[1] != null) {
            for (int i = 0; i < eRoads[1].getLanes().size(); i++) {
                for (Car c : eRoads[1].getLanes().get(i).getCars()) {
                    if (c.isIsGate()) {
                        continue;
                    }

                    int overlap = 0;
                    if (j.verticalLanes() >= 4) {
                        overlap = j.verticalLanes();
                    }
                    double x = (j.getGridX() + overlap) * gridSpace + jWidth + (c.getPosition() * scaling) - paintCorner[0];
                    double y = (j.getGridY()) * gridSpace + offset + (i * gridSpace) - paintCorner[1];
                    double cHeight = c.getLength() * scaling;

                    g2d.setColor(c.getColor());
                    g2d.fillRect((int) Math.round(x), (int) Math.round(y + cOffset), (int) Math.round(cHeight), (int) Math.round(cWidth));
                    g2d.drawString(c.getName(), (int) x, (int) y);
                    if (c.getAcceleration() < 0) {
                        g2d.setColor(Color.red);
                        g2d.fillRect((int) Math.round(x), (int) Math.round(y + brakeSize + cOffset), brakeSize, brakeSize);
                    }
                }
            }
        }

        int lanes = 0;
        if (eRoads[2] != null) {
            lanes = eRoads[2].getLanes().size();
            for (int i = 0; i < eRoads[2].getLanes().size(); i++) {
                for (Car c : eRoads[2].getLanes().get(i).getCars()) {

                    if (c.isIsGate()) {
                        continue;
                    }

                    int overlap = 0;
                    if (j.horizontalLanes() >= 4) {
                        overlap = j.horizontalLanes();
                    }
                    double x = j.getGridX() * gridSpace + (lanes + (i * gridSpace)) - paintCorner[0];
                    double y = (j.getGridY() + overlap) * gridSpace + (c.getPosition() * scaling) - paintCorner[1];
                    double cHeight = c.getLength() * scaling;

                    g2d.setColor(c.getColor());
                    g2d.fillRect((int) Math.round(x + cOffset), (int) Math.round(y), (int) Math.round(cWidth), (int) Math.round(cHeight));
                    g2d.drawString(c.getName(), (int) x, (int) y);
                    if (c.getAcceleration() < 0) {
                        g2d.setColor(Color.red);
                        g2d.fillRect((int) Math.round(x + cOffset), (int) Math.round(y + brakeSize), brakeSize, brakeSize);
                    }
                }
            }
        }
        if (eRoads[3] != null) {
            lanes = eRoads[3].getLanes().size();
            for (int i = 0; i < eRoads[3].getLanes().size(); i++) {
                for (Car c : eRoads[3].getLanes().get(i).getCars()) {
                    if (c.isIsGate()) {
                        continue;
                    }

                    int overlap = getJunctionList().get(eRoads[3].getEndnode()).verticalLanes();
                    double x = (j.getGridX() + overlap) * gridSpace - (c.getPosition() * scaling) - paintCorner[0];
                    double y = j.getGridY() * gridSpace - (lanes - (i * gridSpace)) - paintCorner[1];
                    double cHeight = c.getLength() * scaling;

                    g2d.setColor(c.getColor());
                    g2d.fillRect((int) Math.round(x - cHeight), (int) Math.round(y + cOffset + 4), (int) Math.round(cHeight), (int) Math.round(cWidth));
                    g2d.drawString(c.getName(), (int) x, (int) y);
                    if (c.getVelocity() < 0) {
                        g2d.setColor(Color.red);
                        g2d.fillRect((int) Math.round(x), (int) Math.round(y + brakeSize + cOffset), brakeSize, brakeSize);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        addMouseWheelListener(new java.awt.event.MouseWheelListener() {

            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 786, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 453, Short.MAX_VALUE));
    }// </editor-fold>                        

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
    /**
     * @return the paintMode
     */
    public boolean isPaintMode() {
        return paintMode;
    }

    /**
     * @param paintMode the paintMode to set
     */
    public void setPaintMode(boolean paintMode) {
        this.paintMode = paintMode;
        for (Junction j : getJunctionList()) {
            for (int i = 0; i < 4; i++) {
                try {
                    j.getIncomingRoads()[i].setDrawn(false);
                } catch (NullPointerException e) {
                }
                try {
                    j.getOutgoingRoads()[i].setDrawn(false);
                } catch (NullPointerException e) {
                }
            }
        }
        if (!paintMode) {
            if (mapImage != null) {
                paintMap();
                this.repaint();
            }
        }
    }

    /**
     * @return the junctionList
     */
    public static ArrayList<Junction> getJunctionList() {
        return junctionList;
    }

    /**
     * @param junctionList the junctionList to set
     */
    public void setJunctionList(ArrayList<Junction> junctionList) {
        this.junctionList = junctionList;

        exitNodes = new ArrayList<Integer>();
        for (int i = 0; i < junctionList.size(); i++) {
            if ( //junctionList.get(i).getType() == JunctionType.Exit || 
                    junctionList.get(i).getType() == JunctionType.Spawn) {
                exitNodes.add(i);
            }
        }
        this.repaint();
    }

    private void setupControlList() {
        controlList.add(new ComboItem("Marching Control", controlList.size()));
        controlList.add(new ComboItem("Sotl request", controlList.size()));
        controlList.add(new ComboItem("Sotl phase", controlList.size()));
        controlList.add(new ComboItem("Sotl platoon", controlList.size()));
    }
}
