import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
//    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
    final HttpServer server = new HttpServer(4221, 10);
    server.run();
  }
}
