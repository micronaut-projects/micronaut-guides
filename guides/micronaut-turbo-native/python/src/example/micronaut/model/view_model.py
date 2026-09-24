from dataclasses import dataclass
from datetime import UTC, datetime
from email.utils import format_datetime

from java.security import Principal


@dataclass
class ViewModel:
    pageTitle: str
    pageClass: str = ""
    action: str | None = None
    principal: Principal | None = None

    def to_dict(self) -> dict:
        return {
            "pageTitle": self.pageTitle,
            "pageClass": self.pageClass,
            "action": self.action,
            "principal": self.principal,
            "renderDate": format_datetime(datetime.now(UTC), usegmt=True),
        }
