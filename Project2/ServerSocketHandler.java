// This thread simply listens for connections on port 5000 and starts a new Connection Thread for each incoming connection
import java.io.*;
import java.net.*;
import java.util.*;

class ServerSocketHandler extends Thread
{
    boolean a = true;
    Server s;
    ArrayList<Connection> connectionList;
    ArrayList<info> clientInfoList;

    public ServerSocketHandler(Server s, ArrayList<Connection> connectionList, ArrayList<info> clientInfoList){
        this.s=s;
        this.connectionList=connectionList;
        this.clientInfoList = clientInfoList;
    }

    public void run() {
        int totalClients = 0;
        Socket clientSocket;
        while (a) {
           // wait for incoming connections. Start a new Connection Thread for each incoming connection.
            try {
                clientSocket = s.listener.accept();
                Connection c = new Connection(clientSocket, connectionList, clientInfoList);
                connectionList.add(c);
                c.start();


                System.out.println("---------------");
                System.out.println("Port : " + c.socket.getPort());
                System.out.println("IP: " + c.socket.getLocalAddress());

                for(int i=0; i <=connectionList.size(); i++) {
                    totalClients = i;
                }
                System.out.println("Client connected. Total Registered Clients : " + totalClients);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //other methods may be necessary. Include them when appropriate.
}