package server;

import command.CommandProcessor;
import command.CommandParser;
import command.Command;
import command.ComputationCommand;
import command.StatCommand;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputationServer {
    private final int PORT;
    private final CommandParser validator;
    private final ServerStats stats; // le modifiche di questo dovranno essere sincronizzate
    private final CommandProcessor processor;
    ExecutorService computationExecutor;

    public ComputationServer(int port) {
        this.PORT = port;
        validator = new CommandParser();
        processor = new CommandProcessor();
        stats = new ServerStats();
        int numProcessors = Runtime.getRuntime().availableProcessors();
        computationExecutor = Executors.newFixedThreadPool(numProcessors);
    }

    public void run() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                computationExecutor.submit(new ClientHandler(socket, this));
            }
        }
    }

    public String process(String input) {
        Command command = validator.parseCommand(input);

        if (command.isStatCommand()) {
            return processor.processStatCommand((StatCommand) command, stats);
        }

        if (command.isComputationCommand()) {
            return processor.processComputationCommand((ComputationCommand) command);
        }

        return input;
    }

    public boolean isQuitCommand(String input) {
        return validator.isQuitCommand(input);
    }

    public void addTime(double requestTime) {
        stats.addTime(requestTime);
    }
}