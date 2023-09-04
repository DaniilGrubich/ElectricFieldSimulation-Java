import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class CanvasMouseCallbacks extends MouseInputAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mousePressed(e);
        int x = e.getX();
        int y = e.getY();
        int b = e.getButton();

        if(b == 1)
            ParticleSystem.addNewParticle(x, y, 1, true);
        else if(b == 3)
            ParticleSystem.addNewParticle(x, y, -1, true);

    }
}
