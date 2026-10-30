package ch.mcserver.goliathPaperCore.module.sector;

import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

public class SectorTransferService {

    private final RedisManager redisManager;

    private final Gson gson = new Gson();

    private static final String CHANNEL = "goliath:sector:transfer:request";

    public SectorTransferService(RedisManager redisManager) {
        this.redisManager = redisManager;
    }


    public boolean publish(SectorTransferData data) {

        String json = gson.toJson(data);

        try (Jedis jedis = redisManager.getConnection()) {
            boolean success = jedis.publish(CHANNEL, json) > 0;
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
