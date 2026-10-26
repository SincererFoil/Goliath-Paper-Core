package ch.mcserver.goliathPaperCore.module.anticheat.modguard;

import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ModGuardScanService {

    private final Map<String, String> languageKeyMap = new HashMap<>() {{
        put("key.meteor-client.open-gui", "Meteor");
        put("key.meteor-client.open-commands", "Meteor");
        put("key.category.meteor-client.meteor-client", "Meteor");
        put("key.freecam.toggle", "Freecam");
        put("itemscroller.gui.button.config_gui.generic", "Item Scroller");
    }};

    private final Plugin plugin;

    private final RedisManager redisManager;

    public String fallback = UUID.randomUUID().toString();

    public Map<UUID, Integer> currentBatch = new HashMap<>();

    public Map<UUID, List<List<String>>> playerBatches = new HashMap<>();

    private final Map<UUID, BukkitTask> timeoutTasks = new HashMap<>();

    private final Map<UUID, Vector3i> signPositions = new HashMap<>();

    public ModGuardScanService(Plugin plugin, RedisManager redisManager) {
        this.plugin = plugin;
        this.redisManager = redisManager;
    }

    public void startScan(Player player) {

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            executeScan(player);
        }, 40L);
    }

    private void executeScan(Player player) {

        if (currentBatch.containsKey(player.getUniqueId())) {
            return;
        }

        List<String> batch = new ArrayList<>();
        List<List<String>> batches = new ArrayList<>();
        int count = 0;

        for (String key : languageKeyMap.keySet()) {
            count++;
            batch.add(key);

            if (count % 4 == 0) {
                batches.add(new ArrayList<>(batch));
                batch.clear();
            }
        }

        if (count % 4 != 0) {
            batches.add(new ArrayList<>(batch));
        }

        UUID uuid = player.getUniqueId();

        playerBatches.put(uuid, batches);
        currentBatch.put(uuid, 0);

        executeNextBatch(player);
    }

    private void executeNextBatch(Player player) {

        UUID uuid = player.getUniqueId();

        List<List<String>> batches = playerBatches.get(uuid);
        Integer index = currentBatch.get(uuid);

        if (batches == null || index == null || !player.isOnline()) {
            removeFakeSign(player);
            currentBatch.remove(uuid);
            playerBatches.remove(uuid);
            return;
        }

        if (index >= batches.size()) {
            removeFakeSign(player);
            currentBatch.remove(uuid);
            playerBatches.remove(uuid);
            signPositions.remove(uuid);
            return;
        }

        executeBatch(player, batches.get(index));

        BukkitTask timeout = Bukkit.getScheduler().runTaskLater(plugin, () -> {

            if (!Objects.equals(currentBatch.get(uuid), index)) {
                return;
            }

            timeoutTasks.remove(uuid);
            removeFakeSign(player);
            currentBatch.remove(player.getUniqueId());
            playerBatches.remove(player.getUniqueId());


        }, 100L);

        timeoutTasks.put(uuid, timeout);
    }

    private void executeBatch(Player player, List<String> keys) {
        Vector3i position = new Vector3i(
        player.getLocation().getBlockX(),
        player.getLocation().getBlockY() - 3,
        player.getLocation().getBlockZ()
);

signPositions.put(player.getUniqueId(), position);

        NBTCompound nbt = new NBTCompound();
        NBTCompound frontText = new NBTCompound();
        NBTList<NBTCompound> messages = NBTList.createCompoundList();

        for (String key : keys) {
            NBTCompound component = new NBTCompound();

            component.setTag("translate", new NBTString(key));
            component.setTag("fallback", new NBTString(fallback));

            messages.addTag(component);
        }

        while (messages.size() < 4) {
            NBTCompound emptyLine = new NBTCompound();
            emptyLine.setTag("text", new NBTString(""));

            messages.addTag(emptyLine);
        }

        frontText.setTag("messages", messages);

        frontText.setTag("color", new NBTString("black"));

        nbt.setTag("front_text", frontText);

        WrapperPlayServerBlockChange blockChangePacket = new WrapperPlayServerBlockChange(position, StateTypes.OAK_SIGN.createBlockState());

        WrapperPlayServerOpenSignEditor signPacket = new WrapperPlayServerOpenSignEditor(position, true);

        WrapperPlayServerBlockEntityData entityDataPacket = new WrapperPlayServerBlockEntityData(position, BlockEntityTypes.SIGN, nbt);


        PacketEvents.getAPI().getPlayerManager().sendPacket(player, blockChangePacket);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, entityDataPacket);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, signPacket);

        PacketEvents.getAPI().getPlayerManager().sendPacket(
                player, new WrapperPlayServerCloseWindow(0)
        );

    }

    private void removeFakeSign(Player player) {
        Vector3i position = signPositions.remove(player.getUniqueId());

        if (position == null || !player.isOnline()) {
            return;
        }

        org.bukkit.block.Block block = player.getWorld().getBlockAt(
                position.getX(),
                position.getY(),
                position.getZ()
        );

        player.sendBlockChange(block.getLocation(), block.getBlockData());
    }

    public void handleSignResponse(Player player, Vector3i position, String[] lines) {

        if (!currentBatch.containsKey(player.getUniqueId())) {
            return;
        }

        if (!position.equals(signPositions.get(player.getUniqueId()))) {
            return;
        }

        UUID uuid = player.getUniqueId();

        BukkitTask timeout = timeoutTasks.remove(uuid);

        if (timeout == null) {
            return;
        }

        timeout.cancel();

        int newBatch = currentBatch.get(player.getUniqueId()) + 1;

        int index = currentBatch.get(player.getUniqueId());
        List<String> currentKeys = playerBatches.get(player.getUniqueId()).get(index);

        for (int i = 0; i < currentKeys.size() && i < lines.length; i++) {
            String line = lines[i];

            String languageKey = currentKeys.get(i);
            String modName = languageKeyMap.get(languageKey);

            if (line != null && !line.isEmpty() && !line.equals(fallback)) {


                System.out.println("POSSIBLE MOD FLAGGED: " + modName);

            } else {
                System.out.println("NO DETECTION FOR: " + modName);
            }

        }
        removeFakeSign(player);
        currentBatch.put(player.getUniqueId(), newBatch);
        executeNextBatch(player);
    }

}
