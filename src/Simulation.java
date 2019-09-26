import static java.lang.Math.*;
import static utils.NumericalRoutines.*;

public class Simulation {

    private Pendulum pendulum = new Pendulum();
    private double g;
    private double maxTorque;
    private double timestep;

    public Pendulum getPendulum() {
        return pendulum;
    }
    public void setPendulum(Pendulum pendulum) {
        this.pendulum = pendulum;
    }
    public double getG() {
        return g;
    }
    public void setG(double g) {
        this.g = g;
    }
    public double getMaxTorque() {
        return maxTorque;
    }
    public void setMaxTorque(double maxTorque) {
        this.maxTorque = maxTorque;
    }
    public double getTimestep() {
        return timestep;
    }
    public void setTimestep(double timestep) {
        this.timestep = timestep;
    }


    public void runSimulationForOneStep(double torque) {
        PendulumState newPendulumState = computeOneSimulationStep(torque);
        pendulum.setState(newPendulumState);
    }


    public PendulumState computeOneSimulationStep(double torque) {
        double h = timestep;
        double w = pendulum.getState().getAngularVelocity();
        double m = pendulum.getMass();
        double length = pendulum.getLength();
        double angle = pendulum.getState().getAngle();

        double k1y = h*w;
        double k1v = h * computeAngularAcceleration(m, length, angle, torque, g);

        double k2y = h*(w+0.5*k1v);
        double k2v = h * computeAngularAcceleration(m, length, angle + 0.5 * k1y, torque, g);

        double k3y = h*(w+0.5*k2v);
        double k3v = h * computeAngularAcceleration(m, length, angle + 0.5 * k2y, torque, g);

        double k4y = h*(w+k3v);
        double k4v = h * computeAngularAcceleration(m, length, angle + k3y, torque, g);

        double newAngle = angle + (k1y + 2 * k2y + 2 * k3y + k4y) / 6.0;
        double newAngularVelocity = w + (k1v + 2 * k2v + 2 * k3v + k4v) / 6.0;

        PendulumState newPendulumState = new PendulumState();
        newPendulumState.setAngle(newAngle);
        newPendulumState.setAngularVelocity(newAngularVelocity);
        return newPendulumState;
    }


    public double computeFitnessOfControl(double[] control, double goalAngle) {

        double prevAngle = pendulum.getState().getAngle();
        double integral = 0;

        for (double torque : control) {
            PendulumState newPendulumState = computeOneSimulationStep(torque);
            double angle = newPendulumState.getAngle();
            double angularVelocity = newPendulumState.getAngularVelocity();

            // Left Riemann Sum
//            integral += getAngularDistanceBetweenAngles(prevAngle, goalAngle)*timestep;

            // Midpoint rule
            integral += (getAngularDistanceBetweenAngles(angle, goalAngle) + getAngularDistanceBetweenAngles(prevAngle, goalAngle)) / 2 * timestep;

            prevAngle = angle;
        }

        return integral;
    }
}
