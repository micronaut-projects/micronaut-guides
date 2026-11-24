import java

from example.micronaut.calculator import Calculator

ApplicationContext = java.type("io.micronaut.context.ApplicationContext")


def test_calculator_bean_exists_in_manually_started_context():
    application_context = ApplicationContext.run()  # <1>
    try:
        assert application_context.containsBean(Calculator)  # <2>
    finally:
        application_context.close()
