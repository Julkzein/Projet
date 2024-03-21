package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OurBoardTest {
    private final Zone.Forest forest1 = new Zone.Forest(562, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest2 = new Zone.Forest(9, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest3 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest4 = new Zone.Forest(9, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest5 = new Zone.Forest(274, Zone.Forest.Kind.PLAIN);
    private final Zone.Meadow meadow4 = new Zone.Meadow(171, List.of(), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(173, new ArrayList<>(
            new ArrayList<>(List.of(new Animal(66, Animal.Kind.DEER)))
    ), null);
    private final Zone.Meadow meadow7 = new Zone.Meadow(176, new ArrayList<>(
            List.of(new Animal(13, Animal.Kind.TIGER))), null);
    private final Zone.Meadow meadow6 = new Zone.Meadow(1, new ArrayList<>(), null);
    private final Zone.Meadow meadow8 = new Zone.Meadow(561, new ArrayList<>(List.of(
            new Animal(20, Animal.Kind.AUROCHS))), null);
    private final Zone.Meadow meadow9 = new Zone.Meadow(563, new ArrayList<>(
            List.of()), null);
    private final Zone.Meadow meadow10 = new Zone.Meadow(271, new ArrayList<>(), null);
    private final Zone.Meadow meadow11 = new Zone.Meadow(273, new ArrayList<>(
            List.of(new Animal(10, Animal.Kind.DEER))), null);
    private final Zone.Lake lake6 = new Zone.Lake(3, 0, null);
    private final Zone.Lake lake7 = new Zone.Lake(3, 1, null);
    private final Zone.Lake lake8 = new Zone.Lake(3, 0, null);
    private final Zone.Lake lake9 = new Zone.Lake(3, 0, Zone.SpecialPower.LOGBOAT);
    private final Zone.River river1 = new Zone.River(172, 0, null);
    private final Zone.River river2 = new Zone.River(174, 0, null);
    private final Zone.River river3 = new Zone.River(564, 0, null);
    private final Zone.River river5 = new Zone.River(272, 0, null);
    private final TileSide[] sides17 = new TileSide[] {
            new TileSide.River(meadow4, river1, meadow5), new TileSide.River(meadow5, river1, meadow4),
            new TileSide.River(meadow4, river2, meadow7), new TileSide.River(meadow7, river2, meadow4)
    };
    private final TileSide[] sides56 = new TileSide[] {
            new TileSide.Meadow(meadow8), new TileSide.Forest(forest1), new TileSide.Forest(forest1),
            new TileSide.River(meadow9, river3, meadow8)
    };
    private final TileSide[] sides27 = new TileSide[] {
            new TileSide.Meadow(meadow10), new TileSide.River(meadow10, river5, meadow11),
            new TileSide.River(meadow11, river5, meadow10), new TileSide.Forest(forest5)
    };
    private final Tile UnaddableTile = new Tile(21, Tile.Kind.NORMAL, sides27[0], sides27[1], sides27[2], sides27[3]);
    private final Tile tile17 = new Tile(17, Tile.Kind.NORMAL, sides17[0], sides17[1], sides17[2], sides17[3]);
    private final Tile tile56 = new Tile(56, Tile.Kind.START, sides56[0], sides56[1], sides56[2], sides56[3]);
    private final Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sides27[0], sides27[1], sides27[2], sides27[3]);
    private final PlacedTile placedTile56 = new PlacedTile(tile56, null, Rotation.NONE, new Pos(0, 0), null);
    private final PlacedTile UnaddablePlacedTile = new PlacedTile(UnaddableTile, PlayerColor.RED, Rotation.NONE, new Pos(1, 4), null);
    private final PlacedTile placedTile17 = new PlacedTile(tile17, PlayerColor.RED, Rotation.NONE, new Pos(-1, 0), null);
    private final PlacedTile placedTile27 = new PlacedTile(tile27, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0), null);
    private final PlacedTile[] placedTiles = new PlacedTile[625]; {{
            placedTiles[311] = placedTile17;
            placedTiles[312] = placedTile56;
            placedTiles[313] = placedTile27;
    }}

    private final int[] id = new int[625]; {{
            id[311] = 12;
            id[312] = 56;
            id[313] = 21;
    }}

    private final Board board1 = Board.EMPTY;
    private final Board board2 = Board.EMPTY;

    @Test
    void tileAt() {
        assertEquals(Tile.Kind.START, Objects.requireNonNull(board1.withNewTile(placedTile56).tileAt(new Pos(0, 0))).tile().kind());
    }
    @Test
    void tileAtIsCorrectlyDefined() {
        assertEquals(Tile.Kind.START, board1.withNewTile(placedTile56).tileAt(new Pos(0, 0)).tile().kind());

        assertEquals(placedTile56, board1.withNewTile(placedTile56).tileWithId(56));
        assertEquals(UnaddablePlacedTile, board1.withNewTile(UnaddablePlacedTile).tileWithId(21));
        assertEquals(placedTile17, board1.withNewTile(placedTile17).tileWithId(17));

        assertNull(board1.withNewTile(placedTile56).tileAt(new Pos(1, 4)));
        assertNull(board2.withNewTile(UnaddablePlacedTile).tileAt(new Pos(0, 0)));
    }

    @Test
    void tileWithIdIsCorrectlyDefined() {
        assertEquals(placedTile56, board1.withNewTile(placedTile56).tileWithId(56));
        assertEquals(UnaddablePlacedTile, board1.withNewTile(UnaddablePlacedTile).tileWithId(21));
        assertEquals(placedTile17, board1.withNewTile(placedTile17).tileWithId(17));

        assertThrows(IllegalArgumentException.class, () -> board1.tileWithId(60));
        assertThrows(IllegalArgumentException.class, () -> board2.tileWithId(0));
    }

    @Test
    void cancelledAnimalsIsCorrectlyDefined() {
        assertEquals(0, board1.canceledAnimal().size());
        assertEquals(0, board2.canceledAnimal().size());

        Set<Animal> cancelledAnimals = new HashSet<>(Set.of(new Animal(10, Animal.Kind.AUROCHS)));
        Board boardWithCancelledAnimals = board1.withNewTile(placedTile56).withMoreCancelledAnimals(cancelledAnimals);

        assertEquals(cancelledAnimals, boardWithCancelledAnimals.canceledAnimal());

        assertNotEquals(cancelledAnimals, boardWithCancelledAnimals.withMoreCancelledAnimals(
                Set.of(new Animal(20, Animal.Kind.DEER))
        ).canceledAnimal());

        cancelledAnimals.add(new Animal(20, Animal.Kind.DEER));
        assertEquals(cancelledAnimals, boardWithCancelledAnimals.withMoreCancelledAnimals(
                Set.of(new Animal(20, Animal.Kind.DEER))
        ).canceledAnimal());
    }

    @Test
    void withOccupantIsCorrectlyDefined() {
        assertEquals(0, board1.occupants().size());

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 568);
        assertEquals(Set.of(occupant1), board1.withNewTile(placedTile56).withNewTile(placedTile17).withOccupant(occupant1).occupants());

        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, 173);
        assertEquals(Set.of(occupant1, occupant2), board1.withNewTile(placedTile56).withNewTile(placedTile17)
                .withOccupant(occupant1).withOccupant(occupant2).occupants());


        Occupant withoutTileOccupant = new Occupant(Occupant.Kind.PAWN, 561);
        assertThrows(IllegalArgumentException.class, () -> board1.withOccupant(withoutTileOccupant));
    }

    @Test
    void withoutOccupantIsCorrectlyDefined() {
        assertEquals(0, board1.occupants().size());

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 567);
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, 173);

        assertThrows(IllegalArgumentException.class, () -> board1.withoutOccupant(occupant1));
        assertEquals(Set.of(occupant1), board1.withNewTile(placedTile56).withNewTile(placedTile17)
                .withOccupant(occupant1).occupants());

        assertEquals(Set.of(), board1.withNewTile(placedTile56).withNewTile(placedTile17)
                .withOccupant(occupant1).withoutOccupant(occupant1).occupants());
        assertEquals(Set.of(occupant1), board1.withNewTile(placedTile56).withNewTile(placedTile17)
                .withOccupant(occupant1).withOccupant(occupant2).withoutOccupant(occupant2).occupants());
    }
    @Test
    void occupantsIsCorrectlyDefined() {
        assertEquals(0, board1.occupants().size());
        assertEquals(0, board2.occupants().size());

        Board boardWithOccupants = board1.withNewTile(placedTile56).withNewTile(placedTile17);
        Occupant withTileOccupant1 = new Occupant(Occupant.Kind.PAWN, 567);
        Occupant withTileOccupant2 = new Occupant(Occupant.Kind.HUT, 173);
        Occupant withNoTileOccupant = new Occupant(Occupant.Kind.PAWN, 700);

        assertEquals(Set.of(withTileOccupant1), boardWithOccupants.withOccupant(withTileOccupant1).occupants());
        assertEquals(Set.of(), boardWithOccupants.withoutOccupant(withTileOccupant1).occupants());
        assertEquals(Set.of(withTileOccupant1, withTileOccupant2), boardWithOccupants
                .withOccupant(withTileOccupant1).withOccupant(withTileOccupant2).occupants());
    }

    @Test
    void forestAreaIsCorrectlyDefined() {
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).forestArea(forest1));
        assertThrows(IllegalArgumentException.class, () -> board1.forestArea(forest5));

        Area<Zone.Forest> forestArea1 = new Area<>(Set.of(forest1), List.of(), 2);
        Area<Zone.Forest> forestArea2 = new Area<>(Set.of(forest5), List.of(), 1);

        Area<Zone.Forest> forestArea = forestArea1.connectTo(forestArea2);

        assertEquals(forestArea, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest1));
        assertEquals(forestArea, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest5));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest3));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest4));
        assertEquals(forestArea1, board1.withNewTile(placedTile56).forestArea(forest1));
}

    @Test
    void meadowAreaIsCorrectlyDefined() {
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).meadowArea(meadow8));
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).meadowArea(meadow9));
        assertThrows(IllegalArgumentException.class, () -> board1.meadowArea(meadow5));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).meadowArea(meadow4));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile17).meadowArea(meadow10));


        Area<Zone.Meadow> meadowArea1 = new Area<>(Set.of(meadow4), List.of(), 4);
        Area<Zone.Meadow> meadowArea2 = new Area<>(Set.of(meadow5), List.of(), 2);
        Area<Zone.Meadow> meadowArea3 = new Area<>(Set.of(meadow7), List.of(), 2);
        Area<Zone.Meadow> meadowArea4 = new Area<>(Set.of(meadow8), List.of(), 2);
        Area<Zone.Meadow> meadowArea5 = new Area<>(Set.of(meadow9), List.of(), 1);

        Area<Zone.Meadow> meadow1 = meadowArea1.connectTo(meadowArea5);
        Area<Zone.Meadow> meadow2 = meadowArea2.connectTo(meadowArea4);

        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow4).openConnections(),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow9).openConnections());

        board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow4);
        assertEquals(meadow1, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow4));
        assertEquals(meadow1, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow9));

        assertEquals(meadow2, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow5));
        assertEquals(meadowArea3, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow7));

        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow6));
        assertThrows(IllegalArgumentException.class, () ->board1.withNewTile(placedTile56).meadowArea(meadow4));
    }

    @Test
    void riverAreaIsCorrectlyDefined() {
    }

    @Test
    void riverSystemAreaIsCorrectlyDefined() {
    }

    @Test
    void meadowAreasIsCorrectlyDefined() {
    }

    @Test
    void riverSystemAreasIsCorrectlyDefined() {
    }

    @Test
    void adjacentMeadow() {
    }

    @Test
    void occupantCount() {
    }

    @Test
    void insertionPositions() {
    }

    @Test
    void lastPlacedTile() {
    }

    @Test
    void forestsClosedByLastTile() {
    }

    @Test
    void riversClosedByLastTile() {
    }

    @Test
    void canAddTile() {
    }

    @Test
    void couldPlaceTile() {
    }

    @Test
    void withNewTile() {
    }


    @Test
    void withoutGatherersOrFishersIn() {
    }

    @Test
    void withMoreCancelledAnimals() {
//        Set<Animal> cancelledAnimals = Set.of(new Animal(10, Animal.Kind.AUROCHS));
//        Board boardWithCancelledAnimals = board1.withNewTile(placedTile56).withMoreCancelledAnimals(cancelledAnimals);
//        assertEquals(cancelledAnimals, boardWithCancelledAnimals.cancelledAnimals());
//
//
//
//        boardWithCancelledAnimals.withMoreCancelledAnimals(cancelledAnimals);
//        assertEquals(cancelledAnimals, boardWithCancelledAnimals.cancelledAnimals());
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}