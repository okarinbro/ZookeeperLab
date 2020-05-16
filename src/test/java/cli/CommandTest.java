package cli;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandTest {
    protected ZooKeeper zooKeeper;
    protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    protected final PrintStream originalOut = System.out;

    @Before
    public void setUp() throws KeeperException, InterruptedException {
        zooKeeper = mock(ZooKeeper.class);
        when(zooKeeper.getChildren("/z", false)).thenReturn(Arrays.asList("1", "2"));
        when(zooKeeper.getChildren("/z/1", false)).thenReturn(Collections.singletonList("11"));
        when(zooKeeper.getChildren("/z/2", false)).thenReturn(Collections.emptyList());
        when(zooKeeper.getChildren("/z/1/11", false)).thenReturn(Collections.emptyList());
        when(zooKeeper.exists("/z", false)).thenReturn(new Stat(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

}