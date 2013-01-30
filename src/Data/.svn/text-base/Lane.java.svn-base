/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Data.Junctions.Junction;
import GUI.Direction;
import GUI.SimulationPanel;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 *
 * @author Cliff
 */
public class Lane implements Serializable {

    private LinkedList<Car> cars;
    private double length;
    private double density;
    private double extraStartLength, extraEndLength;// The part of the road which is on the junction
    private Direction dir;
    private double averageSpeed = 0;
    private int noOfCars = 0;

    public Lane(double length) {
        this.length = length;
        extraStartLength = extraEndLength = 5;
        cars = new LinkedList();
    }

    public Lane(LinkedList<Car> cars, double length) {
        this.cars = cars;
        this.length = length;
    }

    public void calculateValue() {
        if (this.cars.size() > 0) {
            this.density = this.length / this.cars.size();
        } else {
            this.density = 0;
        }
    }

    public void calculateActualValue() {
        if (this.cars.size() > 0) {
            this.density = this.cars.size() / this.length;
        } else {
            this.density = 0;
        }
    }

    public LinkedList<Car> getCars() {
        return this.cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCar(int i, Car car) {
        cars.add(i, car);
    }

    public void deleteCar(int i) {
        cars.remove(i);
    }

    public void deleteCar(Car car) {
        cars.remove(car);
    }

    public Car getCar(int index) {
        return cars.get(index);
    }

    public void setCars(LinkedList<Car> cars) {
        this.cars = cars;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @return the density
     */
    public double getDensity() {
        return density;
    }

    /**
     * @return the dir
     */
    public Direction getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(Direction dir) {
        this.dir = dir;
    }

    /**
     * @return the extraStartLength
     */
    public double getExtraStartLength() {
        return extraStartLength;
    }

    /**
     * @param extraStartLength the extraStartLength to set
     */
    public void setExtraStartLength(double extraStartLength) {
        this.extraStartLength = extraStartLength;
    }

    /**
     * @return the extraEndLength
     */
    public double getExtraEndLength() {
        return extraEndLength;
    }

    /**
     * @param extraEndLength the extraEndLength to set
     */
    public void setExtraEndLength(double extraEndLength) {
        this.extraEndLength = extraEndLength;
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

    public void setNoOfCars(int noOfCars) {
        this.noOfCars = noOfCars;
    }

    /**
     * Calculates the avarage speed of all cars on this lane.
     * Return Integer.MINVALUE when it is empty, for the eval function of LC
     */
    public double calculateAvgSpeed() {
        if (this.cars.size() > 0) {
            double speedSummed = 0;
            for (Car car : this.cars) {
                speedSummed += car.getVelocity();
            }
            return speedSummed / this.cars.size();
        } else {
            return 1000;
        }
    }

    public static double getActualDensity() {
        double totalDensity = 0;
        int elements = 0;
        for (Junction j : SimulationPanel.getJunctionList()) {
            for (Road r : j.getIncomingRoads()) {
                if (r != null) {
                    for (Lane l : r.getLanes()) {
                        totalDensity += (l.getCars().size() / l.getLength());
                        elements++;
                    }
                }
            }
            for (Road r : j.getOutgoingRoads()) {
                if (r != null) {
                    for (Lane l : r.getLanes()) {
                        totalDensity += (l.getCars().size() / l.getLength());
                        elements++;
                    }
                }
            }
        }
        double returnValue = totalDensity / elements;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return returnValue;
    }
}
