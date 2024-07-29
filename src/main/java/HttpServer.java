import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final int port;
    private final ExecutorService executorService;

    public HttpServer(final  int port, final int concurrencyLevel){
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(concurrencyLevel);
    }

    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while (true){
                Socket socket = serverSocket.accept(); // Wait for connection from client.
                executorService.submit(() -> {
                    try {
                        handleRequest(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void handleRequest(Socket socket) throws IOException {
        try (
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

            Map<String, String> headers = handlerHeaders(in);

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
            }
            else if(path.startsWith("/echo") || path.equals("/user-agent")){
                String pathStr =  "";
                if (path.startsWith("/echo")){
                    pathStr = path.split("/")[2];
                } else{
                    pathStr = headers.get("User-Agent");
                }
                responseBodyHandler(out, pathStr);

            }
            else{
                out.write(
                        "HTTP/1.1 404 Not Found\r\n\r\n".getBytes()
                );
            }
        }
        System.out.println("accepted new connection");
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

    public static void responseBodyHandler(OutputStream out, String pathStr) throws IOException{
        int strLen = pathStr.length();
        StringBuilder sb = new StringBuilder();
        sb.append("Content-Length: ");
        sb.append(strLen);
        sb.append("\r\n\r\n");

        // status
        out.write( "HTTP/1.1 200 OK\r\n".getBytes());
        // headers
        out.write( "Content-Type: text/plain\r\n".getBytes());
        out.write( sb.toString().getBytes());

        // body
        out.write(pathStr.getBytes());
    }

    public static Map<String, String> handlerHeaders(BufferedReader in) throws  IOException{
        Map<String, String> header = new HashMap<>();
        String inputLine = in.readLine();
        while (inputLine != null && !inputLine.isEmpty()){
            String key = inputLine.split(":",2)[0].trim();
            String value = inputLine.split(":",2)[1].trim();

            header.put(key, value);
            inputLine = in.readLine();
        }
        return header;
    }

}
