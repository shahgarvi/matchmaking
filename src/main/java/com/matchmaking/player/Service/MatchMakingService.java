package com.matchmaking.player.Service;

import com.matchmaking.player.domain.Player;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MatchMakingService {
    @Autowired
    private PlayerService playerService;

    public static final String MATCHMAKING_QUEUE = "matchmakingQueue";

    @Autowired
    private AmqpTemplate amqpTemplate;

    public Player registerPlayer(Player player) {
        playerService.savePlayer(player);
        return initiateMatchmaking(player);
    }

    public Player initiateMatchmaking(Player currentPlayer) {

        List<Player> potentialOpponents = playerService.findPotentialOpponents(currentPlayer);

        if (!potentialOpponents.isEmpty()) {
            // Select an opponent (for simplicity, a random opponent is chosen here)
            Player opponent = selectOpponentBySkillLevel(currentPlayer, potentialOpponents);

            // Notify players about the match
            notifyMatch(currentPlayer, opponent);
            return opponent;
        } else {
            // If no opponent is found, enqueue for later matching
            enqueueForMatching(currentPlayer.getId());
            return null;
        }
    }

    private void enqueueForMatching(String playerId) {
        amqpTemplate.convertAndSend(MATCHMAKING_QUEUE, playerId);
    }

    private Player notifyMatch(Player player1, Player player2) {
        amqpTemplate.convertAndSend("resultQueue", "Match Result: " + player1.getId() + " vs. " + player2.getId());
        return player2;
    }

    private Player selectOpponentBySkillLevel(Player currentPlayer, List<Player> potentialOpponents) {
        // Select an opponent with a similar skill level
        int currentPlayerSkill = currentPlayer.getSkillLevel();

        Player bestMatch = null;
        int minSkillDifference = Integer.MAX_VALUE;

        for (Player opponent : potentialOpponents) {
            int skillDifference = Math.abs(opponent.getSkillLevel() - currentPlayerSkill);
            if (skillDifference < minSkillDifference) {
                bestMatch = opponent;
                minSkillDifference = skillDifference;
            }
        }

        return bestMatch;
    }

}

