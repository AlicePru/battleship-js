package lv.ctco.javaschool.game.entity;

import lombok.Data;
import lv.ctco.javaschool.auth.entity.domain.User;

import javax.persistence.*;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    private User player1;
    private boolean player1Active;
    private boolean player1Winner;


    @ManyToOne
    private User player2;
    private boolean player2Active;
    private boolean player2Winner;

    @Column
    private int moves;
    private String winner;


    @Enumerated(EnumType.STRING)
    private GameStatus status;

    public boolean isPlayerActive(User player) {
        if (player.equals(player1)) {
            return player1Active;
        } else if (player.equals(player2)) {
            return player2Active;
        } else {
            throw new IllegalArgumentException();
        }
    }


    public void setPlayerActive(User player, Boolean active) {
        if (player.equals(player1)) {
            player1Active = active;
        } else if (player.equals(player2)) {
            player2Active = active;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public User getEnemy(User player) {
        if (player.equals(player1)) {
            return player2;
        } else if (player.equals(player2)) {
            return player1;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
