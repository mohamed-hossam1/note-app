package com.sourcepackage.Applicationpackage;

import java.io.Serializable;

public class Sketch implements Serializable {
    private double x;
    private double y;
    private double size;
    private double red;
    private double green;
    private double blue;
    private double opacity;

    public Sketch(double x, double y, double size,
                  double red, double green, double blue, double opacity) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getSize() { return size; }
    public double getRed() { return red; }
    public double getGreen() { return green; }
    public double getBlue() { return blue; }
    public double getOpacity() { return opacity; }
}
