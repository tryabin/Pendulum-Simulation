package utils;

import static java.lang.Math.*;


public class NumericalRoutines {

    public static double computeAngularAcceleration(double mass, double length, double angle, double torque, double g) {
        double I = mass * pow(length, 2);
        double acceleration = (torque - mass*g*length*sin(angle)) / I;
        return acceleration;
    }
}
