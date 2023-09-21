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

//     @Override
//     public void keyPressed(KeyEvent e) {
//         if(e.getKeyCode() == KeyEvent.VK_V) {
//             voltageMap = !voltageMap;
//         }else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
//             ShiftPressed = true;
//         else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
//             ControlPressed = true;
//         else if(e.getKeyCode() == KeyEvent.VK_E)
//             EPressed = true;
//         else if(e.getKeyCode() == KeyEvent.VK_SPACE){
//             tCharges.add(new Charge(MouseInfo.getPointerInfo().getLocation().x - 2, MouseInfo.getPointerInfo().getLocation().y - 2, 1, false));
//         }else if(e.getKeyCode() == KeyEvent.VK_EQUALS){
//             for (Charge c :
//                     charges) {
//                 c.q *= 2f;
//             }
//             startingQ*=2f;
//             voltageHashMap = computeVoltageMap();
//         }else if(e.getKeyCode() == KeyEvent.VK_MINUS){
//             if(charges.get(0).q/2f != .5f){
//                 for (Charge c :
//                         charges) {
//                     c.q/=2f;
//                 }
//                 startingQ/=2f;
//             }
//             voltageHashMap = computeVoltageMap();

//         }else if(e.getKeyCode() == KeyEvent.VK_D){
//             clearDisp = true;
//         }else if(e.getKeyCode() == KeyEvent.VK_C){
//             charges.clear();
//             tCharges.clear();
//             eqpotentials.clear();
//             clearDisp = true;
//         }else if(e.getKeyCode() == KeyEvent.VK_O){

//             if(!ShiftPressed) {
//                 angleOffset += .1;
//                 for (Charge c :
//                         charges) {
//                     int chargeToPlace = (int) (c.q / Math.abs(c.q));
//                     for (float i = 0; i < 2f * 3.14f; i += 2f * 3.14f / (12f)) {
//                         float ang = (i + angleOffset);
//                         int x = (int) (c.x + (4.f) * Math.cos(ang));
//                         int y = (int) (c.y + (4.f) * Math.sin(ang));

//                         tCharges.add(new Charge(x, y, chargeToPlace, true));


//                     }


//                 }
//             }

//         }else if(e.getKeyCode() == KeyEvent.VK_F)
//             field = !field;
//         else if(e.getKeyCode() == KeyEvent.VK_T)
//             trace = !trace;
//     }
// }
