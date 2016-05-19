package app;

import org.apache.thrift.TException;

public class Main {
    public static void main(String[] args) throws TException {
        if (args.length < 1) {
            kill();
        }
        String command = args[0];

        Printer p = new Printer();
        switch (command) {
            case "server":
                new Server().start();
                return;
            case "client":
                if (args.length < 2) kill();
                new Client(p).call(args[1]);
                return;
            case "newfile":
                if (args.length < 4) kill();
                new Client(p).copyFile(args[1], args[2], args[3]);
                return;
            case "delete":
                if (args.length < 3) kill();
                new Client(p).deleteFile(args[1], args[2]);
                return;
        }
    }

    private static void kill() {
        System.err.println("Server mode: server");
        System.err.println("Client mode: client hostname");
        System.exit(-1);
    }
}
