package cli;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.Objects;

public class TreeValidator {
    private final static Logger logger = Logger.getLogger(TreeValidator.class);

    static boolean isTreeInvalid(ZooKeeper zooKeeper, String znode) {
        try {
            if (Objects.isNull(zooKeeper.exists(znode, false))) {
                return true;
            }
        } catch (KeeperException | InterruptedException e) {
            logger.warn(String.format("Error occured. Reason: %s", e.getMessage()));
        }
        return false;
    }
}
