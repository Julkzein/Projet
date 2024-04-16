package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

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
        var playerColor5 = PlayerColor.PURPLE;
        assertEquals("Dalia, Bachir et Alice", textMakerFr.orderPlayer(Set.of(playerColor1, playerColor2, playerColor3)));
    }
}
