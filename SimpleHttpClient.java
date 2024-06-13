import java.io.*;
import java.net.*;

public class SimpleHttpClient {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SimpleHttpClient <URL>");
            return;
        }

        try {
            URI uri = new URI(args[0]);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }

            in.close();

            System.out.println("Response Body:");
            System.out.println(response.toString());

        } catch (URISyntaxException e) {
            System.out.println("Invalid URI: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }
}
