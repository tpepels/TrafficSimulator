package Data;

import java.awt.Color;
import java.io.Serializable;
import java.util.Queue;

/**
 *
 * @author Tom
 */
public class Car implements Serializable {

    int delta = 4;
    private double position = 0;
    private double velocity = 0;
    private double length;                  // Length of the vehicle, in meters
    private Queue<Integer> path;
    private String name = "";
    private boolean isGate = false;
    private boolean truck = false;
    private Color color = Color.lightGray;
    // IDM Constants ---------------------------------------
    private double dVelocity;               // The desired velocity
    private double minSpace;                // The minimum spacing between two cars
    double dTimeHead;               // Desired time-headway between two cars
    double dAccel;                  // Desired Acceleration, in m/s^2
    double decel;                   // Comfortable braking deceleration
    private double acceleration;
    private double timeStandingStill = 0;
    private double epsilon = 7.2;
    private long lastLC = System.currentTimeMillis();

    public Car(double length, double dVelocity, double minSpace, double dTimeHead,
            double dAccel, double decel, String name, boolean truck, Queue<Integer> path) {
        this.length = length;
        this.dVelocity = dVelocity;
        this.minSpace = minSpace;
        this.dTimeHead = dTimeHead;
        this.dAccel = dAccel;
        this.decel = decel;
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }
        this.path = path;
        this.truck = truck;
        this.color = Color.getHSBColor((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    /**
     * creates a ghostcar/gate for a junction
     */
    public Car() {
        this.length = 1;
        this.dVelocity = 0;
        this.minSpace = 2;
        this.dTimeHead = 0;
        this.dAccel = 0;
        this.decel = 0;
        this.name = "GATE";
        path = null;
        isGate = true;
    }

    /**
     *
     * @param prevCar the previous car
     * @param timeInterval the desired time interval in seconds
     */
    public void drive(double trailSpeed, double trailDistance, boolean firstCar, double fraction) {
        acceleration = accelerate(trailSpeed, trailDistance, firstCar);
        velocity = velocity + acceleration * (fraction / 1000.0);
        if (velocity < 0) {
            velocity = 0;
        }
        double deltaPosition = velocity * (fraction / 1000.0);
        position = position + deltaPosition;
        if (velocity < epsilon || (acceleration < 0 && velocity < 35/3.6)) {
            
            timeStandingStill += (fraction / 1000.0);
        }

        //System.out.println("Hi, I'm: " + name + " and my position is: " + position + ", speed: " + velocity);
    }

    /**
     * 
     * @return timeStandingStill the time the car has been standing still
     */
    public double getTimeStandingStill() {
        return timeStandingStill;
    }

    public double getSpeed() {
        double speed = velocity * 3.6; // Do rounding in the gui, not here
        return speed;
    }

    /**
     * 
     */
    /**
     *
     * @param prevCar the previous car
     * @return  the new acceleration
     */
    private double accelerate(double trailSpeed, double trailDistance, boolean firstCar) {
        if (dVelocity != 0) {
            double Sa, DVa;
            if (!firstCar) {
                Sa = trailDistance;
                DVa = velocity - trailSpeed;
            } else {
                Sa = Double.MAX_VALUE;   // No cars in front..
                DVa = velocity;
            }

            double Sstar = minSpace + velocity * dTimeHead + (velocity * DVa) / (2 * Math.sqrt(dAccel * decel));
            acceleration = dAccel * (1 - Math.pow((velocity / dVelocity), delta) - Math.pow((Sstar / Sa), 2));

            return acceleration;
        }
        return 0;
    }

    /**
     * @return the position
     */
    public double getPosition() {
        return position;
    }

    /**
     * @return the velocity
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(double position) {
        this.position = position;
    }

    /**
     * @return the minSpace
     */
    public double getMinSpace() {
        return minSpace;
    }

    /**
     * @return the path
     */
    public Queue<Integer> getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(Queue<Integer> path) {
        this.path = path;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the isGate
     */
    public boolean isIsGate() {
        return isGate;
    }

    /**
     * @return the dVelocity
     */
    public double getdVelocity() {
        return dVelocity;
    }

    /**
     * @param dVelocity the dVelocity to set
     */
    public void setdVelocity(double dVelocity) {
        this.dVelocity = dVelocity;
    }

    /**
     * @return the truck
     */
    public boolean isTruck() {
        return truck;
    }

    /**
     * @param truck the truck to set
     */
    public void setTruck(boolean truck) {
        this.truck = truck;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the acceleration
     */
    public double getAcceleration() {
        return acceleration;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lastLC
     */
    public long getLastLC() {
        return lastLC;
    }

    /**
     * @param lastLC the lastLC to set
     */
    public void setLastLC(long lastLC) {
        this.lastLC = lastLC;
    }
}
