package com.example.boardgamebuddy;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

@Controller
public class AskController {

  private final BoardGameService boardGameService;

  public AskController(BoardGameService boardGameService) { 
    this.boardGameService = boardGameService;
  }

  @Post("/ask")
  public Answer ask(@Body Question question) {
      return boardGameService.askQuestion(question); 
  }

}
