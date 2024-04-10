package ch.epfl.chacun;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.StringTemplate.STR;

//ENLEVER LE ABSTRACT IL EST LA  POUR LES TESTS
public abstract class TextMakerFr implements TextMaker{

    //private order()
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

    private String pluralVerb(boolean condition) {
        return STR."\{condition ? "a remporté" : "ont remporté" }";
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
        return STR."\{orderPlayer(scorers)} \{pluralVerb(scorers.size() > 1)} \{points(points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'une forêt composée de \{tileCount} tuiles \{mushroomGroupCount > 0 ? "et de " + mushroomGroupCount + " groupe" + plural(mushroomGroupCount > 1) +" de champignons" : ""}.";
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String s = "";
        if (fishCount > 1) { s = "s"; }
        return STR."\{orderPlayer(scorers)} \{pluralVerb(scorers.size() > 1)} \{points(points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'une rivière composée de \{tileCount} tuiles \{fishCount > 0 ? "et de " + fishCount + " poisson" + plural(fishCount > 1) : ""}.";
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
        return STR."\{orderPlayer(scorers)} \{pluralVerb(scorers.size() > 1)} \{points(points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'un pré contenant \{orderAnimal(animals)}";
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{orderPlayer(scorers)} \{pluralVerb(scorers.size() > 1)} \{points(points)} en tant qu'occupant·e\{pluralScorer(scorers.size() > 1)} majoritaire\{plural(scorers.size() > 1)} d'un réseau hydrographique contenant \{fishCount} poisson\{plural(fishCount > 1)}";
    }
}
