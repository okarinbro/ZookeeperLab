package cli;

import org.apache.zookeeper.ZooKeeper;

public abstract class Command {
    protected ZooKeeper zooKeeper;

    public Command(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    abstract void execute();

    abstract String help();
}
