from logback.config import dictConfig

# tag::logging[]
LOGGING = {
    "version": 1,
    "disable_existing_loggers": False,
    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
            "level": "TRACE",
            "stream": "ext://sys.stdout",
        }
    },
    "root": {
        "level": "INFO",
        "handlers": ["console"],
    },
    "loggers": {
        "io.micronaut.http.client": {
            "level": "TRACE",
        }
    },
}

dictConfig(LOGGING)
# end::logging[]
