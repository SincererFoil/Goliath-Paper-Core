package ch.mcserver.goliathPaperCore.common.pluginmessage;

import ch.mcserver.goliathPaperCore.module.anticheat.inspection.gui.SusGui;
import ch.mcserver.goliathPaperCore.module.anticheat.inspection.player.SuspectEntry;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class GoliathSusMessenger implements PluginMessageListener {

    public static final String CHANNEL = "goliath:anticheat:sus";

    private final Plugin plugin;
    private final SusGui susGui;
    private final Gson gson = new Gson();

    public GoliathSusMessenger(Plugin plugin, SusGui susGui) {
        this.plugin = plugin;
        this.susGui = susGui;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!CHANNEL.equals(channel)) {
            return;
        }

        try {
            ByteArrayDataInput input = ByteStreams.newDataInput(message);
            String subchannel = input.readUTF();

            if (subchannel.equals("OPEN")) {
                handleOpen(player, input);
            }
        } catch (RuntimeException exception) {
            plugin.getLogger().log(Level.WARNING, "Could not read anticheat sus plugin message.", exception
            );
        }
    }

    private void handleOpen(Player messageCarrier, ByteArrayDataInput input) {
        UUID staffUuid = UUID.fromString(input.readUTF());

        if (!messageCarrier.getUniqueId().equals(staffUuid)) {
            plugin.getLogger().warning("Rejected sus message because the message carrier was not the staff player.");
            return;
        }

        String suspectsJson = input.readUTF();

        Type listType = TypeToken
                .getParameterized(List.class, SuspectEntry.class)
                .getType();

        List<SuspectEntry> suspects = gson.fromJson(suspectsJson, listType);

        if (suspects == null) {
            suspects = List.of();
        }

        susGui.open(messageCarrier, suspects);
    }
}