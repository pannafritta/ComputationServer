import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private static BufferedReader br;
    private static BufferedWriter bw;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Socket socket = setUp(args);

            while (true) {
                System.out.println("Enter command (type 'BYE' to close the connection, 'FILE <filename>' to send commands from a file):");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("BYE")) {
                    break;
                } else if (input.startsWith("FILE")) {
                    String filename = "requests.txt";
                    sendCommandsFromFile(filename);
                } else {
                    sendMessage(input);
                }
            }

            socket.close();
            System.out.println("Connection closed.");
        } catch (ConnectException e) {
            System.err.println("Server not available.");
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void sendMessage(String message) throws IOException {
        String sent = message;
        bw.write(sent + System.lineSeparator());
        bw.flush();
        String received = br.readLine();
        System.out.printf("Sent: %s%nReceived: %s%n", sent, received);
    }

    private static void sendCommandsFromFile(String filename) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                sendMessage(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    private static Socket setUp(String[] args) throws IOException {
        InetAddress serverInetAddress;
        if (args.length > 1) {
            serverInetAddress = InetAddress.getByName(args[0]);
        } else {
            serverInetAddress = InetAddress.getLocalHost();
        }
        Socket socket = new Socket(serverInetAddress, 2000);
        br = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        bw = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())
        );
        return socket;
    }
}
