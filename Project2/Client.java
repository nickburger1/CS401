// The client class will implement the functions listed in the project description.
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Client {

    int serverPort;
    InetAddress ip = null;
    Socket s;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;



    public static void main(String[] args) throws FileNotFoundException {

        int peerID = 0;
        int peer_listen_port = 0;
        char FILE_VECTOR[] = new char[0];

        Scanner sc = new Scanner(new File(args[1]));

        while (sc.hasNextLine()) {
            String[] lineData = sc.nextLine().split(" ");


            if (lineData[0].equals("CLIENTID")) {
               peerID = Integer.parseInt(lineData[1]);
            }
            if (lineData[0].equals("MYPORT")) {
                peer_listen_port = Integer.parseInt(lineData[1]);
            }

            if (lineData[0].equals("FILE_VECTOR")) {
                for (char ch : lineData[1].toCharArray()) {
                }
            }
            FILE_VECTOR = lineData[1].toCharArray();
        }


        Client client = new Client();


            try{

                client.s = new Socket(args[0],5000);
                client.outputStream=new ObjectOutputStream(client.s.getOutputStream());
                ClientPacketHandler packetHandler = new ClientPacketHandler(client.s, client.outputStream, peerID, client.s.getPort(), peer_listen_port, FILE_VECTOR);

                packetHandler.start();

                System.out.print("Connected to Server...");
                System.out.println(client.s);
                System.out.println("Packet Sent");


            } catch (IOException e) {
                e.printStackTrace();
            }
        // create client object and connect to server. If successful, print success message , otherwise quit.

        // Once connected, send registration info, event_type=0
        // start a thread to handle server responses. This class is not provided. You can create a new class called ClientPacketHandler to process these requests.

        //done! now loop for user input




    }
}


