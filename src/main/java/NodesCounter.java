import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.Deque;
import java.util.LinkedList;

public class NodesCounter {
    private final static Logger logger = Logger.getLogger(NodesCounter.class);

    public int getDescendantsCount(Watcher watcher, ZooKeeper zk, String znode) {
        int result = 0;
        String currentNode;
        try {
            if (zk.exists(znode, watcher) != null) {
                Deque<String> queue = new LinkedList<>();
                queue.add(znode);
                while (!queue.isEmpty()) {
                    currentNode = queue.pop();
                    result += traverseDescendants(watcher, zk, currentNode, queue);
                }
            }
        } catch (KeeperException | InterruptedException e) {
            logger.error("Error during counting descendants of z node: " + e.getMessage());
        }
        return result;
    }

    private int traverseDescendants(Watcher watcher, ZooKeeper zk, String currentNode, Deque<String> queue) {
        int counter = 0;
        try {
            for (String child : zk.getChildren(currentNode, watcher)) {
                queue.add(currentNode + "/" + child);
                counter++;
            }
        } catch (KeeperException ignored) {
        } catch (Exception e) {
            logger.error("Error during traversal: " + e.getMessage());
        }
        return counter;
    }
}
