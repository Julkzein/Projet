package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class MyMessageBoardTest {

    @Test
    void pointsCorrectlyReturned(){
        TextMakerClassForTestPurposes textMaker = new TextMakerClassForTestPurposes();
        MessageBoard messageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message("test", 2, Set.of(PlayerColor.RED, PlayerColor.BLUE), Set.of(3, 1)), new MessageBoard.Message("test", 3, Set.of(PlayerColor.RED, PlayerColor.GREEN), Set.of(3, 1))));

        //assertEquals()
    }
}
