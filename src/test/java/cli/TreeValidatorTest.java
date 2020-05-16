package cli;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TreeValidatorTest {
    @Test
    public void shouldCheckWhetherTreeIsInvalid() throws KeeperException, InterruptedException {
        ZooKeeper zooKeeper = mock(ZooKeeper.class);
        when(zooKeeper.exists("/z", false)).thenReturn(null);

        boolean isValid = TreeValidator.isTreeValid(zooKeeper, "/z");

        assertFalse(isValid);
    }

    @Test
    public void shouldCheckWhetherTreeIsValid() throws KeeperException, InterruptedException {
        ZooKeeper zooKeeper = mock(ZooKeeper.class);
        when(zooKeeper.exists("/z", false)).thenReturn(new Stat());

        boolean isValid = TreeValidator.isTreeValid(zooKeeper, "/z");

        assertTrue(isValid);
    }
}