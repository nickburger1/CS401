import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

class ClientPacketHandler extends Thread{
    Socket s;
    ObjectOutputStream ostream;
    ObjectInputStream istram;
   // Scanner istream;

    int event_type;
    int peerID;
    int port_number;
    int peer_listen_port;
    char[] FILE_VECTOR;
    int req_file_index;

    public ClientPacketHandler(Socket s, ObjectOutputStream outputStream, int peerID, int port_number, int peer_listen_port, char[] FILE_VECTOR) throws IOException {

        this.s=s;
        this.ostream=outputStream;
        this.port_number=port_number;
        this.peer_listen_port=peer_listen_port;
        this.peerID=peerID;
        this.FILE_VECTOR=FILE_VECTOR;
        istram = new ObjectInputStream(s.getInputStream());

    }
    
    public void run(){

        Packet initialPacket = new Packet();
        initialPacket.peerID = peerID;
        initialPacket.event_type = 0;
        initialPacket.port_number = this.s.getPort();
        initialPacket.peer_listen_port = peer_listen_port;
        initialPacket.req_file_index = req_file_index;
        initialPacket.FILE_VECTOR = FILE_VECTOR;


        try {
            this.ostream.writeObject(initialPacket);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a query");
            while(true)
            {
                String msg = scanner.nextLine();
                if(msg.equals("q")) {
                    Packet packetQuit = new Packet();
                    packetQuit.event_type = 5;
                    packetQuit.peerID = peerID;
                    this.ostream.writeObject(packetQuit);
                    break;

                }

                else if(msg.equals("f"))
                {
                    System.out.println("Enter the file index you want");
                    int ind = scanner.nextInt();

                    if (ind < 64)
                    {
                        Packet filePacket = new Packet();
                        filePacket.event_type = 1;
                        filePacket.req_file_index = ind;
                        filePacket.port_number = this.s.getLocalPort();
                        filePacket.peerID = peerID;
                        filePacket.peer_listen_port = peer_listen_port;
                        filePacket.FILE_VECTOR = FILE_VECTOR;
                        this.ostream.writeObject(filePacket);
                        continue;
                    } else {
                        System.out.println("Invalid index!");
                    }

                }

                Object o = istram.readObject();
                System.out.println(o);
                Object op = istram.readObject();
                System.out.println(op);
                System.out.println("Enter a query");

            }




        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
