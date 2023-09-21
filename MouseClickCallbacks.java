import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class MouseClickCallbacks extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1)
            addNonStationaryCharge(e.getX(), e.getY(), 1);
        else if(e.getButton() == 3)
            addNonStationaryCharge(e.getX(), e.getY(), -1);
        else if(e.getButton() == 2)
            Main.charges.add(new Charge(e.getX(), e.getY(), 1, true));
        
    }

    public void addNonStationaryCharge(int x, int y, int q){
        Charge c = new Charge(x, y, q, false);
        boolean isUnique = true;
        for (Charge c2 : Main.charges) {
            if(c2.equals(c)) { 
                c2.q += c.q; isUnique = false; break;
            }
        }
        if(isUnique)
            Main.charges.add(c);

    }
}




// if(!duplicate)


//             if(e.getButton() == 1)
//                 charges.add(new Charge(e.getX() - 2, e.getY() - 2, startingQ, false));
//             else if(e.getButton() == 3)
//                 charges.add(new Charge(e.getX() - 2, e.getY() - 2, -startingQ, false));
//             else if(e.getButton() == 2)




//                 if(e.getButton() != 2) {
//                     if (ShiftPressed) {

//                         Point P1 = new Point((int) charges.get(charges.size() - 2).x, (int) charges.get(charges.size() - 2).y);
//                         Point P2 = new Point((int) charges.get(charges.size() - 1).x, (int) charges.get(charges.size() - 1).y);
//                         float plateCharge = charges.get(charges.size() - 1).q;

//                         float distance = (float) Math.hypot(P2.x - P1.x, P2.y - P1.y);


//                         float ux = (float) (P2.x - P1.x) / distance;
//                         float uy = (float) (P2.y - P1.y) / distance;

//                         float ix = P1.x;
//                         float iy = P1.y;
//                         for (float i = 0; i < distance; i += 1)
//                             charges.add(new Charge((ix + i * ux) - 2, (iy + i * uy) - 2, plateCharge, false));



//                     } else if (ControlPressed) {
//                         Point P1 = new Point((int) charges.get(charges.size() - 2).x, (int) charges.get(charges.size() - 2).y);
//                         Point P2 = new Point((int) charges.get(charges.size() - 1).x, (int) charges.get(charges.size() - 1).y);
//                         float circleCharge = charges.get(charges.size() - 1).q;

//                         float radius = (float) Math.hypot(P2.x - P1.x, P2.y - P1.y) / 2f;

//                         float centerX = (float) (P1.x + P2.x) / 2f;
//                         float centerY = (float) (P1.y + P2.y) / 2f;

//                         for (float i = 0; i < 2. * 3.14; i += .05) {
//                             int x = (int) (centerX + radius * Math.cos(i));
//                             int y = (int) (centerY + radius * Math.sin(i));

//                             charges.add(new Charge(x, y, circleCharge, false));
//                         }

//                     }

//                 }else{
//                     if(EPressed) {
//                         eqpotentials.add(voltageAt(e.getX(), e.getY()));
//                         voltageHashMap = computeVoltageMap();

//                     }
//                 }

// //                if()

//         if(e.getButton() != 2)
//             voltageHashMap = computeVoltageMap();