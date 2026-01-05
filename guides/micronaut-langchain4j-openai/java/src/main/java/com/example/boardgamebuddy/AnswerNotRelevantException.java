package com.example.boardgamebuddy;

public class AnswerNotRelevantException extends RuntimeException {
    public AnswerNotRelevantException(String question, String answer) {
        super("The answer '" + answer + "' is not relevant to the question '" + question + "'.");
    }
}
