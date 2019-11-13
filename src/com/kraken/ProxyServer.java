package com.kraken;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ProxyServer {

    private static ServerSocket clientServerSocket;
    private static Socket clientSocket;
    private static ObjectOutputStream clientOOS;
    private static ObjectInputStream clientOIS;

    // port on which proxy server is running
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            // init everything
            initEverything();
            // setup streams with the client
//            setupStreams();
            // send response to the client
            sendResponse();
//            fetchPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchPage(String urlString) throws Exception {
        URL url = new URL(urlString);
        System.out.printf("Fetching %s\n", url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.printf("Connection established with %s\n", url);
        InputStream in = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(in);
        System.out.printf("Reading stream from %s\n", url);
        int s = reader.read();

        String res = "";
        while (s != -1) {
            res += (char) s;
            s = reader.read();
        }
        System.out.printf("Fetch data complete");
        return res;
    }

    private static void initEverything() throws IOException {
        clientServerSocket = new ServerSocket(PORT, 10);
        System.out.println("Listening on port: " + PORT);
        clientSocket = clientServerSocket.accept();
        System.out.println("Connected to: " + clientSocket.getInetAddress().toString());
    }

    private static void setupStreams() throws IOException {
        clientOOS = new ObjectOutputStream(clientSocket.getOutputStream());
        clientOOS.flush();
        clientOIS = new ObjectInputStream(clientSocket.getInputStream());
    }

    private static void sendResponse() throws IOException {
//        System.out.println(msg);
        while (true) {
            try (Socket clientSocket = clientServerSocket.accept()) {
    //            // experiment
    //            // show what the browser sent
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String line = reader.readLine();
                    String[] parts = line.split(" ");
    //            while (!line.isEmpty()) {
                    System.out.printf("URL: %s\n", parts[1].substring(1));
    //                line = reader.readLine();
    //            }
    //
    //                // send a response now
    //                // today's date
                Date today = new Date();
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
    //                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + fetchPage(parts[1].substring(1));
                    System.out.println(httpResponse);
                    clientSocket.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
