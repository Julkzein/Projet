package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TextMakerFrTest {


    @Test
    void ordersPlayersCorrectly() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Dalia a fermé une forêt contenant un menhir et peut donc placer une tuile menhir."
                , textMakerFr.playerClosedForestWithMenhir(playerColor1));
    }

    @Test
    void closedForest() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Claude a remporté 6 points en tant qu'occupant·e majoritaire d'une forêt composée de 3 tuiles.",
                textMakerFr.playersScoredForest(Set.of(playerColor2), 6, 0, 3));

        assertEquals("Dalia et Alice ont remporté 9 points en tant qu'occupant·e·s majoritaires d'une forêt composée de 3 tuiles et de 1 groupe de champignons.",
                textMakerFr.playersScoredForest(Set.of(playerColor1, playerColor4), 9, 1, 3));
    }

    @Test
    void closedRiver() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Claude et Bachir ont remporté 3 points en tant qu'occupant·e·s majoritaires d'une rivière composée de 3 tuiles.",
                textMakerFr.playersScoredRiver(Set.of(playerColor2, playerColor3), 3, 0, 3));

        assertEquals("Alice a remporté 8 points en tant qu'occupant·e majoritaire d'une rivière composée de 3 tuiles et contenant 5 poissons.",
                textMakerFr.playersScoredRiver(Set.of(playerColor4), 8, 5, 3));
    }

    @Test
    void huntingTrap() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        var animals = new HashMap<Animal.Kind, Integer>();
        animals.put(Animal.Kind.MAMMOTH, 1);
        animals.put(Animal.Kind.AUROCHS, 2);
        animals.put(Animal.Kind.DEER, 3);

        assertEquals("Bachir a remporté 10 points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de 1 mammouth, 2 aurochs et 3 cerfs.",
                textMakerFr.playerScoredHuntingTrap(playerColor3, 10, animals));
    }

    @Test
    void logboat() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Alice a remporté 8 points en plaçant la pirogue dans un réseau hydrographique contenant 4 lacs.",
                textMakerFr.playerScoredLogboat(playerColor4, 8, 4));
    }

    @Test
    void meadow() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        var animals = new HashMap<Animal.Kind, Integer>();
        animals.put(Animal.Kind.DEER, 1);

        var animals2 = new HashMap<Animal.Kind, Integer>();
        animals2.put(Animal.Kind.MAMMOTH, 1);
        animals2.put(Animal.Kind.DEER, 2);

        assertEquals("Dalia a remporté 1 point en tant qu'occupant·e majoritaire d'un pré contenant 1 cerf.",
                textMakerFr.playersScoredMeadow(Set.of(playerColor1), 1, animals));

        assertEquals("Claude et Bachir ont remporté 5 points en tant qu'occupant·e·s majoritaires d'un pré contenant 1 mammouth et 2 cerfs.",
                textMakerFr.playersScoredMeadow(Set.of(playerColor2, playerColor3), 5, animals2));
    }

    @Test
    void riverSystem() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Alice a remporté 9 points en tant qu'occupant·e majoritaire d'un réseau hydrographique contenant 9 poissons.",
                textMakerFr.playersScoredRiverSystem(Set.of(playerColor4), 9, 9));

        assertEquals("Dalia, Claude et Bachir ont remporté 1 point en tant qu'occupant·e·s majoritaires d'un réseau hydrographique contenant 1 poisson.",
                textMakerFr.playersScoredRiverSystem(Set.of(playerColor1, playerColor2, playerColor3), 1, 1));
    }

    @Test
    void pitTrap() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        var animals = new HashMap<Animal.Kind, Integer>();
        animals.put(Animal.Kind.MAMMOTH, 2);
        animals.put(Animal.Kind.AUROCHS, 2);
        animals.put(Animal.Kind.DEER, 2);

        var animals2 = new HashMap<Animal.Kind, Integer>();
        animals2.put(Animal.Kind.AUROCHS, 1);

        assertEquals("Bachir et Alice ont remporté 12 points en tant qu'occupant·e·s majoritaires d'un pré contenant la grande fosse à pieux entourée de 2 mammouths, 2 aurochs et 2 cerfs.",
                textMakerFr.playersScoredPitTrap(Set.of(playerColor3, playerColor4), 12, animals));

        assertEquals("Dalia a remporté 2 points en tant qu'occupant·e majoritaire d'un pré contenant la grande fosse à pieux entourée de 1 auroch.",
                textMakerFr.playersScoredPitTrap(Set.of(playerColor1), 2, animals2));
    }

    @Test
    void raft() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Dalia et Claude ont remporté 10 points en tant qu'occupant·e·s majoritaires d'un réseau hydrographique contenant le radeau et 10 lacs.",
                textMakerFr.playersScoredRaft(Set.of(playerColor1, playerColor2), 10, 10));

        assertEquals("Alice a remporté 1 point en tant qu'occupant·e majoritaire d'un réseau hydrographique contenant le radeau et 1 lac.",
                textMakerFr.playersScoredRaft(Set.of(playerColor4), 1, 1));
    }

    @Test
    void victory() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Bachir a remporté la partie avec 111 points !",
                textMakerFr.playersWon(Set.of(playerColor3), 111));

        assertEquals("Dalia et Alice ont remporté la partie avec 123 points !",
                textMakerFr.playersWon(Set.of(playerColor1, playerColor4), 123));
    }

    @Test
    void addRemoveOccupant() {
        TextMakerFr textMakerFr = new TextMakerFr();
        var playerColor1 = PlayerColor.RED;
        var playerColor2 = PlayerColor.BLUE;
        var playerColor3 = PlayerColor.GREEN;
        var playerColor4 = PlayerColor.YELLOW;
        textMakerFr.playerMap.put(playerColor1, "Dalia");
        textMakerFr.playerMap.put(playerColor2, "Claude");
        textMakerFr.playerMap.put(playerColor3, "Bachir");
        textMakerFr.playerMap.put(playerColor4, "Alice");

        assertEquals("Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.",
                textMakerFr.clickToOccupy());
        assertEquals("Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.",
                textMakerFr.clickToUnoccupy());
    }
}

