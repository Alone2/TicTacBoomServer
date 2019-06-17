
package ch.blobber.tictacboom;

import ch.jeda.ui.TextButton;


public class ValueButton extends TextButton
{    
    int playerSize;
    TicTacBoomServer t;
    
    public ValueButton(double x, double y, double width, double height, int playerSize, TicTacBoomServer t)
    {
        super(x, y, String.valueOf(playerSize), 0);
        this.setWidth(width);
        this.setHeight(height);
        this.setTextSize(height / 1.64);
        this.t = t;
        this.playerSize = playerSize;
    }
    
    @Override
    public void clicked()
    {
        t.playerSize(playerSize);
    }

}
