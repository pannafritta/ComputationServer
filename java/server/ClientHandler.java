package server;

import server.utils.ResponseFormatter;
import server.utils.Stopwatch;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private int requestsCounter = 0;
    private final ComputationServer server;

    public ClientHandler(Socket socket, ComputationServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        Stopwatch stopwatch = new Stopwatch();
        System.out.printf("[%1$tY-%1$tm-%1$td %1$tT] Connection from %2$s.%n", System.currentTimeMillis(), socket.getInetAddress());
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String line = br.readLine();
                stopwatch.start();
                if (server.isQuitCommand(line)) {
                    socket.close();
                    break;
                }
                try {
                    double requestTime = stopwatch.stop();
                    bw.write(ResponseFormatter.okResponse(requestTime, server.process(line)));
                    bw.flush();
                    server.addTime(requestTime);
                } catch (Exception e) {
                    bw.write(ResponseFormatter.errResponse(e.getMessage()));
                }
                bw.flush();
                requestsCounter = requestsCounter + 1;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.printf("[%1$tY-%1$tm-%1$td %1$tT] Disconnection of %2$s after %3$d requests.%n", System.currentTimeMillis(), socket.getInetAddress(), requestsCounter);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}