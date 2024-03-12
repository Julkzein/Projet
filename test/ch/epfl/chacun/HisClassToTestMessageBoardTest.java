package ch.epfl.chacun;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class HisClassToTestMessageBoardTest implements TextMaker {

    @Override
    public String playerName(PlayerColor playerColor) {
        return new StringJoiner(" ")
                .add("the")
                .add("name")
                .add("of")
                .add("the")
                .add("player")
                .add("is")
                .add(playerColor.toString())
                .toString();
    }

    @Override
    public String points(int points) {
        return new StringJoiner(" ")
                .add(String.valueOf(points))
                .add("points")
                .add("scored")
                .toString();
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return new StringJoiner(" ")
                .add(player.toString())
                .add("closed")
                .add("forest")
                .add("with")
                .add("menhir")
                .toString();
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(mushroomGroupCount))
                .add(String.valueOf(tileCount))
                .toString();
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .add(String.valueOf(tileCount))
                .toString();
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return new StringJoiner(" ")
                .add(scorer.toString())
                .add(String.valueOf(points))
                .add(animals.toString())
                .toString();
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return new StringJoiner(" ")
                .add(scorer.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .toString();
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(animals.toString())
                .toString();
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .toString();
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(animals.toString())
                .toString();
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return new StringJoiner(" ")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .toString();
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return new StringJoiner(" ")
                .add(winners.toString())
                .add(String.valueOf(points))
                .toString();
    }

    @Override
    public String clickToOccupy() {
        return new StringJoiner(" ")
                .add("click")
                .add("to")
                .add("occupy")
                .toString();
    }

    @Override
    public String clickToUnoccupy() {
        return new StringJoiner(" ")
                .add("click")
                .add("to")
                .add("unoccupy")
                .toString();
    }
}
