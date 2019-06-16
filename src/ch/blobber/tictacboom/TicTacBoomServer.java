
package ch.blobber.tictacboom;

import ch.jeda.Program;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Text;
import ch.jeda.ui.View;
import ch.jeda.ui.ViewFeature;

/**
 *
 * @author alain.sinzig
 */
public class TicTacBoomServer extends Program
{

    View view;
    Canvas canvas;
    Server server;
    int playersize = 2;

    @Override
    public void run()
    {
        view = new View(200, 200, ViewFeature.ORIENTATION_LANDSCAPE);
        Text t = new Text(20, view.getWidthDp() / 2, "I'm just a Server \nI display nothing...");
        view.add(t);
        server = new Server(playersize, this);
        
    }
    
    public void reset() {
        
    }
}
