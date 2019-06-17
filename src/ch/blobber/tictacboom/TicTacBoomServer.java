
package ch.blobber.tictacboom;

import ch.jeda.Program;
import ch.jeda.event.Button;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Text;
import ch.jeda.ui.View;
import ch.jeda.ui.ViewFeature;
import java.util.ArrayList;

/**
 *
 * @author alain.sinzig
 */
public class TicTacBoomServer extends Program
{

    View view;
    Canvas canvas;
    Server server;
    ArrayList<ValueButton> buts;
    
    @Override
    public void run()
    {
        view = new View(200, 200, ViewFeature.ORIENTATION_LANDSCAPE);
        buts = new ArrayList<ValueButton>();
        int[] sizes = {2,3,4};
        for (int i = 0; i < sizes.length; i++)
        {
            float width = view.getWidthDp() / (sizes.length + 1) * (i + 1);
            int bigness = 30;
            ValueButton b = new ValueButton(width - bigness/2, view.getHeightDp()/2, bigness, bigness, sizes[i], this);
            buts.add(b);
            view.add(b);
        }
                
    }
    
    public void playerSize(int playerSize) {
        for (int i = 0; i < buts.size(); i++)
        {
            view.remove(buts.get(i));
        }
        server = new Server(playerSize, this);
    }
}
