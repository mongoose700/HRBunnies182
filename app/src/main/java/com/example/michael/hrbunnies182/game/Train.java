package com.example.michael.hrbunnies182.game;

import android.graphics.Point;

/**
 * Created by Michael on 1/17/2016.
 */
public class Train {
    private final Point coordinates;
    private final double theta;

    public Train(int x, int y, double theta) {
        this.coordinates = new Point(x, y);
        this.theta = theta;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public double getTheta() {
        return theta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Train train = (Train) o;

        if (Double.compare(train.theta, theta) != 0) return false;
        return !(coordinates != null ? !coordinates.equals(train.coordinates) : train.coordinates != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = coordinates != null ? coordinates.hashCode() : 0;
        temp = Double.doubleToLongBits(theta);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
