package utils;

import static java.lang.Math.*;


public class NumericalRoutines {

    public static double computeAngularAcceleration(double mass, double length, double angle, double torque, double g) {
        double I = mass * pow(length, 2);
        double a = (torque - mass*g*length*sin(angle)) / I;
        return a;
    }

    public static double getAngularDistanceBetweenAngles(double x, double y) {
        double a = (x - y) % (2 * PI);
        double b = (y - x) % (2 * PI);
        return a < b ? abs(a) : abs(b);
    }
}
