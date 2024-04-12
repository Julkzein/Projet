package ch.epfl.chacun;

import java.util.*;
import java.util.List;
import static java.lang.StringTemplate.STR;

/**
 * This class creates the texts,  in French, for the different events of the game.
 *
 * @author Louis Bernard (379724)
 * @author Jules Delforge (372325)
 */
public final class TextMakerFr implements TextMaker{
    private final Map<PlayerColor, String> playerMap = new HashMap<>(); // Map of the players' names and their colors

    /**
     * This method orders the players in a string.
     *
     * @param players the set of players
     * @return the string with the players' names
     */
    private String orderPlayer(Set<PlayerColor> players){
        List<PlayerColor> playersList = PlayerColor.ALL.stream().filter(players::contains).toList();
        StringJoiner orderedPlayersString = new StringJoiner(", ", "", "");
        for (int i = 0; i < playersList.size() - 1; i++) {
            orderedPlayersString.add(STR."\{playerMap.get(playersList.get(i))}");
        }
        return STR."\{orderedPlayersString.toString()} et \{playerMap.get(playersList.getLast())}";
    }

    /**
     * This method orders the animals with their number in a string.
     *
     * @param animals the map of the animals
     * @return the string with the animals' names and their number
     */
    public String orderAnimal(Map<Animal.Kind, Integer> animals) {
        List<Animal.Kind> animalList = Arrays.stream(Animal.Kind.values()).filter(animals::containsKey).toList();
        Map<Animal.Kind, String> frName = Map.of(Animal.Kind.DEER, "cerf", Animal.Kind.AUROCHS, "aurochs", Animal.Kind.MAMMOTH, "mammouth", Animal.Kind.TIGER, "tigre");
        StringJoiner orderedPlayersString = new StringJoiner(", ", "", "");
        List<Integer> animalIntList = animalList.stream().map(animals::get).toList();
        for (int i = 0; i < animalList.size() - 1; i++) {
            orderedPlayersString.add(STR."\{animalIntList.get(i)} \{frName.get(animalList.get(i))}\{frName.get(animalList.get(i)).equals("aurochs") ? "" : plural(animalIntList.get(i) > 1)}");
        }
        if (animals.keySet().size() > 1) {
            return STR."\{orderedPlayersString.toString()} et \{animalIntList.getLast()} \{frName.get(animalList.getLast())}\{frName.get(animalList.getLast()).equals("aurochs") ? "" : plural(animalIntList.getLast() > 1)}.";
        }
        return STR."\{animalIntList.getLast()} \{frName.get(animalList.getLast())}\{frName.get(animalList.getLast()).equals("aurochs") ? "" : plural(animalIntList.getLast() > 1)}.";
    }

    /**
     * This method returns "s" if the condition is true.
     *
     * @param condition the condition
     * @return "s" if the condition is true
     */
    private String plural(boolean condition) {
        return STR."\{condition ? "s" : "" }";
    }


    /**
     * This method returns "·s" if the condition is true.
     *
     * @param condition the condition
     * @return "·s" if the condition is true
     */
    private String pluralScorer(boolean condition) {
        return STR."\{condition ? "·s" : "" }";
    }

    /**
     * This method returns the verb in the plural form and the points won if the condition is true.
     *
     * @param players the set of players
     * @param points the number of points
     * @return the verb in the plural form and the points won if the condition is true
     */
    private String pluralStartSentence(Set<PlayerColor> players, int points) {
        return STR."\{orderPlayer(players)} \{players.size() > 1 ? "ont remporté" : "a remporté"} \{points(points)} en tant qu'occupant·e\{pluralScorer(players.size() > 1)} majoritaire\{plural(players.size() > 1)}";
    }

    /**
     * This method returns the name of the player associated to the given color.
     *
     * @param playerColor the color of the player we want the name of
     * @return the name of the player associated to the given color.
     */
    @Override
    public String playerName(PlayerColor playerColor) {
        return playerMap.get(playerColor);
    }

    /**
     * This method returns the number of points in a string.
     *
     * @param points the number of points
     * @return the string with the number of points
     */
    @Override
    public String points(int points) {
        return STR."\{points} point\{plural(points > 1)}";
    }

    /**
     * This method returns the text of a message declaring that a player has closed a forest with a menhir.
     *
     * @param player the player who closed the forest
     * @return the string with the message
     */
    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{player} a fermé une forêt contenant un menhir et peut donc placeer une tuile menhir.";
    }

    /**
     * This method returns the text of a message declaring that the majority occupants of a newly closed forest,
     * consisting of a certain number of tiles and containing a certain number of mushroom groups, have won the corresponding points.
     *
     * @param scorers the majority occupants of the forest
     * @param points the points won
     * @param mushroomGroupCount the number of mushroom groups in the forest
     * @param tileCount the number of tiles that make up the forest
     * @return the string with the message
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String mushroomString = mushroomGroupCount > 0 ? STR." et de \{mushroomGroupCount} groupe\{plural(mushroomGroupCount > 1)} de champignons" : "";
        return STR."\{pluralStartSentence(scorers, points)} d'une forêt composée de \{tileCount} tuiles\{mushroomString}.";
    }

    /**
     * This method returns the text of a message declaring that the majority occupants of a newly closed river,
     * consisting of a certain number of tiles and containing a certain number of fish, have won the corresponding points.
     *
     * @param scorers the majority occupants of the river
     * @param points the points won
     * @param fishCount the number of fish swimming in the river or adjacent lakes
     * @param tileCount the number of tiles that make up the river
     * @return the string with the message
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String fishString = fishCount > 0 ? STR." et de \{fishCount} poisson\{plural(fishCount > 1)}" : "";
        return STR."\{pluralStartSentence(scorers, points)} d'une rivière composée de \{tileCount} tuile\{plural(tileCount > 1)}\{fishString}.";
    }

    /**
     * This method returns the text of a message declaring that a player has placed the hunting trap in a meadow containing,
     * on the 8 neighboring tiles of the trap, certain animals, and won the corresponding points.
     *
     * @param scorer the player who placed the hunting trap
     * @param points the points won
     * @param animals the set of animals present in the range of the hunting trap
     * @return the string with the message
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{scorer} a remporté \{points(points)} en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{orderAnimal(animals)}.";
    }

    /**
     * This method returns the text of a message declaring that a player has placed the logboat in a river system containing a certain number of lakes, and won the corresponding points.
     *
     * @param scorer the player who placed the logboat
     * @param points the points won
     * @param lakeCount the number of lakes in th erange of the logboat
     * @return the string with the message
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{scorer} a remporté \{points(points)} en  plaçant la pirogue dans un réseau hydrographique contenant \{lakeCount} lac\{lakeCount > 1 ? "s" : ""}.";
    }

    /**
     * This method returns the text of a message declaring that the majority occupants of a meadow containing certain animals have won the corresponding points.
     *
     * @param scorers the majority occupants of the meadow
     * @param points the points won
     * @param animals the set of animals present in the meadow
     * @return the string with the message
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{pluralStartSentence(scorers, points)} d'un pré contenant \{orderAnimal(animals)}.";
    }

    /**
     * This method returns the text of a message declaring that the majority occupants of a river system containing a certain number of fish have won the corresponding points.
     *
     * @param scorers the majority occupants of the river system
     * @param points the points won
     * @param fishCount the number of fish in the river system
     * @return the string with the message
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{pluralStartSentence(scorers, points)} d'un réseau hydrographique contenant \{fishCount} poisson\{plural(fishCount > 1)}.";
    }

    /**
     * This method returns the text of a message declaring that the majority occupants of a meadow containing the pit trap and, on the 8 neighboring tiles of it, certain animals, have won the corresponding points.
     *
     * @param scorers the majority occupants of the meadow containing the pit trap
     * @param points the points won
     * @param animals the set of animals present on the tiles neighboring the pit trap
     * @return the string with the message
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{pluralStartSentence(scorers, points)} d'un pré contenant la grande fosse à pieux entourée de \{orderAnimal(animals)}.";
    }

    /**
     * This method returns the text of a message declaring that the majority occupants of a river system containing the raft have won the corresponding points.
     *
     * @param scorers the majority occupants of the river system containing the raft
     * @param points the points won
     * @param lakeCount the number of lakes in the river system
     * @return the string with the message
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{pluralStartSentence(scorers, points)} d'un réseau hydrographique contenant le radeau et \{lakeCount} lac\{plural(lakeCount > 1)}.";
    }

    /**
     * This method returns the text of a message declaring that the players have won with the corresponding points.
     *
     * @param winners the set of players who won
     * @param points the points of the winner(s)
     * @return the string with the message
     */
   @Override
   public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{orderPlayer(winners)} \{winners.size() > 1 ? "ont remporté" : "a remporté"} avec \{points(points)}.";
   }

   /**
    * This method returns the text of a message asking the current player to click on the occupant it wants to place or the
    * message if he doesn't want to place any occupant.
    *
    * @return the string with the message
    */
   @Override
    public String clickToOccupy() {
        return "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
   }

   /**
    * This method returns the text of a message asking the current player to click on the pawn it wants to take back or the
    * message if he doesn't want to take back any pawn.
    *
    * @return the string with the message
    */
   @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
   }
}
