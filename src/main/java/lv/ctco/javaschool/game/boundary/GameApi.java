package lv.ctco.javaschool.game.boundary;


import lombok.extern.java.Log;
import lv.ctco.javaschool.auth.control.UserStore;
import lv.ctco.javaschool.auth.entity.domain.User;
import lv.ctco.javaschool.game.control.GameStore;
import lv.ctco.javaschool.game.entity.*;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

@Path("/game")
@Stateless
@Log
public class GameApi {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private UserStore userStore;
    @Inject
    private GameStore gameStore;


    @POST
    @RolesAllowed({"ADMIN", "USER"})
    public void startGame() {
        User currentUser = userStore.getCurrentUser();
        Optional<Game> game = gameStore.getIncompleteGame();

        game.ifPresent(g -> {
            g.setPlayer2(currentUser);
            g.setStatus(GameStatus.PLACEMENT);
            g.setPlayer1Active(true);
            g.setPlayer2Active(true);
        });

        if (!game.isPresent()) {
            Game newGame = new Game();
            newGame.setPlayer1(currentUser);
            newGame.setStatus(GameStatus.INCOMPLETE);
            em.persist(newGame);
        }
    }

    @POST
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/cells")
    public void setShips(JsonObject field) {
        User currentUser = userStore.getCurrentUser();
        Optional<Game> game = gameStore.getStartedGameFor(currentUser, GameStatus.PLACEMENT);
        game.ifPresent(g -> {
            if (g.isPlayerActive(currentUser)) {
                List<String> ships = new ArrayList<>();
                for (Map.Entry<String, JsonValue> pair : field.entrySet()) {
                    log.info(pair.getKey() + " - " + pair.getValue());
                    String addr = pair.getKey();
                    String value = ((JsonString) pair.getValue()).getString();
                    if ("SHIP".equals(value)) {
                        ships.add(addr);
                    }

                }
                gameStore.setShips(g, currentUser, false, ships);
                g.setPlayerActive(currentUser, false);
                if (!g.isPlayer1Active() && !g.isPlayer2Active()) {
                    g.setStatus(GameStatus.STARTED);
                    g.setPlayer1Active(true);
                    g.setPlayer2Active(false);
                }

            }
        });
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/status")
    public GameDto getGameStatus() {
        User currentUser = userStore.getCurrentUser();
        Optional<Game> game = gameStore.getOpenGameFor(currentUser);
        return game.map(g -> {
            GameDto dto = new GameDto();
            dto.setStatus(g.getStatus());
            dto.setPlayerActive(g.isPlayerActive(currentUser));
            return dto;
        }).orElseThrow(IllegalStateException::new);
    }

    @POST
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/fire/{address}")
    public void doFire(@PathParam("address") String address) {
        log.info("Firing to " + address);
        User currentUser = userStore.getCurrentUser();
        Optional<Game> game = gameStore.getOpenGameFor(currentUser);
        game.ifPresent(g -> {
            User enemy = g.getEnemy(currentUser);
            Optional<Cell> cell = gameStore.findCell(g, enemy, address, false);
            Integer sink = 0;
            if (cell.isPresent()) {
                Cell c = cell.get();
                if (c.getState() != CellState.HIT) {
                    c.setState(CellState.HIT);
                    gameStore.setCellState(g, currentUser, address, true, CellState.HIT);
                    log.info(CellState.HIT + address);
                    sink++;
                }

            } else {
                gameStore.setCellState(g, enemy, address, false, CellState.MISS);
                gameStore.setCellState(g, currentUser, address, true, CellState.MISS);
                log.info(CellState.MISS + address);

            }

            if(sink==16){

            }

            boolean p1a = g.isPlayer1Active();
            g.setPlayer1Active(!p1a);
            g.setPlayer2Active(p1a);
        });
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/placement")
    public List<CellStateDto> getShipsPlacement() {
        User currentUser = userStore.getCurrentUser();
        Optional<Game> game = gameStore.getStartedGameFor(currentUser, GameStatus.STARTED);
        return game.map(g -> {
            List<Cell> cells = gameStore.getCells(g, currentUser);
            return cells.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }).orElseThrow(IllegalStateException::new);
    }

    private CellStateDto convertToDto(Cell cell) {
        CellStateDto dto = new CellStateDto();
        dto.setTargetArea(cell.isTargetArea());
        dto.setAddress(cell.getAddress());
        dto.setState(cell.getState());
        return dto;
    }

//    @GET
//    @RolesAllowed({"ADMIN", "USER"})
//    @Path("/sink")
//    public void getSinkShips(){
//        User currentUser = userStore.getCurrentUser();
//        Optional<Game> game = gameStore.getStartedGameFor(currentUser, GameStatus.STARTED);
//        return game.map(g -> {
//            List<Cell> cells = gameStore.getCells(g, currentUser);
//        Integer sink=0;
//        while (sink!=16)
//        {
//           if()
//        }
//
//    }

}


