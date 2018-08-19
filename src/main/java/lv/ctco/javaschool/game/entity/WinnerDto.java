package lv.ctco.javaschool.game.entity;

import lombok.Data;

@Data
public class WinnerDto {
    private Long gameId;
    private String username;
    private Integer move;

}
