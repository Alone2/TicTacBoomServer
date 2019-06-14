
package ch.blobber.tictacboom;

import ch.jeda.Program;
import ch.jeda.ui.Canvas;
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
        view = new View(500, 500, ViewFeature.ORIENTATION_LANDSCAPE);
        
        server = new Server(playersize);
  
    }
    
    public void reset() {
        
    }
}
