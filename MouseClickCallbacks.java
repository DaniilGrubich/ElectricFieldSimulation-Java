/*
 *  Author: Daniil Pavlovich Grubich
 *  Purpose: Handles mouse clicks. 
 *           The user can add a positive charge by left clicking, a negative charge by right clicking,
 *           and a tester charge by middle clicking.
 * 
 */

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

