package com.matchmaking.player.Service;

import com.matchmaking.player.domain.Player;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String PLAYER_KEY_PREFIX = "demo";

    public void savePlayer(Player player) {
        player.setId(generateRandomId());
        String playerKey = PLAYER_KEY_PREFIX + player.getId();
        redisTemplate.opsForValue().set(playerKey, player);
    }

    private Player get(String playerKey) {
        return (Player) redisTemplate.opsForValue().get(PLAYER_KEY_PREFIX+playerKey);
    }

    public List<Player> getAllPlayers() {
        Set<String> keys = getAllKeys();
        keys = keys.stream().map(e -> e.substring(7)).collect(Collectors.toSet());
        // Fetch values corresponding to the keys
        List<Player> players = redisTemplate.opsForValue().multiGet(keys);
        return players;
    }

    public Player getPlayerById(String playerId) {
        return get(playerId);
    }

    public List<Player> findPotentialOpponents(Player currentPlayer) {
        int currentSkillLevel = currentPlayer.getSkillLevel();

        List<Player> potentialOpponents = new ArrayList<>();

        for (Player player : getAllPlayers()) {
            if (!player.equals(currentPlayer) && Math.abs(player.getSkillLevel() - currentSkillLevel) <= 100) {
                potentialOpponents.add(player);
            }
        }
        return potentialOpponents;
    }

    public static String generateRandomId() {
        UUID uuid = UUID.randomUUID();
        String randomId = uuid.toString().replaceAll("-", "");
        return randomId;
    }

    public Set<String> getAllKeys() {
        return (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();
            String pattern = "*demo*";
            ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();

            try (Cursor<byte[]> cursor = connection.scan(scanOptions)) {
                while (cursor.hasNext()) {
                    byte[] keyBytes = cursor.next();
                    String key = new String(keyBytes, StandardCharsets.UTF_8);
                    keys.add(key);
                }
            }

            return keys;
        });
    }
}

