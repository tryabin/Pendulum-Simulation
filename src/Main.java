import javax.swing.*;
import java.awt.*;

import static java.lang.Math.PI;

public class Main {

    public static void main(String[] args) {

        double timestep = 1/60f;
        double g = 9.81;
        double maxTorque = 5;
        double mass = 1;
        double length = 1;
        double angle = PI/3;
        double angularVelocity = 0;

        int frameWidth = 600;
        int frameHeight = 600;
        int FPS = 60;
        double pixelsPerMeter = 100;

        Simulation simulation = new Simulation();
        simulation.getPendulum().setMass(mass);
        simulation.getPendulum().setLength(length);
        simulation.getPendulum().getState().setAngle(angle);
        simulation.getPendulum().getState().setAngularVelocity(angularVelocity);
        simulation.setG(g);
        simulation.setMaxTorque(maxTorque);
        simulation.setTimestep(timestep);

//      Run the simulation.
        JFrame frame = new JFrame();
        Container container = frame.getContentPane();
        Rendering canvas = new Rendering(simulation, pixelsPerMeter, FPS);
        container.add(canvas);
        frame.setSize(frameWidth, frameHeight);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screenSize.getWidth() - frameWidth)/2, (int)(screenSize.getHeight() - frameHeight) / 2);
        frame.setVisible(true);
        container.setBackground(Color.BLACK);
        canvas.startAnimation();
    }
}


