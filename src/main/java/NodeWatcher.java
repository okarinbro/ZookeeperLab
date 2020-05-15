import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NodeWatcher implements Watcher, Runnable, AutoCloseable {

    private final static Logger logger = Logger.getLogger(NodeWatcher.class);
    private final String znode;
    private final String[] applicationParams;
    private final NodesCounter counter;
    private boolean running = true;
    private final ZooKeeper zk;
    private Process externalApp;
    private final Lock lock = new ReentrantLock(true);

    public NodeWatcher(String connectionString, String znode, String[] applicationParams) throws IOException {
        this.znode = znode;
        this.applicationParams = applicationParams;
        this.zk = new ZooKeeper(connectionString, 3000, this);
        this.counter = new NodesCounter();
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                init();
                while (running) {
                    zk.exists(znode, this);
                    wait();
                }
            }
        } catch (InterruptedException | KeeperException e) {
            logger.error("Error due to: " + e.getMessage());
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        lock.lock();
        if (watchedEvent.getType() == Event.EventType.NodeCreated) {
            onNodeChange(watchedEvent);
        } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            onNodeDeleted(watchedEvent);
        } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            onChildChange(watchedEvent);
        }
        notifyAll();
        lock.unlock();
    }

    private void init() throws KeeperException, InterruptedException {
        if (zk.exists(znode, this) != null) {
            runExternalApp();
            logger.info("Descendants: " + counter.getDescendantsCount(this, zk, znode));
        }
    }

    private void onNodeChange(WatchedEvent watchedEvent) {
        try {
            if (watchedEvent.getPath().equals(znode)) {
                runExternalApp();
            }
            zk.getChildren(watchedEvent.getPath(), this);
            logger.info("Descendants: " + counter.getDescendantsCount(this, zk, znode));
        } catch (KeeperException | InterruptedException e) {
            logger.error("Error occurred:" + e.getMessage());
        }
    }

    private void onChildChange(WatchedEvent watchedEvent) {
        logger.info("Descendants: " + counter.getDescendantsCount(this, zk, znode));
        try {
            String path = watchedEvent.getPath();
            List<String> children = zk.getChildren(path, this);
            for (String child : children) {
                String childPath = path + "/" + child;
                zk.exists(childPath, this);
                zk.getChildren(childPath, this);
            }
        } catch (KeeperException ignored) {
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage());
        }
    }

    private void onNodeDeleted(WatchedEvent watchedEvent) {
        if (watchedEvent.getPath().equals(znode)) {
            killExternalApp();
        }
    }

    private void runExternalApp() {
        try {
            if (this.externalApp == null) {
                logger.info("External app running..");
                this.externalApp = Runtime.getRuntime().exec(applicationParams);
            }
        } catch (IOException e) {
            logger.info("Error occurred: " + e.getMessage());
        }
    }

    private void killExternalApp() {
        if (!Objects.isNull(this.externalApp)) {
            logger.info("Destroying external app..");
            this.externalApp.destroy();
            try {
                this.externalApp.waitFor();
                this.externalApp = null;
            } catch (InterruptedException e) {
                logger.info("Could n't close external app: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        this.running = false;
        this.killExternalApp();
        synchronized (this) {
            notifyAll();
        }
    }

    public synchronized ZooKeeper getZk() {
        return zk;
    }
}