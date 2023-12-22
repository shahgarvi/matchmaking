package com.matchmaking.player.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Data
public class Player implements Serializable {
    private static final long serialVersionUID = -232111726415873888L;
    private String id;
    private String username;
    private int skillLevel;

    // Constructors, getters, setters

    public Player(String id, String username, int skillLevel) {
        this.id = id;
        this.username = username;
        this.skillLevel = skillLevel;
    }

    // Equals and hashCode methods (to be used in matching logic)

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

