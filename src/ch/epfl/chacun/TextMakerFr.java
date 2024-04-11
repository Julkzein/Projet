package ch.epfl.chacun;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.StringTemplate.STR;

public class TextMakerFr implements TextMaker{

    public Map<PlayerColor, String> playerMap = new HashMap<>();

    private String orderPlayer(Set<PlayerColor> players){
        List playersList = new ArrayList();
        playersList.addAll(players);
        Collections.sort(playersList);
        String orderedPlayersString = "";
        for (int i = 0; i < playersList.size() - 1; i++) {
            orderedPlayersString = STR."\{orderedPlayersString} , \{playersList.get(i).toString()}";
        }
        return STR."\{orderedPlayersString} et \{playersList.getLast().toString()}";
    }

    private String orderAnimal(Map<Animal.Kind, Integer> animals) {
        List animalList = new ArrayList<>();
        animalList.addAll(animals.keySet());
        Collections.sort(animalList);
        String orderedPlayersString = "";
        for (Object a : animalList) {
            if (a == animalList.getLast()) {
                orderedPlayersString = STR."\{orderedPlayersString} et \{animals.get(a)} \{a.toString()}\{plural(animals.get(a) > 0)}";
            } else {
                orderedPlayersString = STR."\{orderedPlayersString}, \{animals.get(a)} \{a.toString()}";
            }
        }
        return STR."\{orderedPlayersString}.";
    }

    private String plural(boolean condition) {
        return STR."\{condition ? "s" : "" }";
    }

    private String pluralScorer(boolean condition) {
        return STR."\{condition ? "·s" : "" }";
    }

    private String pluralVerb(Set<PlayerColor> players, int points) {
        return STR."\{orderPlayer(players)} \{players.size() > 1 ? "a remporté" : "ont remporté"} \{points(points)}";
    }



    @Override
    public String playerName(PlayerColor playerColor) {
        return playerMap.get(playerColor);
    }

    @Override
    public String points(int points) {
        return STR."\{points} point\{plural(points > 0)}";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{player} a fermé une forêt contenant un menhir et peut donc placeer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return STR."\{pluralVerb(scorers, points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'une forêt composée de \{tileCount} tuiles \{mushroomGroupCount > 0 ? "et de " + mushroomGroupCount + " groupe" + plural(mushroomGroupCount > 1) +" de champignons" : ""}.";
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String s = "";
        if (fishCount > 1) { s = "s"; }
        return STR."\{pluralVerb(scorers, points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'une rivière composée de \{tileCount} tuiles \{fishCount > 0 ? "et de " + fishCount + " poisson" + plural(fishCount > 1) : ""}.";
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{scorer} a remporté \{points(points)} en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{orderAnimal(animals)}";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{scorer} a remporté \{points(points)} en  plaçant la pirogue dans un réseau hydrographique contenant \{lakeCount} lac\{lakeCount > 1 ? "s" : ""}.";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{pluralVerb(scorers, points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'un pré contenant \{orderAnimal(animals)}";
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{pluralVerb(scorers, points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'un réseau hydrographique contenant \{fishCount} poisson\{plural(fishCount > 1)}";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{pluralVerb(scorers, points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'un pré contenant la grande fosse à pieux entourée de \{orderAnimal(animals)}";
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{pluralVerb(scorers, points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'un réseau hydrographique contenant le radeau et \{lakeCount} lac\{plural(lakeCount > 1)}";
    }

   @Override
   public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{orderPlayer(winners)} \{winners.size() > 1 ? "ont remporté" : "a remporté"} avec \{points(points)}";
   }

   @Override
    public String clickToOccupy() {
        return "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
   }

   @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
   }
}
