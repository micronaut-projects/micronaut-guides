from jakarta.inject import Singleton
from org.springframework.security.crypto.bcrypt import BCryptPasswordEncoder

from .password_encoder import PasswordEncoder


@Singleton  # <1>
class BCryptPasswordEncoderService(PasswordEncoder):
    def __init__(self):
        self.delegate = BCryptPasswordEncoder()

    def encode(self, raw_password: str) -> str:
        return self.delegate.encode(raw_password)

    def matches(self, raw_password: str, encoded_password: str) -> bool:
        return self.delegate.matches(raw_password, encoded_password)
