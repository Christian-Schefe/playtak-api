package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tak.Database;
import tak.IRCBridge;
import tak.Player;
import tak.Settings;
import tak.utils.BadWordFilter;

import java.util.Map;

@SpringBootApplication
public class Main {
    public static int portws = 8080;

    public static void main(String[] args) {
        Settings.parse();
        Database.initConnection();
        BadWordFilter.loadConfigs();
        Player.loadFromDB();

        IRCBridge.init();

        SpringApplication app = new SpringApplication(Main.class);

        app.setDefaultProperties(Map.of("server.port", Integer.toString(portws)));
        System.out.println("Starting WebSocket server on port " + portws);
        app.run(args);
    }
}
