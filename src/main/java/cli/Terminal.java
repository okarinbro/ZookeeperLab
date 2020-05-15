package cli;

import com.google.common.collect.ImmutableMap;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Terminal implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(Terminal.class);
    private final ZooKeeper zooKeeper;
    private String znode;

    public Terminal(ZooKeeper zooKeeper, String znode) {
        this.zooKeeper = zooKeeper;
        this.znode = znode;
    }

    @Override
    public void run() {
        Map<String, Command> commandMap = new ImmutableMap.Builder<String, Command>()
                .put("tree", new TreePrinter(zooKeeper, znode))
                .build();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type 'help' for more");
        while (true) {
            String in = getInput(reader);
            if (in.equals("help")) {
                commandMap.values().forEach(command -> System.out.println(command.help()));
            } else if (in.equals("quit")) {
                break;
            } else {
                commandMap.getOrDefault(in, new UnknownCommand(zooKeeper)).execute();
            }


        }
    }

    private String getInput(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return "";
        }
    }
}
