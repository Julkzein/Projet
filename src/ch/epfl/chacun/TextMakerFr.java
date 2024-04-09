package ch.epfl.chacun;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.StringTemplate.STR;

public class TextMakerFr implements TextMaker{

    //private order()
    public Map<PlayerColor, String> playerMap = new HashMap<>();

    @Override
    public String playerName(PlayerColor playerColor) {
        return playerMap.get(playerColor);
    }

    @Override
    public String points(int points) {
        return STR."\{points} points";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{player} a fermé une forêt contenant un menhir et peut donc placeer une tuile menhir.";
    }

    @Override
    public String playerScoreForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        PlayerColor[] s = (PlayerColor[]) scorers.toArray();
        if (scorers.size() == 1 && mushroomGroupCount == 0) {
            return STR."\{playerMap.get(s[0])} a remporté \{points(points)} en tant qu'occupant·e majoritaire d'une forêt composée de \{tileCount} tuiles.";
        } else if (scorers.size() > 1 && mushroomGroupCount == 0) {
            //String scorerstri
            return  STR."\{} ont remporté \{points(points)} en tant qu'occupant·e·s majoritaires d'une forêt composée de \{tileCount} tuiles.";
        } else if (scorers.size() == 1 && mushroomGroupCount > 0) {
            return  STR."\{} ont remporté \{points(points)} en tant qu'occupant·e·s majoritaires d'une forêt composée de \{tileCount} tuiles et \{mushroomGroupCount} groupe de champignons.";
        }

    }

}
