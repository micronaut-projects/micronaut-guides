from collections import deque

from jakarta.inject import Singleton


@Singleton
class ReceivedEvents:
    def __init__(self):
        self.games = deque()
        self.moves = deque()

    def clear(self):
        self.games.clear()
        self.moves.clear()
