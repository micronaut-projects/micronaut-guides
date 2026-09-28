from logback.config import dictConfig

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
        "stderr": {
            "class": "ch.qos.logback.core.ConsoleAppender",
            "level": "INFO",
            "formatter": "standard",
            "target": "System.err"
        }
    },
    "root": {
        "level": "INFO",
        "handlers": ["stderr"]
    }
}

dictConfig(LOGGING)
# end::logging[]
