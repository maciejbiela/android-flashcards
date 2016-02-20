package io.github.maciejbiela.fiszki.utils;

public class StatisticsHelper {

    public static String getText(int goodAnswers, int totalAnswers) {

        return "Your statistics for this card: "
                + goodAnswers + "/" + totalAnswers +
                " (good/total)";
    }
}
