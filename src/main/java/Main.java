import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
//    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
     try {
       ServerSocket serverSocket = new ServerSocket(4221);

       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);

       try (
               Socket socket = serverSocket.accept(); // Wait for connection from client.
               BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               OutputStream out = socket.getOutputStream();
               ){
           String method, path, httpVersion;

           String inputLine = in.readLine();
           // first Line
           if (!isRequestExist(inputLine)){
               out.write(
                       "HTTP/1.1 404 Not Found\r\n\r\n".getBytes()
               );
               return;
           }
           String[] tokens = inputLine.split(" ");
           method = getMethod(tokens);
           path = getPath(tokens);
           httpVersion = getHttpVersion(tokens);

           if (method == null || path == null || httpVersion == null ){
               out.write(
                       "HTTP/1.1 404 Not Found\r\n\r\n".getBytes()
               );
               return;
           }

           if (path.equals("/") || path.equals("/index.html")){
               out.write(
                       "HTTP/1.1 200 OK\r\n\r\n".getBytes()
               );
           } else{
               out.write(
                       "HTTP/1.1 404 Not Found\r\n\r\n".getBytes()
               );
           }
       }
       System.out.println("accepted new connection");
//       serverSocket.
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

  public static boolean isRequestExist(String inputLine){
      return inputLine  != null && !inputLine.isEmpty();
  }

  public static String getMethod(String[] tokens){
      if (tokens == null || tokens.length == 0){
          return null;
      }
      return tokens[0];
  }

    public static String getPath(String[] tokens){
        if (tokens == null || tokens.length < 2){
            return null;
        }
        return tokens[1];
    }

    public static String getHttpVersion(String[] tokens){
        if (tokens == null || tokens.length < 3){
            return null;
        }
        return tokens[2];
    }

}
