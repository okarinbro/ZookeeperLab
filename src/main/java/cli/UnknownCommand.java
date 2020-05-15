package cli;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

public class UnknownCommand extends Command {
    private final static Logger logger = Logger.getLogger(TreePrinter.class);

    public UnknownCommand(ZooKeeper zooKeeper) {
        super(zooKeeper);
    }

    @Override
    void execute() {
        logger.info("Command unknown. Type 'help' for more.");
    }

    @Override
    String help() {
        return null;
    }
}
