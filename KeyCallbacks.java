/*
 * Author:  Daniil Pavlovich Grubich
 * Purpose: Handles key presses.
 *          The user can press the 'v' key to toggle the voltage map and the 'f' key to toggle
 *          the field lines. The 'd' key clears the voltage map and the 'c' key clears all charges.
 *          The 't' key creates a grid of tracers. The '=' and '-' keys scale the charges. The space bar
 *          creates a test charge at the mouse's location. The escape key exits the program. 
 *      
 *
 * 
 */
import java.awt.MouseInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyCallbacks extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) 
            System.exit(0);
        else if(e.getKeyCode() == KeyEvent.VK_SPACE)
            Main.charges.add(new Charge(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y, 1, true));
        else if(e.getKeyCode() == KeyEvent.VK_EQUALS)
            scaleCharges(2f);
        else if(e.getKeyCode() == KeyEvent.VK_MINUS)
            scaleCharges(.5f);
        else if(e.getKeyCode() == KeyEvent.VK_V)
            Main.showVoltageMap = !Main.showVoltageMap;
        else if(e.getKeyCode() == KeyEvent.VK_F)
            Main.field = !Main.field;
        else if(e.getKeyCode() == KeyEvent.VK_D)
            clearDisplay();
        else if(e.getKeyCode() == KeyEvent.VK_C)
            clearAll();
        else if(e.getKeyCode() == KeyEvent.VK_T)
            createTracers(10);


    }

    public void scaleCharges(float factor){
        for (Charge c :
                Main.charges) {
            c.q *= factor;
        }
    }

    public void clearDisplay(){
        for(int i = 0; i < Main.voltageImageMap.getWidth(); i++)
            for(int j = 0; j < Main.voltageImageMap.getHeight(); j++)
                Main.voltageImageMap.setRGB(i, j, 0x00000000);

    }

    public void clearAll(){
        clearDisplay();
        Main.charges.clear();
        Main.tracers.clear();
    }

    public void createTracers(int spacing){
        for (float x = 0; x < Main.voltageImageMap.getWidth(); x += spacing) {
            for (float y = 0; y < Main.voltageImageMap.getHeight(); y += spacing) {
                Main.tracers.add(new Tracer(x, y));
            }
        }
    }
}
