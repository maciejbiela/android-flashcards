package io.github.maciejbiela.fiszki;

public class Statistics {

    public static String getText(int goodAnswers, int totalAnswers) {
        return "Your statistics for this card: "
                + goodAnswers + "/" + totalAnswers +
                " (good/total)";
    }
}
