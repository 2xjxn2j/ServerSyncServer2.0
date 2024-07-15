import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler {

    InetAddress address;
    int port;
    Socket socket;


    PrintWriter os;

    BufferedReader is;


    public ClientHandler(Socket socket) throws IOException {
        os = new PrintWriter(socket.getOutputStream(), true);
        is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.address = socket.getInetAddress();
        this.port = socket.getPort();
        this.socket = socket;

        System.out.println("New client!");
        System.out.println(address + ":" + port + " (#1)");

        Thread listener = new Thread(() -> {
            while(true) {
                try {

                    String text = is.readLine();
                    System.out.println("Received from " + text);

                    Main.sendPacketToAll(text, this);


                } catch (IOException e) {
                    System.out.println("Connection to " + address + " has closed!");
                    os.close();
                    try {
                        is.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Main.clients.remove(this);
                    break;
                }
            }
        });
        listener.start();
    }

//    void send(byte[] bytes) {
//        os.println(bytes);
//    }



}
