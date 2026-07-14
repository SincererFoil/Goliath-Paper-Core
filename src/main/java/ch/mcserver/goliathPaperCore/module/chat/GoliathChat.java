package ch.mcserver.goliathPaperCore.module.chat;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import com.google.gson.Gson;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import static ch.mcserver.goliathPaperCore.GoliathPaperCore.serverName;

public class GoliathChat implements Listener {

    private static final String CHANNEL = "goliath:chat:global";

    private final GoliathPaperCore plugin;
    private final Gson gson = new Gson();
    private Jedis subscriberConnection;

    public GoliathChat(GoliathPaperCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        String plainMessage = PlainTextComponentSerializer.plainText()
                .serialize(event.message());

        sendChatMessage(player, plainMessage);

    }

    public void sendChatMessage(Player player, String message) {
        ChatMessage chatMessage = new ChatMessage(
                player.getUniqueId(),
                player.getName(),
                serverName,
                message,
                System.currentTimeMillis()
        );

        GoliathPaperCore.getChatLogRepository().createEvent(player.getUniqueId(), serverName, message, player.getName());

        String json = gson.toJson(chatMessage);

        try (Jedis jedis = plugin.getRedisManager().getConnection()) {
            jedis.publish(CHANNEL, json);
        }
    }

    public void startChatSubscriber() {
        subscriberConnection = plugin.getRedisManager().getConnection();

        Thread thread = new Thread(() -> {
            subscriberConnection.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Component component = Component.text()
                                .append(Component.text("<"))
                                .append(Component.text(chatMessage.name()))
                                .append(Component.text("> "))
                                .append(Component.text(chatMessage.message()))
                                .build();

                        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(component));
                    });
                }
            }, CHANNEL);
        }, "Goliath-Chat-Subscriber");

        thread.setDaemon(true);
        thread.start();
    }

    public void stopChatSubscriber() {
        if (subscriberConnection != null) {
            subscriberConnection.close();
        }
    }
}