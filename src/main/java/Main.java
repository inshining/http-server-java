import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
//    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
    final HttpServer server;
    if (args.length >= 2 && args[0].equals("--directory")){
      server = new HttpServer(4221, 10, args[1]);
    }else{
      server = new HttpServer(4221, 10);
    }
    server.run();
  }
}
