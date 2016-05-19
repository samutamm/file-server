package app;

import com.samutamm.fileserver.FileObject;
import com.samutamm.fileserver.FileServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{

    private TServer server;

    public static String filesystemPath = "/home/samutamm/Documents/INSA/MID/TP/TP1/file-server/filesystem";

    @Override
    public void run() {
        try {
            start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static class FileServerHandler implements FileServer.Iface {

        @Override
        public List<FileObject> listFiles() {
            List<FileObject> list = new ArrayList<>();

            File file = new java.io.File(Server.filesystemPath);
            if (!file.isDirectory()) {
                System.out.println("NO FILESYSTEM DIRECTORY");
                System.out.println(file.toString());
                return new ArrayList<>();
            }
            File[] files = file.listFiles();
            for(File f: files) {
                try {
                    ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(f.toPath()));
                    FileObject newFile = new FileObject(f.getName(), buffer);
                    list.add(newFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        @Override
        public boolean addFile(String oldPath, String newName) {
            try {
                File newFile = new File(Server.filesystemPath + "/" + newName);
                if (newFile.exists()) {
                    System.out.println("New file allready exists!");
                    return false;
                }
                System.out.println("Copying " + oldPath.toString() + " to " + newFile.toString());
                File oldFile = new File(oldPath);
                if (!oldFile.exists()) {
                    System.out.println("File does not exists!");
                    return false;
                }
                Files.copy(oldFile.toPath(), newFile.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        public boolean deleteFile(String path) {
            return new File(path).delete();
        }
    }

    public void start() throws TTransportException {
        FileServer.Processor<FileServerHandler> processor = new FileServer.Processor<>(new FileServerHandler());
        TServerTransport transport = new TServerSocket(4100);
        server = new TThreadPoolServer(new TThreadPoolServer.Args(transport).processor(processor));
        server.serve();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
