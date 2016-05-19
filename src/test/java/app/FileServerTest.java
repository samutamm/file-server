package app;

import org.apache.thrift.TException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileServerTest {

    private Thread server;
    private Printer printer;

    @Before
    public void setUp() throws Exception {
        System.out.println("MOLO");
        this.server = new Thread(new Server());
        this.server.start();
        printer = Mockito.mock(Printer.class);
    }

    @After
    public void tearDown() throws Exception {
       this.server.interrupt();
    }

    @Test(timeout = 5000)
    public void testFileListing() throws Exception {
        new Client(printer).call("localhost");
        Mockito.verify(printer).print(Mockito.contains("vittu"));
    }

    @Test(timeout = 5000)
    public void testNewFile() throws Exception {
        String fileName = "testFile.txt";
        File file = new File(Server.filesystemPath + "/" + fileName);
        assertFalse(file.exists());
        addFile(fileName);
        assertTrue(file.exists());
        file.delete();
    }

    private void addFile(String fileName) throws TException {
        String testFileLocation =
                "/home/samutamm/Documents/INSA/MID/TP/TP1/file-server/src/test/resources/"
                        + fileName;
        new Client(printer).copyFile("localhost", testFileLocation, fileName);
    }

    @Test
    public void testDeleteFile() throws Exception {
        String filename = "testFile.txt";
        addFile(filename);
        File file = new File(Server.filesystemPath + "/" + filename);
        assertTrue(file.exists());
       // String filename = "test.py";
        Thread.sleep(200);
        new Client(printer).deleteFile("localhost", Server.filesystemPath + "/" + filename);
        assertFalse(!file.exists());
    }

}