package ch.epfl.chacun;


import java.util.*;

import static ch.epfl.chacun.Points.*;

/**
 * Represent the messages that will be sent by the game at the different
 * said events. The nested record represents the messages displayed on the
 * messageBoard that will be updated everytime one of the methods is called.
 *
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    /**
     * Compact constructor that guaranties the immutability
     *
     * @param textMaker the interface that creates the different messages displayed on the
     *                  messageBoard
     * @param messages the messages that will be displayed on the messageBoard
     */
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * This method returns a new MessageBoard identical to the current one except that it contains a new message
     *
     * @param m the message we want to add to the MessageBoard
     * @return the new MessageBoard with the additional message
     */
    public List<Message> messagesWithNewMessage(Message m) {
        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.add(m);
        return List.copyOf(newMessages);
    }

    /**
     * This method returns a map that associates to all player who scored points at least
     * once the amount of points they currently have
     *
     * @return said map of the type <PlayerColor, Integer>
     */
    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> scoarerMap = new HashMap<>();
        for (Message message : messages) {
            if (message.points > 0) {
                for (PlayerColor playerColor : message.scorers) {
                    scoarerMap.put(playerColor, scoarerMap.getOrDefault(playerColor, 0) + message.points);
                }
            }
        }
        return scoarerMap;
    }
    /**
     * This method returns a MessageBoard identical to the current one except if the given
     * forest is occupied in which case it adds a message to the MessageBoard that indicates that
     * the majority occupant gained the points associated to the closing of said forest
     *
     * @param forest the forest we want to check
     * @return the new MessageBoard with the possible new message
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        if (forest.isOccupied()) {
            int addPoints = forClosedForest(forest.zones().size(), forest.mushroomGroupCount(forest));
            String addText = textMaker.playersScoredForest(forest.majorityOccupants(), addPoints, forest.mushroomGroupCount(forest), forest.zones().size());
            return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, addPoints, forest.majorityOccupants(), forest.tileIds())));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one but with a new message
     * indicating that the given player may play a second time as he colsed a forest containing
     * one or more menhir
     *
     * @param player the player that closed of the given forest
     * @param forest the forest closed by the given player
     * @return the new MessageBoard with the additional message
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        String addText = textMaker.playerClosedForestWithMenhir(player);
        return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, 0, forest.majorityOccupants(), forest.tileIds())));
    }


    /**
     * This method returns a MessageBoard identical to the current one except if the given river is
     * occupied in which case it will add a message indicating that its majority occupant have won
     * the amount of points associated to its closing
     *
     * @param river the given river we want to check if closed
     * @return the MessageBoard with the possible additional message
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        if (river.isOccupied()) {
            int addPoints = Area.riverFishCount(river);
            String addText = textMaker.playersScoredRiver(river.majorityOccupants(), addPoints, river.riverFishCount(river), river.zones().size());
            return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, addPoints, river.majorityOccupants(), river.tileIds())));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one except if the placing of
     * the given Hunting Trap by the given player gained points in which case it will add a message
     * indicating the points given by the Hunting Trap placed by the player
     *
     * @param scorer the player that placed the trapz
     * @param adjacentMeadow all meadows in the range of the Hunting Trap
     * @return the MessageBoard with the possible new message
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        Set<Animal> animals = Area.animals(adjacentMeadow, Set.of());
        Map<Animal.Kind, Integer> animalMap = new HashMap<>();
        for (Animal.Kind kind : Animal.Kind.values()) {
            animalMap.put(kind, 0);
        }
        for (Animal animal : animals) {
            animalMap.put(animal.kind(), animalMap.get(animal.kind()) + 1);
        }
        for (Animal animal : animals) {
           if (animalMap.get(Animal.Kind.TIGER) == 0) {
               break;
           } else if (animal.kind() == Animal.Kind.DEER) {
               animalMap.put(Animal.Kind.DEER, animalMap.get(Animal.Kind.DEER) - 1);
               animalMap.put(Animal.Kind.TIGER, animalMap.get(Animal.Kind.TIGER) - 1);
           }
        }
        int points = forMeadow(animalMap.get(Animal.Kind.MAMMOTH), animalMap.get(Animal.Kind.AUROCHS), animalMap.get(Animal.Kind.DEER));
        if (points > 0) {
            String addText = textMaker.playerScoredHuntingTrap(scorer, points, animalMap);
            return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, points, Set.of(scorer), adjacentMeadow.tileIds())));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one but with an additional
     * message indicating that the given player obtained a certain amount of points by placing
     * the logboat on the river system
     *
     * @param scorer the player that placed the logboat
     * @param riverSystem the river system on which the player placed the logboat
     * @return the MessageBoard with the new message
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        String addText = textMaker.playerScoredLogboat(scorer, forLogboat(riverSystem.lakeCount(riverSystem)), riverSystem.lakeCount(riverSystem));
        return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, forLogboat(riverSystem.lakeCount(riverSystem)), Set.of(scorer), riverSystem.tileIds())));
    }


    /**
     * This method returns a MessageBoard identical to the current one except if the given meadow is
     * occupied and if it sums up to a positive amount of points in which case it will add a message indicating that its majority occupant have won
     * the amount of points associated to its closing
     *
     * @param meadow the given meadow we want to check if closed
     * @param cancelledAnimals the animals that must not be counted in the poins
     * @return the MessageBoard with the possible additional message
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        if (meadow.isOccupied()) {
            Set<Animal> animals = meadow.animals(meadow, cancelledAnimals);
            Map<Animal.Kind, Integer> animalMap = new HashMap<>();
            for (Animal.Kind kind : Animal.Kind.values()) {
                animalMap.put(kind, 0);
            }
            for (Animal animal : animals) {
                animalMap.put(animal.kind(), animalMap.get(animal.kind()) + 1);
            }
            int points = forMeadow(animalMap.get(Animal.Kind.MAMMOTH), animalMap.get(Animal.Kind.AUROCHS), animalMap.get(Animal.Kind.DEER));
            if (points > 0) {
                Set<PlayerColor> scorers = meadow.majorityOccupants();
                String addText = textMaker.playersScoredMeadow(scorers, points, animalMap);
                return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, points, scorers, meadow.tileIds())));
            }
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one except if the given river system
     * is occupied and that the points it gives is superior to 0 in which case it will add a new message
     * indicating that the majority occupant players have won a certain amount of points
     *
     * @param riverSystem the river systel we want to check
     * @return the MessageBoard with the possible new message
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()){
            int points = Area.riverSystemFishCount(riverSystem);
            if (points > 0 ) {
                Set<PlayerColor> scorers = riverSystem.majorityOccupants();
                String addText = textMaker.playersScoredRiverSystem(scorers, points, points);
                return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, points, scorers, riverSystem.tileIds())));
            }
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one unless the given meadow which contains the
     * pit trap is occupied and that it gives an amount of points to its majority occupants superior to 0 in which
     * case it will add a new message indicating that the majority occupants have gained a certain amount of points
     *
     * @param adjacentMeadow the meadows in the range of the pit trap
     * @param cancelledAnimals the animals that must not be counted int the point calculations
     * @return the MessageBoard with the potential additional message
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        if (adjacentMeadow.isOccupied()) {
            Set<Animal> animals = adjacentMeadow.animals(adjacentMeadow, cancelledAnimals);
            Map<Animal.Kind, Integer> animalMap = new HashMap<>();
            for (Animal.Kind kind : Animal.Kind.values()) {
                animalMap.put(kind, 0);
            }
            for (Animal animal : animals) {
                animalMap.put(animal.kind(), animalMap.get(animal.kind()) + 1);
            }
            int points = forMeadow(animalMap.get(Animal.Kind.MAMMOTH), animalMap.get(Animal.Kind.AUROCHS), animalMap.get(Animal.Kind.DEER));
            if (points > 0) {
                Set<PlayerColor> scorers = adjacentMeadow.majorityOccupants();
                String addText = textMaker.playersScoredPitTrap(scorers, points, animalMap);
                return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, points, scorers, adjacentMeadow.tileIds())));
            }
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one except if the given river system is occupied
     * in which case it will add a message indicating that its majority occupants have won the corresponding points
     *
     * @param riverSystem the river systel we desire to check
     * @return the MessageBoard with the possible additional message
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()) {
            int points = forRaft(Area.lakeCount(riverSystem));
            Set<PlayerColor> scorers = riverSystem.majorityOccupants();
            String addText = textMaker.playersScoredRaft(scorers, points, Area.lakeCount(riverSystem));
            return new MessageBoard(textMaker, messagesWithNewMessage(new Message(addText, points, scorers, riverSystem.tileIds())));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one but with an additional message indicating
     * that the given winners have won the game with the given amount of points
     *
     * @param winners the players who won
     * @param points the amount of points with which the players have won
     * @return the MessageBoard with the possible additional message
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        return new MessageBoard(textMaker, messagesWithNewMessage(new Message(textMaker.playersWon(winners, points), points, winners, Set.of())));
    }


    /**
     * This nested record represents a message that will be displayed on the MessageBoard
     * @param text the text of the message
     * @param points the points associated to the message
     * @param scorers the set of players that have won the given points
     * @param tileIds the ids of the concerned tiles or an empty set if the message does not concern any tile
     */
    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {
        public Message {
            Preconditions.checkArgument(points >= 0);
            if (scorers.equals(null)){
                scorers = Set.of();
            } else {
                scorers = Set.copyOf(scorers);
            }
            if (tileIds.equals(null)){
                tileIds = Set.of();
            } else {
                tileIds = Set.copyOf(tileIds);
            }
        }

    }
}
