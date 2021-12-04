// The server class will implement the functions listed in the project description. 

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class Server {

    int serverPort;
    int MAX_CONNECTED_CLIENTS;
    ServerSocket listener;
    int numClients;
    ArrayList<Connection> connectionList;
    ArrayList<info> clientInfoList;


    public Server() {
        serverPort=5000;
        MAX_CONNECTED_CLIENTS=20;
        listener=null;
        numClients=0;
        connectionList= new ArrayList<Connection>();
        clientInfoList= new ArrayList<info>();
    }

    public static void main(String args[]){


        //First, let's start our server and bind it to a port(5000).

        //Next let's start a thread that will handle incoming connections
        try {
            Server server = new Server();
            server.listener = new ServerSocket(server.serverPort);
            System.out.println("Started Server. Waiting for a client");
            ServerSocketHandler c = new ServerSocketHandler(server, server.connectionList, server.clientInfoList);

            c.start();


            boolean running = true;
            Scanner input = new Scanner(System.in);
            String command = null;
            while (running)
            {
                // wait on user inputs
                System.out.println("Enter a query");


                command = input.nextLine();

                if(command.length() == 0){
                    System.out.println("Please enter a command");
                }
                else if(command.equals("q")){
                    System.exit(0);
                    running = false;
                    server.listener.close();
                    break;
                }
                else if(command.equals("i"))
                {
                    System.out.println("Listing all file vectors!");
                    for(int i = 0; i < server.clientInfoList.size(); i++)
                    {
                        System.out.print("Client ID: " + server.clientInfoList.get(i).id + ": ");
                        System.out.println(server.clientInfoList.get(i).FileV);
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Note in programs shown in class, at this point we listen for incoming connections
        // in the main method.
        // However for this project since the server has to handle incoming connections
        // and also handle user input simultaneously, we start a separate thread to listen
        // for incoming connections in the Server. This is the ServerSocketHandler thread,
        // which will in turn spawn new Connection Threads, for each client connection.


        //Done! Now main() will just loop for user input!.
        //will quit on user input



        }
    // add other methods as necessaryu. For example, you will prbably need a method to print the incoming connection info.

}


