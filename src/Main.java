import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import static java.lang.Math.PI;

public class Main {

    public static void main(String[] args) {

        double optimalControlTimeInterval = 5;
        int numberOfSteps = 300;
        double timestep = optimalControlTimeInterval / (double)numberOfSteps;
        double g = 9.81;
        double maxTorque = 5;
        double mass = 1;
        double length = 1;
        double angle = PI/2;
        double angularVelocity = 0;
        double goalAngle = PI;

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

//        double[] control = new double[numberOfSteps];
//        Arrays.fill(control, 0);
//        control[0] = 0;
//        double fitnessOfControl = simulation.computeFitnessOfControl(control, goalAngle);
//        System.out.println("control fitness = " + fitnessOfControl);

//      Run the simulation.
        JFrame frame = new JFrame();
        Container container = frame.getContentPane();
        MyCanvas canvas = new MyCanvas(simulation, pixelsPerMeter, FPS);
        container.add(canvas);
        frame.setSize(frameWidth, frameHeight);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screenSize.getWidth() - frameWidth)/2, (int)(screenSize.getHeight() - frameHeight) / 2);
        frame.setVisible(true);
        container.setBackground(Color.BLACK);
        canvas.startAnimation();
    }
}


class MyCanvas extends JComponent implements Runnable {
    private long delayNanoSeconds;
    private Thread animatorThread;
    private Simulation simulation;
    private double pixelsPerMeter;

    MyCanvas(Simulation simulation, double pixelsPerMeter, int fps) {
        this.simulation = simulation;
        this.pixelsPerMeter = pixelsPerMeter;
        this.delayNanoSeconds = 1000*1000000 / fps;
    }

    public void startAnimation() {
        if (animatorThread == null) {
            animatorThread = new Thread(this);
        }
        animatorThread.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double lineEndX = simulation.getPendulum().getPointX()*pixelsPerMeter + (double)this.getWidth()/2;
        double lineEndY =  (double)this.getHeight()/2 - simulation.getPendulum().getPointY()*pixelsPerMeter;
        Line2D.Double line = new Line2D.Double((double)this.getWidth()/2, (double)this.getHeight()/2, lineEndX, lineEndY);
        BasicStroke stroke = new BasicStroke(5);
        g2.setStroke(stroke);
        g2.setColor(Color.WHITE);
        g2.draw(line);

        double circleRadius = 20;
        Ellipse2D.Double circle = new Ellipse2D.Double(lineEndX - circleRadius, lineEndY - circleRadius, 2*circleRadius, 2*circleRadius);
        g2.setColor(new Color(0, 128, 255));
        g2.fill(circle);
    }

    public void run() {

        //Remember the starting time
        long computationStartTime = System.nanoTime();

        //This is the animation loop.
        while (Thread.currentThread() == animatorThread) {

            // Advance the simulation one step.
            simulation.runSimulationForOneStep(0);

            //Display it.
            repaint();

            //Delay depending on how far we are behind.
            long computationTime = System.nanoTime() - computationStartTime;
            long curDelayNanoSeconds = delayNanoSeconds - computationTime;
            long sleepStartTime = System.nanoTime();
            while(System.nanoTime() - sleepStartTime < curDelayNanoSeconds); // Busy wait
            computationStartTime = System.nanoTime();
        }
    }
}
