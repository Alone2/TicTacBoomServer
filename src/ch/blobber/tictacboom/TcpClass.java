package ch.blobber.tictacboom;

import ch.jeda.Connection;
import ch.jeda.Data;
import ch.jeda.TcpConnection;
import ch.jeda.TcpServer;
import ch.jeda.event.ConnectionAcceptedListener;
import ch.jeda.event.ConnectionEvent;
import ch.jeda.event.MessageEvent;
import ch.jeda.event.MessageReceivedListener;

import java.net.InetAddress;

/**
 *
 * @author alain.sinzig
 */
public class TcpClass implements MessageReceivedListener, ConnectionAcceptedListener
{

    TcpConnection c;
    TcpServer s;
    int port;
    Server server;
    int players = 0;

    public TcpClass(Server server, int port)
    {
        this.s = new TcpServer();
        this.server = server;
        this.port = port;

        if (s.start(port))
        {
            String myIp = "";
            try
            {
                InetAddress inetAddress = InetAddress.getLocalHost();
                myIp = inetAddress.getHostAddress();
            }
            catch (Exception e)
            {
                System.err.print("Error: cannot get server ip");
                myIp = "error";
            }
            System.out.println("Server IP: " + myIp + ", Port: " + String.valueOf(port));

        }
        else
        {
            System.err.print("Error: cannot start server");
        }

    }

    @Override
    public void onMessageReceived(MessageEvent me)
    {        
        Data d = me.getData();
        System.out.println("ch.blobber.tictacboom.TcpClass.onMessageReceived()");
        String from = d.readString("From");

        Data returnData = new Data();
        if ("canPlay".equals(from))
            returnData = server.canPlayReturn(d);
        if ("doMove".equals(from))
            returnData = server.doMoveReturn(d);
        
        Connection connection = me.getConnection();
        connection.sendData(returnData);
    }

    @Override
    public void onConnectionAccepted(ConnectionEvent ce)
    {
        Data data = new Data();
        System.out.println("ch.blobber.tictacboom.TcpClass.onConnectionAccepted()");
        data.writeString("isConnected", "true");
        data.writeString("From", "isHere");
        data.writeInt("myself", players);
        players++;
        
        Connection connection = ce.getConnection();
        connection.sendData(data);
    
    }
}
