import javax.net.ssl.KeyManager;
import javax.swing.text.JTextComponent;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.Kernel;

public class CanvasKeyCallbacks extends KeyAdapter {
    int x = 0;
    int y = 0;

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        x = MouseInfo.getPointerInfo().getLocation().x;
        y = MouseInfo.getPointerInfo().getLocation().y;

        if(e.getKeyCode() == KeyEvent.VK_SPACE) spaceAction();
//        if(e.getKeyCode() == KeyEvent.VK_)
    }

    private void spaceAction(){
        ParticleSystem.addNewParticle(x, y, 1, false);
    }
}
