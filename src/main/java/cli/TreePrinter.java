package cli;

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
        StringBuilder stringBuilder = new StringBuilder();
        printTree(znode, 0, stringBuilder);
        System.out.println(stringBuilder.toString());
    }

    @Override
    String help() {
        return "'tree' - provides graphical representation of Z nodes tree";
    }

    private void printTree(String node, int indent, StringBuilder sb) {
        printNode(node, indent, sb);
        try {
            zooKeeper.getChildren(node, false).forEach(child ->
                    printTree(node.concat("/" + child), indent + 1, sb));
        } catch (KeeperException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private void printNode(String node, int indent, StringBuilder sb) {
        String name = node.substring(node.lastIndexOf("/"));

        sb.append(getIndentString(indent));
        sb.append("├──");
        sb.append(name);
        sb.append("\n");
    }

    private String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("│  ");
        }
        return sb.toString();
    }
}
