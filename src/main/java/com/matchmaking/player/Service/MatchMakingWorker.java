package com.matchmaking.player.Service;

import com.matchmaking.player.domain.Player;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchMakingWorker {

    @Autowired
    private MatchMakingService matchmakingService;

    @Autowired
    private PlayerService playerService;

    @RabbitListener(queues = MatchMakingService.MATCHMAKING_QUEUE)
    public void processMatchmakingRequest(String playerId) {
        Player player = playerService.getPlayerById(playerId);
        matchmakingService.initiateMatchmaking(player);
    }

    @RabbitListener(queues = "resultQueue")
    public void processMatchResult(String message) {
        System.out.println("Received from resultQueue: " + message);
        String[] resultParts = message.split(" vs. ");

        if (resultParts.length == 2) {
            String player1Id = resultParts[0];
            String player2Id = resultParts[1];

            handleMatchResult(player1Id, player2Id);
        } else {
            System.err.println("Unexpected message format: " + message);
        }
    }

    private void handleMatchResult(String player1Id, String player2Id) {
        System.out.println("Handling Match Result: " + player1Id + " vs. " + player2Id);
    }
}

