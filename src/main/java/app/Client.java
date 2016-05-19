package app;

import com.samutamm.fileserver.FileObject;
import com.samutamm.fileserver.FileServer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.List;

public class Client {

    private Printer printer;

    public Client(Printer p) {
        this.printer = p;
    }

    public void call(String server) throws TException {
        TTransport transport = new TSocket(server, 4100);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FileServer.Client client = new FileServer.Client(protocol);
        List<FileObject> list = client.listFiles();
        for (FileObject file : list) {
            printer.print(file.name);
        }
        if (list.size() == 0) {
            printer.print("No files");
        }
    }

    public void copyFile(String server, String path, String name) throws TException{
        TTransport transport = new TSocket(server, 4100);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FileServer.Client client = new FileServer.Client(protocol);
        printer.print(""+client.addFile(path, name));
    }

    public void deleteFile(String server, String path) throws TException {
        TTransport transport = new TSocket(server, 4100);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FileServer.Client client = new FileServer.Client(protocol);
        printer.print(""+client.deleteFile(path));
    }

}

class Printer {
    public String print(String message) {
        System.out.println(message);
        return message;
    }
}
