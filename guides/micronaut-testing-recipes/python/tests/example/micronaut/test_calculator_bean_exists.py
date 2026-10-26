from example.micronaut.calculator import Calculator


def test_calculator_bean_exists(my_context):  # <1>
    assert my_context.containsBean(Calculator)  # <2>
