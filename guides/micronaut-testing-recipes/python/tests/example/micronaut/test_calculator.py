from example.micronaut.calculator import Calculator


def test_primary_calculator_is_selected(my_context):
    calculator = my_context.getBean(Calculator)  # <1>
    assert calculator.add(2, 3) == 5
    assert len(my_context.getBeansOfType(Calculator)) == 2  # <2>
