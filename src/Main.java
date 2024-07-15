import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Main {
    public static List<ClientHandler> clients = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1000);
        System.out.println("Started listening on 1000...");

        Thread t = new Thread() {
            @Override
            public void run() {
                while(true) {
                    Socket client = null;
                    try {
                        client = server.accept();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    boolean x = false;
                    for(ClientHandler newClient : clients) {
                        if(newClient.address == client.getInetAddress()) {
                            x=true;
                        }
                    }
                    if(x) {
                        System.out.println("Client is a duplicate");
                    } else {
                        ClientHandler handler = null;
                        try {
                            handler = new ClientHandler(client);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        clients.add(handler);
                    }
                }
            }

        };
        t.start();

    }

    public static void sendPacketToAll(String line, ClientHandler from) {
        for(ClientHandler client : clients){
            if(!client.address.getHostAddress().equalsIgnoreCase(from.address.getHostAddress())){
                client.os.println(line);
                System.out.println("Sent packet to: " + client.address);
            }
            if(clients.size() == 1) {
                System.out.println("There is only 1 server!!!");
            }
        }
    }

}