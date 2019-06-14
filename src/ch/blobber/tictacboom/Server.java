
package ch.blobber.tictacboom;

import ch.jeda.Data;

/**
 *
 * @author alain.sinzig
 */
public class Server
{
    TcpClass tcp;
    int port = 6465;
    int playerSize;
    int playerTurn = 0;
    String[] spielfeld;
    
    public Server(int playerSize) {
        this.playerSize = playerSize;
        this.tcp = new TcpClass(this, port);
        this.spielfeld = new String[playerSize];
        //this.start();
    }

    Data canPlayReturn(Data d) {
        int he = d.readInt("myself");
        Boolean canPlay =  tcp.players >= playerSize && playerTurn == he;
        d.writeBoolean("canPlay", canPlay);
        
        // Look if won and so on
        
        d.writeStrings("saveStringArr", spielfeld);
         
        return d;
    }

    Data doMoveReturn(Data d) {
        int he = d.readInt("myself");    
        if (tcp.players >= playerSize && playerTurn == he) {
            String move = d.readString("move"); 
            // Do complicated thing of looking if cords alrady crossed
            // then
            spielfeld[he] += "9" + move;
        }
        d.writeStrings("saveStringArr", spielfeld);
        
        return d;
    }
}
