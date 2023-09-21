import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Timer;

import javax.swing.JFrame;



public class Main extends JFrame {

    static ArrayList<Charge> charges = new ArrayList<>();
    static ArrayList<Tracer> tracers = new ArrayList<>();
    static BufferedImage voltageImageMap;

    static boolean showVoltageMap = false;
    static boolean field = false;

    static BufferStrategy bs;
    static Graphics2D g;
    static Canvas c;

    public static void main(String[] args) {
        new Main();
    }

    Main() {
        super("Electric Field By Daniil Pavlovich Grubich");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);

        c = new Canvas(); add(c); setVisible(true);

        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();
        voltageImageMap = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        c.addMouseListener(new MouseClickCallbacks());
        c.addKeyListener(new KeyCallbacks());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                g = (Graphics2D) bs.getDrawGraphics();
                g.setColor(new Color(40,40,40, 100));
                g.fillRect(0, 0, getWidth(), getHeight());


                if(showVoltageMap){
                    computeVoltageMap(0, 1);
                    g.drawImage(voltageImageMap, 0, 0, null);
                }
                
                    
                if (field)
                    drawVectors(10);
                
                
                applyForceToParticles();
                applyForceToTracers();

                cleanCharges();
                cleanTracers();

                drawPaticles();
                drawTracers();

                bs.show();
                g.dispose();

            }
        }, 1000 / 60, 1000 / 60);

    }

    float voltageAt(float x, float y) {
        float v = 0;
        try {
            for (Charge c : charges) {
                if(c.isTest)
                    continue;
                int rad = (int) Math.hypot(c.x - x, c.y - y);
                if (rad != 0)
                    v += c.q / (float) rad;
            }
        } catch (ConcurrentModificationException ignore) {
        }

        return v;
    }

    void applyForceToParticles(){
        try {
            for (Charge tc : charges) {
                if(tc.isTest){
                    float Fx = 0;
                    float Fy = 0;

                    for (Charge c : charges) {

                        if(!c.isTest){
                            float r = (float) Math.hypot(tc.x - c.x, tc.y - c.y);
                            float F;
                            if (r <= 2) {
                                F = 0;
                                tc.needsToBeRemoved = true;
                            } else
                                F = tc.q * c.q / r / r;

                            Fx += F * (tc.x - c.x) / r;
                            Fy += F * (tc.y - c.y) / r;
                        }
                    }

                    tc.applyForce(Fx, Fy);
                }
            }
        } catch (Exception ignore) {
        }
}

    void applyForceToTracers(){
        try {
            for (Tracer t : tracers) {
                    float Fx = 0;
                    float Fy = 0;

                    for (Charge c : charges) {
                        if(c.isTest)
                            continue;

                        float r = (float) Math.hypot(t.x - c.x, t.y - c.y);
                        float F;
                        if (r <= 2) {
                            F = 0;
                            t.needsToBeRemoved = true;
                        } else
                            F = t.q * c.q / r / r;

                        Fx += F * (t.x - c.x) / r;
                        Fy += F * (t.y - c.y) / r;
                        
                    }

                    t.applyForce(Fx, Fy);
                
            }
        } catch (Exception ignore) {
        }   
    }

    void drawPaticles(){
        try {
            for (Charge c : charges) {
                if(c.isTest)
                    g.setColor(Color.GREEN);
                else if (c.q > 0){
                    g.setColor(Color.RED);
                    g.drawString("q = " + c.q, (int) c.x, (int) c.y + 10);
                }else{
                    g.setColor(Color.CYAN);
                    g.drawString("q = " + c.q, (int) c.x, (int) c.y + 10);
                }

                g.drawLine((int) c.x, (int) c.y, (int) c.x + 1, (int) c.y);
            }
        } catch (ConcurrentModificationException ignore) {}
    }

    void computeVoltageMap(float heightOffsetFrac, float heightLengthFrac) {
        float r = 1, g, b, a;
        for (int y = (int) (getHeight()*heightOffsetFrac); y < (getHeight()*heightOffsetFrac) + (getHeight()*heightLengthFrac); y++) {
            for (int x = 0; x < getWidth(); x++) {
                float v = voltageAt(x, y);


                float vfrac = Math.abs(v / 12f);
                a = vfrac - (int) vfrac;

                if (v < 0) {
                    r = 1;
                    b = 1;
                    g = 0;
                } else if (v > 0) {
                    r = 1;
                    g = 1;
                    b = 0;
                } else {
                    r = 1;
                    g = 0;
                    b = 0;
                }

                Color c = new Color(r, g, b, a);
                voltageImageMap.setRGB(x, y, c.getRGB());

            }
        }
    }

    void updateVoltageMap(){
        Thread t = new Thread(() -> {
            computeVoltageMap(0, 1);
            
        });

        t.start();
    }

    void cleanCharges(){
        for (int i = 0; i < charges.size(); i++) {

            Charge charge = charges.get(i);
            if (charge != null && charge.isTest) {
                if (charge.x > getWidth() + 4 || charge.x < -4 || charge.y > getHeight() + 4 || charge.y < -4 || charge.needsToBeRemoved)
                    charges.remove(i--);
            }

        }
    }

    void cleanTracers(){
        for (int i = 0; i < tracers.size(); i++) {

            Tracer tracer = tracers.get(i);
            if (tracer != null) {
                if (tracer.x > getWidth() + 4 || tracer.x < -4 || tracer.y > getHeight() + 4 || tracer.y < -4 || tracer.needsToBeRemoved)
                    tracers.remove(i--);
            }

        }
    }

    void drawVectors(int spacing){
        for (float x = 0; x < getWidth(); x += spacing) {
            for (float y = 0; y < getHeight(); y += spacing) {
                float sumX = 0;
                float sumY = 0;
                try{
                    for (Charge c : charges) {
                        if(c.isTest)
                            continue;

                        float r = (float) Math.hypot(x - c.x, y - c.y);
                        if (r == 0)
                            r = .00001f;
                        float F = c.q / r / r;

                        sumX += F * (x - c.x) / r;
                        sumY += F * (y - c.y) / r;
                    }
                }catch(ConcurrentModificationException ignore){}

                float hyp = (float) Math.hypot(sumX, sumY);
                float ux = sumX / hyp;
                float uy = sumY / hyp;

                g.setColor(Color.BLACK);
                g.drawLine((int) x, (int) y, (int) (x + ux * 10), (int) (y + uy * 10));
            }
        }
    }

    void drawTracers(){
        try {
            for (Tracer t : tracers) {
                g.setColor(Color.PINK);
                g.drawLine((int) t.x, (int) t.y, (int) t.x + 1, (int) t.y);
            }
        } catch (Exception ignore) {}
    }
}
