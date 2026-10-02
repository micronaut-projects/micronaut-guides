import random

from jakarta.inject import Singleton

from .conference import Conference


@Singleton  # <1>
class ConferenceService:
    _conferences = [
        Conference("Greach"),
        Conference("GR8Conf EU"),
        Conference("Micronaut Summit"),
        Conference("Devoxx Belgium"),
        Conference("Oracle Code One"),
        Conference("CommitConf"),
        Conference("Codemotion Madrid"),
    ]

    def random_conf(self) -> Conference:  # <2>
        return random.choice(self._conferences)
