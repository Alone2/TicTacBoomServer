package ch.blobber.tictacboom;

import ch.jeda.Connection;
import ch.jeda.Data;
import ch.jeda.Jeda;
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
            Jeda.addEventListener(this);
            System.out.println("Server IP: " + myIp + ", Port: " + String.valueOf(port));

        }
        else
        {
            System.err.print("Error: cannot start server");
        }

    }
    
    public void close() {
        s.stop();
    }

    @Override
    public void onMessageReceived(MessageEvent me)
    {        
        Data d = me.getData();
        System.out.println("Question: " + me.getLine());
        String from = d.readString("From");

        Data returnData = new Data();
        if ("canPlay".equals(from))
            returnData = server.canPlayReturn(d);
        if ("doMove".equals(from))
            returnData = server.doMoveReturn(d);
        
        System.out.println("Respone: " + returnData.toString());
        
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
        data.writeInt("playerSize", server.playerSize);
        players++;
        
        Connection connection = ce.getConnection();
        connection.sendData(data);
    
    }
}
