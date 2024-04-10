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
                orderedPlayersString = STR."\{orderedPlayersString} et \{animals.get(a)} \{a.toString()}.";
            } else {
                orderedPlayersString = STR."\{orderedPlayersString}, \{animals.get(a)} \{a.toString()}.";
            }
        }
        return orderedPlayersString;
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        return playerMap.get(playerColor);
    }

    @Override
    public String points(int points) {
        return STR."\{points} point\{points > 0 ? "s" : ""}";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{player} a fermé une forêt contenant un menhir et peut donc placeer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return STR."\{orderPlayer(scorers)} \{scorers.size() > 1 ? "a remporté" : "ont remporté"} \{points(points)} en tant qu'occupant·e·\{scorers.size() > 1 ? "s" : ""} majoritaire\{scorers.size() > 1 ? "s" : ""} d'une forêt composée de \{tileCount} tuiles \{mushroomGroupCount > 0 ? "et de " + mushroomGroupCount + " groupe de champignons" : ""}.";
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String s = "";
        if (fishCount > 1) { s = "s"; }
        return STR."\{orderPlayer(scorers)} \{scorers.size() > 1 ? "a remporté" : "ont remporté"} \{points(points)} en tant qu'occupant·e·\{scorers.size() > 1 ? "s" : ""} majoritaire\{scorers.size() > 1 ? "s" : ""} d'une rivière composée de \{tileCount} tuiles \{fishCount > 0 ? "et de " + fishCount + " poisson" + s : ""}.";
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{scorer} a remporté \{points(points)} en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{orderAnimal(animals)}";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{scorer} a remporté \{points(points)} en  plaçant la pirogue dans un réseau hydrographique contenant \{lakeCount} lac\{lakeCount > 1 ? "s" : ""}.";
    }
}
