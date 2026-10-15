from enum import Enum


class Status(Enum):
    AVAILABLE = "AVAILABLE"
    RECONCILED = "RECONCILED"
    CHECKED_OUT = "CHECKED_OUT"
