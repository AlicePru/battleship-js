package lv.ctco.javaschool.game.entity;

import lombok.Data;

@Data
public class UserDto {
    private Long gameId;
    private Long userID;
    private Integer move;

}
