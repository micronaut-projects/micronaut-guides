import logging

from logback.config import dictConfig

logging.ALL = logging.NOTSET

# tag::logging[]
LOGGING = {
    "version": 1,
    "disable_existing_loggers": False,
    "formatters": {
        "standard": {
            "format": "%(asctime)s [%(levelname)s] %(name)s: %(message)s"
        }
    },
    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
            "level": "INFO",
            "formatter": "standard",
            "stream": "ext://sys.stdout"
        }
    },
    "root": {
        "level": "INFO",
        "handlers": ["console"]
    },
    "loggers": {
        "io.micronaut.http.client": {
            "level": "ALL"
        }
    }
}

dictConfig(LOGGING)
# end::logging[]
