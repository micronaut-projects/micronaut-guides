from java.lang import Runnable

from .email_use_case import EmailUseCase


class EmailTask(Runnable):
    def __init__(self, email_use_case: EmailUseCase, email: str, message: str):
        self.email_use_case = email_use_case
        self.email = email
        self.message = message

    def run(self) -> None:
        self.email_use_case.send(self.email, self.message)
