package game;

import javafx.scene.control.Alert;
import json.Statistics;
import json.StatisticsStruct;

import java.util.Comparator;
import java.util.Optional;

public class WinScreen {
    /**
     * When you lose in the game this window will pop up, it will show the current score and the high score.
     * @param score the score from the current game
     * @return
     */
    public static Alert gameOver(int score)
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Game over!");
        Statistics statistics = new Statistics();
        Optional<StatisticsStruct> maxTurns = statistics.getAll().stream().max(Comparator.comparing(StatisticsStruct::getScore));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Your score: ")
                .append(score)
                .append("\n")
                .append("Highscore: ")
                .append(maxTurns.get().getScore());
        a.setContentText(stringBuilder.toString());
        return a;
    }
}
