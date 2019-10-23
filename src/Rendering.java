import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

class Rendering extends JComponent implements Runnable {
    private long delayNanoSeconds;
    private Thread animatorThread;
    private Simulation simulation;
    private double pixelsPerMeter;

    Rendering(Simulation simulation, double pixelsPerMeter, int fps) {
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
