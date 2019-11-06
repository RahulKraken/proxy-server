package com.kraken;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try (Socket clientSocket = clientServerSocket.accept()) {
            // experiment
            // show what the browser sent
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line = reader.readLine();
            while (!line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }

            // send a response now
            // today's date
            Date today = new Date();
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n<h1>Hello there!!!</h1>";
            clientSocket.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
