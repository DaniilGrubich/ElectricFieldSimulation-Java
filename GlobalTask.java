import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.TimerTask;

public class GlobalTask extends TimerTask{

    BufferStrategy bs;
    int width;
    int height;
    GlobalTask(Canvas c){
        bs = c.getBufferStrategy();
        width = c.getWidth();
        height = c.getHeight();
    }
    @Override
    public void run() {
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.setColor(new Color(50, 50, 50));
        g.fillRect(0,0,width, height);

        //draw particles
        ArrayList<Particle> particles = ParticleSystem.getParticles();
        System.out.println(particles.size());
        for(int i = 0; i < particles.size(); i++){
            Particle p = particles.get(i);

            boolean positive = p.q>0;
            boolean stationary = p.stationary;

            if(positive)
                g.setColor(Color.RED);
            else
                g.setColor(Color.CYAN);

            if(!stationary)
                g.setColor(Color.YELLOW);

            for(int j = 0; j < p.historyDepth; j++)
                g.drawRect((int) p.xHistory[j], (int) p.yHistory[j], 1, 1);
        }

        //integrate position of moving particles
        ParticleSystem.integrateNonStationaryParticles();

        //remove distant particles
        ParticleSystem.clearDistantParticles(width, height);

        bs.show();
        g.dispose();

    }
}
