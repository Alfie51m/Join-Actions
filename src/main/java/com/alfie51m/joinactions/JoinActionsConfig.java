package com.alfie51m.joinactions;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class JoinActionsConfig {

    private static final String FILE_NAME = "join-actions.properties";

    public static String[] servers = new String[4];
    public static String[] commands = new String[4];

    static {
        for (int i = 0; i < 4; i++) {
            servers[i] = "";
            commands[i] = "";
        }
    }

    public static void load() {
        Path configFile = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(FILE_NAME);

        Properties props = new Properties();

        if (Files.exists(configFile)) {
            try (var reader = Files.newBufferedReader(configFile)) {
                props.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 4; i++) {
            servers[i] = props.getProperty("server" + (i + 1), "").toLowerCase();
            commands[i] = props.getProperty("command" + (i + 1), "");
        }
    }

    public static void save() {
        Path configFile = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(FILE_NAME);

        Properties props = new Properties();

        for (int i = 0; i < 4; i++) {
            props.setProperty("server" + (i + 1), servers[i]);
            props.setProperty("command" + (i + 1), commands[i]);
        }

        try {
            Files.createDirectories(configFile.getParent());
            try (var writer = Files.newBufferedWriter(configFile)) {
                props.store(writer, "Join Actions Config");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
