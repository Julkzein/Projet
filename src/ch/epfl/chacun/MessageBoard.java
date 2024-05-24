package ch.epfl.chacun;


import java.util.*;

import static ch.epfl.chacun.Animal.Kind.*;
import static ch.epfl.chacun.Points.*;

/**
 * Represent the messages that will be sent by the game at the different
 * said events. The nested record represents the messages displayed on the
 * messageBoard that will be updated everytime one of the methods is called.
 *
 * @param textMaker the interface that creates the different messages displayed on the
 *                  messageBoard
 * @param messages  the messages that will be displayed on the messageBoard
 * @Author Louis Bernard (379724)
 * @Author Jules Delforge (372325)
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    /**
     * Compact constructor that guaranties the immutability
     */
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * This method returns a list of messages identical to the current one except that it contains a new message
     *
     * @param m the message we want to add to the MessageBoard
     * @return a list of messages with the additional message
     */
    private List<Message> messagesWithNewMessage(Message m) {
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
            int mushroomGroupCount = Area.mushroomGroupCount(forest);
            int forestSize = forest.zones().size();
            int addPoints = forClosedForest(forestSize, mushroomGroupCount);
            Set<PlayerColor> majorityOccupants = forest.majorityOccupants();
            String addText = textMaker.playersScoredForest(majorityOccupants, addPoints, mushroomGroupCount, forestSize);
            return messageBoardWithNewMessage(new Message(addText, addPoints, majorityOccupants, forest.tileIds()));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one but with a new message
     * indicating that the given player may play a second time as he closed a forest containing
     * one or more menhir
     *
     * @param player the player that closed of the given forest
     * @param forest the forest closed by the given player
     * @return the new MessageBoard with the additional message
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        String addText = textMaker.playerClosedForestWithMenhir(player);
        return messageBoardWithNewMessage(new Message(addText, 0, Set.of(), forest.tileIds()));
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
            int riverFishCount = Area.riverFishCount(river);
            int riverSize = river.zones().size();
            int addPoints = riverFishCount + riverSize;
            Set<PlayerColor> majorityOccupants = river.majorityOccupants();
            String addText = textMaker.playersScoredRiver(majorityOccupants, addPoints, riverFishCount, riverSize);
            return messageBoardWithNewMessage(new Message(addText, addPoints, majorityOccupants, river.tileIds()));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one except if the placing of
     * the given Hunting Trap by the given player gained points in which case it will add a message
     * indicating the points given by the Hunting Trap placed by the player
     *
     * @param scorer         the player that placed the trap
     * @param adjacentMeadow all meadows in the range of the Hunting Trap
     * @param cancelledAnimals the animals that must not be counted in the point calculations
     * @return the MessageBoard with the possible new message
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = Area.animals(adjacentMeadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalMap = getAnimalMap(animals);
        for (Animal animal : animals) {
            if (animalMap.get(TIGER) == 0) {
                break;
            } else if (animal.kind() == DEER) {
                animalMap.put(DEER, animalMap.get(DEER) - 1);
                animalMap.put(TIGER, animalMap.get(TIGER) - 1);
            }
        }
        int points = pointsForMeadow(animalMap);
        if (points > 0) {
            String addText = textMaker.playerScoredHuntingTrap(scorer, points, animalMap);
            return messageBoardWithNewMessage(new Message(addText, points, Set.of(scorer), adjacentMeadow.tileIds()));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one but with an additional
     * message indicating that the given player obtained a certain amount of points by placing
     * the logboat on the river system
     *
     * @param scorer      the player that placed the logboat
     * @param riverSystem the river system on which the player placed the logboat
     * @return the MessageBoard with the new message
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        int lakeCount = Area.lakeCount(riverSystem);
        int points = forLogboat(lakeCount);
        String addText = textMaker.playerScoredLogboat(scorer, points, lakeCount);
        return messageBoardWithNewMessage(new Message(addText, points, Set.of(scorer), riverSystem.tileIds()));
    }


    /**
     * This method returns a MessageBoard identical to the current one except if the given meadow is
     * occupied and if it sums up to a positive amount of points in which case it will add a message indicating that its majority occupant have won
     * the amount of points associated to its closing
     *
     * @param meadow           the given meadow we want to check if closed
     * @param cancelledAnimals the animals that must not be counted in the points
     * @return the MessageBoard with the possible additional message
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        if (meadow.isOccupied()) {
            Set<Animal> animals = meadow.animals(meadow, cancelledAnimals);
            Map<Animal.Kind, Integer> animalMap = getAnimalMap(animals);
            int points = pointsForMeadow(animalMap);
            if (points > 0) {
                Set<PlayerColor> scorers = meadow.majorityOccupants();
                String addText = textMaker.playersScoredMeadow(scorers, points, animalMap);
                return messageBoardWithNewMessage(new Message(addText, points, scorers, meadow.tileIds()));
            }
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one except if the given river system
     * is occupied and that the points it gives is superior to 0 in which case it will add a new message
     * indicating that the majority occupant players have won a certain amount of points
     *
     * @param riverSystem the river system we want to check
     * @return the MessageBoard with the possible new message
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()) {
            int points = Area.riverSystemFishCount(riverSystem);
            if (points > 0) {
                Set<PlayerColor> scorers = riverSystem.majorityOccupants();
                String addText = textMaker.playersScoredRiverSystem(scorers, points, points);
                return messageBoardWithNewMessage(new Message(addText, points, scorers, riverSystem.tileIds()));
            }
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one unless the given meadow which contains the
     * pit trap is occupied and that it gives an amount of points to its majority occupants superior to 0 in which
     * case it will add a new message indicating that the majority occupants have gained a certain amount of points
     *
     * @param adjacentMeadow   the meadows in the range of the pit trap
     * @param cancelledAnimals the animals that must not be counted int the point calculations
     * @return the MessageBoard with the potential additional message
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        if (adjacentMeadow.isOccupied()) {
            Set<Animal> animals = adjacentMeadow.animals(adjacentMeadow, cancelledAnimals);
            Map<Animal.Kind, Integer> animalMap = getAnimalMap(animals);
            int points = pointsForMeadow(animalMap);
            if (points > 0) {
                Set<PlayerColor> scorers = adjacentMeadow.majorityOccupants();
                String addText = textMaker.playersScoredPitTrap(scorers, points, animalMap);
                return messageBoardWithNewMessage(new Message(addText, points, scorers, adjacentMeadow.tileIds()));
            }
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one except if the given river system is occupied
     * in which case it will add a message indicating that its majority occupants have won the corresponding points
     *
     * @param riverSystem the river system we desire to check
     * @return the MessageBoard with the possible additional message
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()) {
            int lakeCount = Area.lakeCount(riverSystem);
            int points = forRaft(lakeCount);
            Set<PlayerColor> scorers = riverSystem.majorityOccupants();
            String addText = textMaker.playersScoredRaft(scorers, points, lakeCount);
            return messageBoardWithNewMessage(new Message(addText, points, scorers, riverSystem.tileIds()));
        }
        return this;
    }

    /**
     * This method returns a MessageBoard identical to the current one but with an additional message indicating
     * that the given winners have won the game with the given amount of points
     *
     * @param winners the players who won
     * @param points  the amount of points with which the players have won
     * @return the MessageBoard with the possible additional message
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        return messageBoardWithNewMessage(new Message(textMaker.playersWon(winners, points), 0, Set.of(), Set.of()));
    }

    /**
     * This method returns a MessageBoard identical to the current one but with an additional message
     *
     * @param message the message we want to add to the MessageBoard
     * @return the MessageBoard with the additional message
     */
    private MessageBoard messageBoardWithNewMessage(Message message) {
        return new MessageBoard(textMaker, messagesWithNewMessage(message));
    }

    /**
     * This method returns a map that associates to all animal kinds the amount of animals of that kind
     *
     * @param animals the set of animals we want to count
     * @return the map that associates to all animal kinds the amount of animals of that kind
     */
    private Map<Animal.Kind, Integer> getAnimalMap(Set<Animal> animals) {
        Map<Animal.Kind, Integer> animalMap = new HashMap<>();
        for (Animal.Kind kind : Animal.Kind.values()) {
            animalMap.put(kind, 0);
        }
        for (Animal animal : animals) {
            animalMap.put(animal.kind(), animalMap.get(animal.kind()) + 1);
        }
        return animalMap;
    }

    private int pointsForMeadow(Map<Animal.Kind, Integer> animalMap) {
        return forMeadow(animalMap.get(MAMMOTH), animalMap.get(AUROCHS), animalMap.get(DEER));
    }


    /**
     * This nested record represents a message that will be displayed on the MessageBoard
     *
     * @param text    the text of the message
     * @param points  the points associated to the message
     * @param scorers the set of players that have won the given points
     * @param tileIds the ids of the concerned tiles or an empty set if the message does not concern any tile
     */
    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {
        /**
         * Compact constructor that guaranties the immutability
         * @param points  the points associated to the message
         * @param scorers the set of players that have won the given points
         * @param tileIds the ids of the concerned tiles or an empty set if the message does not concern any tile
         * @throws NullPointerException if the text is null
         */
        public Message {
            Preconditions.checkArgument(points >= 0);
            if (text == null) throw new NullPointerException();
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}
