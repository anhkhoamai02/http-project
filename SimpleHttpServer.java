import java.io.*;
import java.net.*;
import java.nio.file.*;

public class SimpleHttpServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is listening on port 8080");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    handleClient(socket);
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String requestLine = in.readLine();
        if (requestLine != null && requestLine.startsWith("GET")) {
            String fileName = requestLine.split(" ")[1];
            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            File file = new File(fileName);
            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath());

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/plain");
                out.println("Content-Length: " + fileBytes.length);
                out.println();
                out.flush();

                socket.getOutputStream().write(fileBytes);
                socket.getOutputStream().flush();
            } else {
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/plain");
                out.println();
                out.println("404");
                out.flush();
            }
        } else {
            out.println("HTTP/1.1 400 Bad Request");
            out.println("Content-Type: text/plain");
            out.println();
            out.println("Unsupported request");
            out.flush();
        }

        socket.close();
    }
}
