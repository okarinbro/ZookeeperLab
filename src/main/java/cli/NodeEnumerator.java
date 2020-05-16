package cli;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class NodeEnumerator extends Command {
    private final static Logger logger = Logger.getLogger(NodeEnumerator.class);
    private final String znode;

    public NodeEnumerator(ZooKeeper zooKeeper, String znode) {
        super(zooKeeper);
        this.znode = znode;
    }

    @Override
    void execute() {
        if (!TreeValidator.isTreeValid(zooKeeper, znode)) {
            logger.warn(String.format("%s node couldn't be found.", znode));
            return;
        }
        int counter = showTree(znode);
        System.out.println("Total nodes count: " + counter);
    }

    private int showTree(String node) {
        int counter = 1;
        try {
            System.out.println(node);
            for (String child : zooKeeper.getChildren(node, false)) {
                counter += showTree(node.concat("/" + child));
            }
        } catch (KeeperException | InterruptedException e) {
            logger.error("Error occured: " + e.getMessage());
        }
        return counter;
    }

    @Override
    String help() {
        return "'traverse' - prints tree in preorder manner";
    }
}
