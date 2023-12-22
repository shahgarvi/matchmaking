package com.matchmaking.player.Controller;

import com.matchmaking.player.Service.MatchMakingService;
import com.matchmaking.player.Service.PlayerService;
import com.matchmaking.player.domain.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/matchmaking")
public class MatchMakingController {
    @Autowired
    private MatchMakingService matchmakingService;
    @Autowired
    private PlayerService playerService;

    @PostMapping("/join")
    public ResponseEntity joinGame(@RequestBody Player player) {
        // ... initiate matchmaking process
        log.info("Matchmaking in progress. Please wait.");

        Player opponent = matchmakingService.registerPlayer(player);
        return ResponseEntity.ok(opponent);
    }
}

