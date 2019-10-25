import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Pendulum {

    private double mass;
    private double length;
    private PendulumState state = new PendulumState();

    public double getMass() {
        return mass;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    public PendulumState getState() {
        return state;
    }
    public void setState(PendulumState state) {
        this.state = state;
    }

    public double getPointX() {
        return sin(state.getAngle()) * length;
    }

    public double getPointY() {
        return -cos(state.getAngle()) * length;
    }
}