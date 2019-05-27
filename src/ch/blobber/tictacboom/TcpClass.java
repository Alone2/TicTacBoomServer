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
    int port = 1234;

    public void TcpClass()
    {
        s = new TcpServer();

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
                //error
                myIp = "error";
            }

        }
        else
        {
            // error
        }

    }

    @Override
    public void onMessageReceived(MessageEvent me)
    {
        String line = me.getLine();
    }

    @Override
    public void onConnectionAccepted(ConnectionEvent ce)
    {
        Data data = new Data();
        data.writeString("connectionAcepptet", "true");
        
        Connection connection = ce.getConnection();
        connection.sendData(data);
    
    }
}
