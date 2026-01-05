package com.example.boardgamebuddy;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.inject.Singleton;

@Singleton
public class MicronautAiBoardGameService implements BoardGameService {

  private final ChatModel chatModel;

  public MicronautAiBoardGameService(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @Override
  public Answer askQuestion(Question question) {
      return new Answer(chatModel.chat(question.question()));
  }
}
