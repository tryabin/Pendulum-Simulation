import static utils.NumericalRoutines.computeAngularAcceleration;

public class Simulation {

    private Pendulum pendulum = new Pendulum();
    private double g;
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
    public double getTimestep() {
        return timestep;
    }
    public void setTimestep(double timestep) {
        this.timestep = timestep;
    }


    public void runSimulationForOneStep() {
        runSimulationForOneStep(0);
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

        double k2y = h*(w + 0.5*k1v);
        double k2v = h * computeAngularAcceleration(m, length, angle + 0.5*k1y, torque, g);

        double k3y = h*(w + 0.5*k2v);
        double k3v = h * computeAngularAcceleration(m, length, angle + 0.5*k2y, torque, g);

        double k4y = h*(w+k3v);
        double k4v = h * computeAngularAcceleration(m, length, angle + k3y, torque, g);

        double newAngle = angle + (k1y + 2*k2y + 2*k3y + k4y) / 6;
        double newAngularVelocity = w + (k1v + 2*k2v + 2 * k3v + k4v) / 6;

        PendulumState newPendulumState = new PendulumState();
        newPendulumState.setAngle(newAngle);
        newPendulumState.setAngularVelocity(newAngularVelocity);
        return newPendulumState;
    }
}