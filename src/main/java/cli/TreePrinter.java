package cli;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class TreePrinter extends Command {
    private final static Logger logger = Logger.getLogger(TreePrinter.class);
    private final String znode;

    public TreePrinter(ZooKeeper zooKeeper, String znode) {
        super(zooKeeper);
        this.znode = znode;
    }

    @Override
    void execute() {
        if (!TreeValidator.isTreeValid(zooKeeper, znode)) {
            logger.warn(String.format("%s node couldn't be found.", znode));
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        printTree(znode, stringBuilder, 0);
        System.out.println(stringBuilder.toString());
    }

    @Override
    String help() {
        return "'tree' - provides graphical representation of Z nodes tree";
    }

    private void printTree(String node, StringBuilder sb, int indent) {
        try {
            printNode(node, sb, indent);
            for (String child : zooKeeper.getChildren(node, false)) {
                printTree(node.concat("/" + child), sb, indent + 1);
            }
        } catch (KeeperException | InterruptedException e) {
            logger.error("Error occured: " + e.getMessage());
        }
    }

    private void printNode(String node, StringBuilder stringBuilder, int indent) {
        String nodeName = node.substring(node.lastIndexOf("/"));
        stringBuilder.append(getIndent(indent))
                .append("|--")
                .append(nodeName)
                .append("\n");
    }

    private String getIndent(int indent) {
        return Strings.repeat("|  ", indent);
    }
}
