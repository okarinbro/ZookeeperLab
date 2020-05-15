import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher;

public class AppRunner {
    private final static Logger logger = Logger.getLogger(AppRunner.class);

    public static void main(String[] args) {


        if (args.length < 2) {
            System.err
                    .println("Specify connectionString and application to be executed, eg. 127.0.0.1:2171 mspaint");
            return;
        }
        logger.info("Application running..");


        String connectionString = args[0];
        String[] applicationParams = new String[args.length - 1];
        System.arraycopy(args, 1, applicationParams, 0, applicationParams.length);
        try {
            NodeWatcher watcher = new NodeWatcher(connectionString, "/z", applicationParams);
            new Thread(watcher).start();
            logger.info("Main thread started");
//            handleInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
