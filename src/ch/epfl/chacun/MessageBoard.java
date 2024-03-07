package ch.epfl.chacun;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.epfl.chacun.Points.*;

public record MessageBoard(TextMaker textMaker, List<Message> messages) {
    public MessageBoard(TextMaker textMaker, List<Message> messages) {
        messages = List.copyOf(messages);
    }

    public Map<PlayerColor, Integer> points() {
       Map<PlayerColor, Integer> scoarerMap = new HashMap<>();
         for (Message message : messages) {
              for (PlayerColor playerColor : message.scorers) {
                scoarerMap.put(playerColor, message.points);
              }
         }
         return scoarerMap;
    }

    MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        if (forest.isOccupied()) {
            int addPoints = points().get(forest.occupants());
            String addText = textMaker.playersScoredForest(forest.majorityOccupants(), addPoints, forest.mushroomGroupCount(forest), forest.zones().size());
            messages.add(new Message(addText, addPoints, forest.majorityOccupants(), forest.tileIds()));
            return this;
        }
        return this;
    }

    MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        String addText = textMaker.playerClosedForestWithMenhir(player);
        int addPoints = points().get(forest.occupants());
        messages.add(new Message(addText, addPoints, forest.majorityOccupants(), forest.tileIds()));
        return this;
    }

    MessageBoard withScoredRiver(Area<Zone.River> river) {
        if (river.isOccupied()) {
            int addPoints = points().get(river.riverFishCount(river));
            String addText = textMaker.playersScoredRiver(river.majorityOccupants(), addPoints, river.riverFishCount(river), river.zones().size());
            messages.add(new Message(addText, addPoints, river.majorityOccupants(), river.tileIds()));
            return this;
        }
        return this;
    }
    



    record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tiledIds) {
        public Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tiledIds) {
            if (points < 0) {
                throw new IllegalArgumentException();
            }
            scorers = Set.copyOf(scorers);
            tiledIds = Set.copyOf(tiledIds);

        }

    }
}
