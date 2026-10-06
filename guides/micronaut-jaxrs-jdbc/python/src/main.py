from logback.config import dictConfig

from example.micronaut.name_dto import NameDto
from example.micronaut.pet import Pet
from example.micronaut.pet_repository import PetRepository
from example.micronaut.pet_resource import PetResource
from example.micronaut.pet_save import PetSave
from example.micronaut.pet_type import PetType

LOGGING = {
    "version": 1,
    "disable_existing_loggers": False,
    "formatters": {
        "colored": {
            "format": "%cyan(%d{HH:mm:ss.SSS}) %highlight(%-5level) %magenta(%logger{36}): %msg"
        }
    },
    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
            "level": "INFO",
            "formatter": "colored",
            "stream": "ext://sys.stdout"
        }
    },
    "root": {
        "level": "INFO",
        "handlers": ["console"]
    }
}

dictConfig(LOGGING)
