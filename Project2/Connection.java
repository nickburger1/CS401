// The connection Thread is spawned from the ServerSocketHandler class for every new Client connections.
// Responsibilities for this thread are to hnadle client specific actions like requesting file, registering to server, and client wants to quit.
import java.io.*;
import java.net.*;
import java.util.*;



class Connection extends Thread
{
    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    int peerPort;
    int peer_listen_port;
    int peerID;
    InetAddress peerIP;
    char FILE_VECTOR[];
    ArrayList<Connection> connectionList;
    ArrayList<info> clientInfoList;
    ArrayList<info> client_info;
    info cInfo;

    public Connection(Socket socket, ArrayList<Connection> connectionList, ArrayList<info> clientInfoList) throws IOException
    {
        this.connectionList=connectionList;
        this.clientInfoList = clientInfoList;
        this.socket=socket;
        this.outputStream=new ObjectOutputStream(socket.getOutputStream());
        this.inputStream=new ObjectInputStream(socket.getInputStream());
        this.peerIP=socket.getInetAddress();
        this.peerPort=socket.getPort();
        this.client_info = new ArrayList<info>();
        this.cInfo = new info();
    }



    @Override
    public void run() {

        //wait for register packet.
        // once received, listen for packets with client requests.


        Packet p;

        while (true){
            try {
                p = (Packet) inputStream.readObject();
                eventHandler(p); // Initial packet

                p = (Packet) inputStream.readObject();
                eventHandler(p); // Packet after request
            }
            catch (Exception e) {break;}
        }
    }

    public void eventHandler(Packet p) throws IOException {
        int event_type = p.event_type;
        switch (event_type)
        {
            case 0: //client register
                p.printPacket();
                this.cInfo.FileV = p.getFileVector();
                this.cInfo.id = p.getSenderId();
                clientInfoList.add(this.cInfo);
                break;
            case 1: //client is requesting a file
                System.out.println("Client " + p.peerID + " is requesting file " + p.req_file_index);
                findIndex(p);
                break;
            case 2: //server sending info to client
                findIndexFromServer(p);
                break;
            case 5: //client wants to quit
                quit(p);
                break;
        }
}

    void quit(Packet p){
        int i;
        System.out.println("Removing client " + p.peerID);
        for(i = 0; i< connectionList.size(); i++){
            if(connectionList.get(i).equals(this)){
                connectionList.remove(i);
                clientInfoList.remove(i);
            }
        }
        System.out.println("Total Registered Clients: " + connectionList.size());

    }

    void findIndexFromServer(Packet p) throws IOException {
        int i=0;
        System.out.println("Packet Sent");
        String msg =  "Server says that no client has file " + p.req_file_index;
        for(i = 0; i < clientInfoList.size(); i++) {
            if(clientInfoList.get(i).FileV[p.req_file_index] == '0') {
                continue;
            }
            else if (clientInfoList.get(i).FileV[p.req_file_index] == '1') {
                msg = "Server says that peer " + clientInfoList.get(i).id + " on listening port " + p.port_number + " has file " + p.req_file_index;
                break;

            }

        }
        outputStream.writeObject(msg);



    }


    void findIndex(Packet p) throws IOException {
        String msg = " ";
        for(int i = 0; i < connectionList.size(); i++) {
            if(connectionList.get(i).equals(this)) {
                String FileVectorString = String.valueOf(p.FILE_VECTOR);
                if (FileVectorString.charAt(p.req_file_index) == '0') {
                    msg = "I don't have the file. Let me contact server...";
                    outputStream.writeObject(msg);
                    Packet ps = p;
                    ps.event_type = 2;
                    eventHandler(ps);
                }
                else if (FileVectorString.charAt(p.req_file_index) == '1') {
                    msg = "I already have this file block!";
                    outputStream.writeObject(msg);
                    outputStream.writeObject(" ");

                }

            }
        }

    }
    
    //other methods go here

}
