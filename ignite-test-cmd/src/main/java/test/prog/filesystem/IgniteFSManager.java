package test.prog.filesystem;

import org.apache.ignite.IgniteFileSystem;
import org.apache.ignite.igfs.IgfsPath;
import test.prog.IgniteManger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements sample usages of ignite file system</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public class IgniteFSManager extends IgniteManger {

    /**
     * singleton eager loaded instance
     */
    private static final IgniteFSManager INSTANCE = new IgniteFSManager();

    /**
     * Gateway into Ignite file system implementation.
     * Provides methods for regular file system operations such as create, delete, mkdir.
     */
    private IgniteFileSystem fs;

    public IgniteFSManager() {
        initFS();
    }

    public static IgniteFSManager getInstance() {
        return INSTANCE;
    }

    private void initFS() {
        fs = ignite.fileSystem("myFileSystem");
    }

    /**
     * creates in-memory directory
     */
    public void createDir() {
        // Create directory.
        IgfsPath dir = new IgfsPath("/myDir");

        fs.mkdirs(dir);
    }

    /**
     * deletes in-memory directory
     */
    public void deleteDir() {
        // Create directory.
        IgfsPath dir = new IgfsPath("/myDir");

        // Delete directory.
        fs.delete(dir, true);
    }

    /**
     * creates in-memory file and appends line.
     */
    public void createFile() {
        IgfsPath dir = new IgfsPath("/myDir");

        // Create file and write some data to it.
        IgfsPath file = new IgfsPath(dir, "myFile");

        try (OutputStream out = fs.create(file, true)) {
            byte[] b = {'h', 'e', 'l', 'l', 'o'};
            out.write(b, 0, 5);
        } catch (IOException e) {
            System.out.println("Exception occurred." + e);
        }
    }

    /**
     * reads from in-memory file
     */
    public void readFile() {
        IgfsPath dir = new IgfsPath("/myDir");

        // Create file and write some data to it.
        IgfsPath file = new IgfsPath(dir, "myFile");

        byte[] buffer = new byte[5];
        // Read from file.
        try (InputStream in = fs.open(file)) {
            in.read(buffer);

            // for each byte in the buffer
            System.out.print("Contents in in memory file: ");
            for(byte b:buffer) {

                // convert byte to character
                char c = (char)b;

                // prints character
                System.out.print(c);
            }
            System.out.println();
        } catch (IOException e) {
            System.out.println("Exception occurred." + e);
        }
    }

}
