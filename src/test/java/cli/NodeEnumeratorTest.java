package cli;

import com.google.common.collect.Lists;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeEnumeratorTest extends CommandTest{

    @Test
    public void shouldPrintTreeInPreorderManner() {
        NodeEnumerator nodeEnumerator = new NodeEnumerator(zooKeeper, "/z");
        nodeEnumerator.execute();
        assertTrue(outContent.toString().contains("/z"));
        assertTrue(outContent.toString().contains("/z/1"));
        assertTrue(outContent.toString().contains("/z/1/11"));
        assertTrue(outContent.toString().contains("/z/2"));
        assertEquals(outContent.toString().split("\n").length, 5);
    }

    @Test
    public void shouldCountNodes() {
        NodeEnumerator nodeEnumerator = new NodeEnumerator(zooKeeper, "/z");
        nodeEnumerator.execute();
        assertTrue(outContent.toString().contains("Total nodes count: 4"));
    }

}