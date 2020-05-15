import cli.Terminal;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher;

public class AppRunner {
    private final static Logger logger = Logger.getLogger(AppRunner.class);

    public static void main(String[] args) {


        if (args.length < 2) {
            logger.error("Expected arguments: 127.0.0.1:2181 ./script.sh [params]");
            return;
        }
        logger.info("Application running..");


        String connectionString = args[0];
        String[] applicationParams = new String[args.length - 1];
        System.arraycopy(args, 1, applicationParams, 0, applicationParams.length);
        try {
            String znode = "/z";
            NodeWatcher watcher = new NodeWatcher(connectionString, znode, applicationParams);
            new Thread(watcher).start();
            new Terminal(watcher.getZk(), znode).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
