import javax.print.attribute.standard.NumberUp;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Timer;

//

public class Main extends JFrame {

    public static void main(String[] args){
        new Main();
    }

    int counter = 0;
    float scale = 1;

    ArrayList<Charge> charges = new ArrayList<>();
    ArrayList<Charge> tCharges = new ArrayList<>();
//    ArrayList<Tracer> tracers = new ArrayList<>();
    ArrayList<Float> eqpotentials = new ArrayList<>();
    JLabel tChargeDataLabel = new JLabel();

    float startingQ = 1;

    boolean trace = false;
    boolean tracerPath = false;
    boolean clearDisp = false;
    boolean field = false;
    boolean voltageMap = false;

    float angleOffset = 0;

    boolean ShiftPressed = false;
    boolean ControlPressed = false;
    boolean EPressed = false;

    BufferedImage voltageImageMap;
    Map<Point, Float> voltageHashMap = new Hashtable<Point, Float>();

    JLabel log = new JLabel(" ");

    Main(){
        super("Electric Field By Daniil Grubich");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000*2/3, 1000*2/3);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(50, 50, 50));

        Canvas c = new Canvas();
        add(c, BorderLayout.CENTER);
//        log.setBackground(new Color(50, 50, 50));
        log.setForeground(Color.lightGray);
        add(log, BorderLayout.SOUTH);

        setVisible(true);



        c.addMouseWheelListener(new MouseInputAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                if(e.getWheelRotation()<0)
                    scale/=2;
                else
                    scale*=2;
                System.out.println(scale);
            }
        });


        c.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);


                Charge c = null;

                if(e.getButton() == 1)
                    c = new Charge(e.getX() - 2, e.getY() - 2, startingQ, false);
                else if(e.getButton() == 3)
                    c = new Charge(e.getX() - 2, e.getY() - 2, -startingQ, false);
                else if(e.getButton() == 2)
                    tCharges.add(new Charge(e.getX() - 2, e.getY() - 2, 1, false));

                boolean duplicate = false;
                if(e.getButton() != 2 && c != null){
                    for (Charge c2 :
                            charges) {
                        if(c2.x == c.x && c2.y == c.y) {
                            c2.q += c.q;
                            duplicate = true;
                            break;
                        }
                    }
                }

                if(!duplicate)






                if(e.getButton() == 1)
                    charges.add(new Charge(e.getX() - 2, e.getY() - 2, startingQ, false));
                else if(e.getButton() == 3)
                    charges.add(new Charge(e.getX() - 2, e.getY() - 2, -startingQ, false));
                else if(e.getButton() == 2)




                if(e.getButton() != 2) {
                    if (ShiftPressed) {

                        Point P1 = new Point((int) charges.get(charges.size() - 2).x, (int) charges.get(charges.size() - 2).y);
                        Point P2 = new Point((int) charges.get(charges.size() - 1).x, (int) charges.get(charges.size() - 1).y);
                        float plateCharge = charges.get(charges.size() - 1).q;

                        float distance = (float) Math.hypot(P2.x - P1.x, P2.y - P1.y);


                        float ux = (float) (P2.x - P1.x) / distance;
                        float uy = (float) (P2.y - P1.y) / distance;

                        float ix = P1.x;
                        float iy = P1.y;
                        for (float i = 0; i < distance; i += 1)
                            charges.add(new Charge((ix + i * ux) - 2, (iy + i * uy) - 2, plateCharge, false));



                    } else if (ControlPressed) {
                        Point P1 = new Point((int) charges.get(charges.size() - 2).x, (int) charges.get(charges.size() - 2).y);
                        Point P2 = new Point((int) charges.get(charges.size() - 1).x, (int) charges.get(charges.size() - 1).y);
                        float circleCharge = charges.get(charges.size() - 1).q;

                        float radius = (float) Math.hypot(P2.x - P1.x, P2.y - P1.y) / 2f;

                        float centerX = (float) (P1.x + P2.x) / 2f;
                        float centerY = (float) (P1.y + P2.y) / 2f;

                        for (float i = 0; i < 2. * 3.14; i += .05) {
                            int x = (int) (centerX + radius * Math.cos(i));
                            int y = (int) (centerY + radius * Math.sin(i));

                            charges.add(new Charge(x, y, circleCharge, false));
                        }

                    }

                }else{
                    if(EPressed) {
                        eqpotentials.add(voltageAt(e.getX(), e.getY()));
                        voltageHashMap = computeVoltageMap();

                    }
                }

//                if()

                if(e.getButton() != 2)
                    voltageHashMap = computeVoltageMap();


            }
        });

        c.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyCode() == KeyEvent.VK_SHIFT)
                    ShiftPressed = false;
                else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
                    ControlPressed = false;
                else if(e.getKeyCode() == KeyEvent.VK_E)
                    EPressed = false;

            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_V) {
                    voltageMap = !voltageMap;
                }else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
                    ShiftPressed = true;
                else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
                    ControlPressed = true;
                else if(e.getKeyCode() == KeyEvent.VK_E)
                    EPressed = true;
                else if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    tCharges.add(new Charge(MouseInfo.getPointerInfo().getLocation().x - 2, MouseInfo.getPointerInfo().getLocation().y - 2, 1, false));
                }else if(e.getKeyCode() == KeyEvent.VK_EQUALS){
                    for (Charge c :
                            charges) {
                        c.q *= 2f;
                    }
                    startingQ*=2f;
                    voltageHashMap = computeVoltageMap();
//                    System.out.println(charges.get(0).q);
                }else if(e.getKeyCode() == KeyEvent.VK_MINUS){
                    if(charges.get(0).q/2f != .5f){
                        for (Charge c :
                                charges) {
                            c.q/=2f;
                        }
                        startingQ/=2f;
                    }
//                    System.out.println(charges.get(0).q);
                    voltageHashMap = computeVoltageMap();

                }else if(e.getKeyCode() == KeyEvent.VK_D){
                    clearDisp = true;
                }else if(e.getKeyCode() == KeyEvent.VK_C){
                    charges.clear();
                    tCharges.clear();
                    eqpotentials.clear();
                    clearDisp = true;
                }else if(e.getKeyCode() == KeyEvent.VK_O){

                     if(!ShiftPressed) {
                         angleOffset += .1;
                         for (Charge c :
                                 charges) {
                             int chargeToPlace = (int) (c.q / Math.abs(c.q));
                             for (float i = 0; i < 2f * 3.14f; i += 2f * 3.14f / (12f)) {
                                 float ang = (i + angleOffset);
                                 int x = (int) (c.x + (4.f) * Math.cos(ang));
                                 int y = (int) (c.y + (4.f) * Math.sin(ang));

                                 tCharges.add(new Charge(x, y, chargeToPlace, true));


                             }


                         }
                     }
//                     else{
//                         tracerPath = true;
//                         angleOffset += .1;
//                         for (Charge c :
//                                 charges) {
//                             int chargeToPlace = (int) (c.q / Math.abs(c.q));
//                             for (float i = 0; i < 2f * 3.14f; i += 2f * 3.14f / (12f)) {
//                                 float ang = (i + angleOffset);
//                                 int x = (int) (c.x + (4.f) * Math.cos(ang));
//                                 int y = (int) (c.y + (4.f) * Math.sin(ang));
//
//                                 tracers.add(new Tracer(x, y));
//
//
//                             }
//
//
//                         }
//                     }



                }else if(e.getKeyCode() == KeyEvent.VK_F)
                    field = !field;
                else if(e.getKeyCode() == KeyEvent.VK_T)
                    trace = !trace;
            }
        });



        c.createBufferStrategy(3);
        BufferStrategy bs = c.getBufferStrategy();
        voltageImageMap = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Graphics graphics = bs.getDrawGraphics();
                counter++;

                //clear display
                if(clearDisp) {
                    clearDisp = false;
                    voltageMap = false;
                    graphics.setColor(new Color(50, 50, 50));
                    graphics.fillRect(0,0,getWidth(), getHeight());

                }else
                    graphics.setColor(new Color(50, 50, 50, 20));

                if(!trace)
                    graphics.fillRect(0,0,getWidth(), getHeight());

                if(voltageMap){
                    graphics.setColor(new Color(50, 50, 50));
                    graphics.fillRect(0,0,getWidth(), getHeight());
                    graphics.drawImage(voltageImageMap, 0,0, null);
                }





                //apply force and calculate position
                try {
                    for (Charge tc :
                            tCharges) {
                        if (tc != null) {
                            float Fx = 0;
                            float Fy = 0;

                            for (Charge c :
                                    charges) {

                                float r = (float) Math.hypot(tc.x - c.x, tc.y - c.y);
                                float F;
                                if (r <= 2) {
                                    F = 0;
                                    tc.needsToBeRemoved = true;
                                }else
                                    F = tc.q * c.q / r / r;

                                Fx += F * (tc.x - c.x) / r;
                                Fy += F * (tc.y - c.y) / r;

                            }


                            tc.applyForce(Fx, Fy);


                        }
                    }
                }catch (ConcurrentModificationException ignore){}


//                if(tracerPath){
//                    tracerPath = false;
////                    voltageHashMap = computeVoltageMap();
//
//                    graphics.setColor(Color.CYAN);
//                    for (int i = 0; i < 100000; i++) {
//                        System.out.println(i);
//                        System.out.println(100000);
//                        try {
//                            for (Tracer t :
//                                    tracers) {
//                                try {
//                                    float x =  t.x;
//                                    float y =  t.y;
//                                    float v = voltageAt(x, y);
//                                    float vfx = voltageAt(x+.00001f, y);
//                                    float vfy = voltageAt(x, y+.00001f);
//
//                                    float Ex = -(vfx - v)/.00001f;
//                                    float Ey = -(vfy - v)/.00001f;
//
//                                    float ux = (float) (Ex / Math.hypot(Ex, Ey));
//                                    float uy = (float) (Ey / Math.hypot(Ex, Ey));
//
//                                    t.x = (x + ux*5);
//                                    t.y = (y + uy*5);
//
////                                    if (Math.abs(ux) <= 1 && Math.abs(uy) <= 1)
//                                        graphics.drawLine((int)x, (int)y, (int)x+1, (int)y);
//                                }catch (NullPointerException ignore){}
//                            }
//                        }catch (ConcurrentModificationException ignore){}
//                    }
//
//
//
//
//                }

              if(field) {

                    for (float x = 0; x < getWidth(); x += 30) {
                        for (float y = 0; y < getHeight(); y += 30) {
                            float sumX = 0;
                            float sumY = 0;
                            for (Charge c :
                                    charges) {
                                float r = (float) Math.hypot(x - c.x, y - c.y);
                                if (r == 0)
                                    r = .00001f;
                                float F = c.q  / r / r;

                                sumX += F * (x - c.x) / r;
                                sumY += F * (y - c.y) / r;
                            }

                            float hyp = (float) Math.hypot(sumX, sumY);
                            float ux = sumX/hyp;
                            float uy = sumY/hyp;



                            graphics.setColor(Color.BLACK);
                            graphics.drawLine((int)x, (int)y, (int)(x+ux*10), (int)(y+uy*10));
                        }
                    }
                }


                //display test charges and delete if outside the bounds
                for (int i = 0; i < tCharges.size(); i++) {

                    Charge tc = tCharges.get(i);
                    if(tc!=null) {

                        if (tc.filedLineDiscover)
                            graphics.setColor(Color.BLACK);
                        else
                            graphics.setColor(Color.GREEN);

                        int x = (int) (tc.x+2);
                        int y = (int) (tc.y+2);


                        graphics.drawLine(x, y, x+1, y+1);

                        if(counter%120 == 0 && tc.filedLineDiscover) {
                            float angle = (float) Math.atan2(tc.uy, tc.ux);
                            if (tc.q > 0)
                                angle += 3.14;

                            float arrowSideLength = 3;
                            graphics.setColor(Color.BLACK);
                            graphics.drawLine(x, y, (int) (x + arrowSideLength * Math.cos(angle + 3.14 / 6.)), (int) (y + arrowSideLength * Math.sin(angle + 3.14 / 6.)));
                            graphics.drawLine(x, y, (int) (x + arrowSideLength * Math.cos(angle - 3.14 / 6.)), (int) (y + arrowSideLength * Math.sin(angle - 3.14 / 6.)));
                        }
//                        else if(!tc.filedLineDiscover){
//                            graphics.drawString( ("Vp = " + voltageAt(tc.x+2, tc.y+2)), x, y+2);
//                        }
                        if (tc.x > getWidth() + 4 || tc.x < -4 || tc.y > getHeight() + 4 || tc.y < -4 || tc.needsToBeRemoved)
                            tCharges.remove(i--);
                    }

                }


                //display source charges
                try {
                    for (Charge c :
                            charges) {
                        if (c.q > 0)
                            graphics.setColor(Color.RED);
                        else
                            graphics.setColor(Color.CYAN);

                        graphics.fillRect((int) c.x, (int) c.y, 4, 4);
                        graphics.drawString("q = " + c.q, (int)c.x, (int)c.y+10);
                    }
                }catch (ConcurrentModificationException ignore){}







                bs.show();
                graphics.dispose();

            }
        }, 1000/60, 1000/60);





    }

    float voltageAt(float x, float y){
        float v= 0;
        try {
            for (Charge c :
                    charges) {
                int rad = (int) Math.hypot(c.x - x, c.y - y);
                if (rad != 0)
                    v += c.q / (float) rad;
            }
        } catch (ConcurrentModificationException ignore) {}

        return v;
    }
   Hashtable<Point, Float> computeVoltageMap(){
        Hashtable<Point, Float> ht = new Hashtable<>();
        float r = 1, g, b, a, vfrac;
       for (int y = 0; y < getHeight(); y++) {
           log.setText("Calculating Voltage Map: " + (int)((float) y / (float) getHeight()*100f) + "%");
           for (int x = 0; x < getWidth(); x++) {
               float v= voltageAt(x, y);


//               ht.put(new Point(x, y), v);

               vfrac = Math.abs(v / 12f);
               a = vfrac - (int)vfrac;

//               if( == 0 ){
//                   r = 0;
//                   g = 0;
//                   b = 0;
//                   a = 1;
//
//               }else
                   if (v < 0) {
                   r = 1;
                   b = 1;
                   g = 0;
               } else if (v > 0){
                   r = 1;
                   g = 1;
                   b = 0;
               }else{
                   r = 1;
                   g = 0;
                   b = 0;
               }

                   Color c;

                   boolean onEQP = false;
               for (float f :
                       eqpotentials) {
                   if (Math.abs(f - v) < .01) {
                       onEQP = true;
                       break;
                   }

               }

                   if(onEQP)
                       c = Color.MAGENTA;
                   else
                       c = new Color(r, g, b, a);

                    voltageImageMap.setRGB(x, y, c.getRGB());
//                    voltageImageMap.


               ht.put(new Point(x, y), v);
           }
       }

       return ht;
   }




    static class Tracer{
        float x, y;
        float ux, uy;
        Tracer(float x, float y){
            this.x = x;
            this.y = y;

        }
    }


    static class Charge {
        float q;

        float x = 0;
        float y = 0;

        private float Vx = 0;
        private float Vy = 0;

        float ux = 0;
        float uy = 0;


        boolean needsToBeRemoved = false;
        private boolean filedLineDiscover = false;

        Charge(float x, float y, float q, boolean discover){
            this.x = x;
            this.y = y;

            this.q = q;
            filedLineDiscover = discover;
        }

        void applyForce(float Fx, float Fy){
            ux = (float) (Fx/Math.hypot(Fx, Fy));
            uy = (float) (Fy/Math.hypot(Fx, Fy));
            if(filedLineDiscover)
                applyVelocity(Fx, Fy);
            else{
                Vx += Fx;
                Vy += Fy;

                x += Vx;
                y += Vy;
            }
        }

        void applyVelocity(float x, float y){
            float V = 2;



            this.x+=V*ux;
            this.y+=V*uy;
        }


    }
}
