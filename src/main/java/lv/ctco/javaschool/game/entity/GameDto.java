package lv.ctco.javaschool.game.entity;

import lombok.Data;
import lv.ctco.javaschool.auth.entity.domain.User;

@Data
public class GameDto {

    private GameStatus status;
    private boolean playerActive;
}
