package cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreePrinterTest extends CommandTest {

    @Test
    public void shouldPrintCorrectTree() {
        TreePrinter treePrinter = new TreePrinter(zooKeeper, "/z");

        treePrinter.execute();
        assertTrue(outContent.toString().contains("|--/z\n" +
                "|  |--/1\n" +
                "|  |  |--/11\n" +
                "|  |--/2\n"));
        assertEquals(outContent.toString().split("\n").length, 5);
    }
}