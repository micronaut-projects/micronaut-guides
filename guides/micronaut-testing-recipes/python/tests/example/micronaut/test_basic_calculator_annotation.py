from jakarta.inject import Singleton
from micronaut.context.annotation import Primary

from example.micronaut.basic_calculator import BasicCalculator


def test_basic_calculator_is_primary(my_context):
    bean_definition = my_context.getBeanDefinition(BasicCalculator)  # <1>
    assert bean_definition.hasAnnotation(Primary)  # <2>
    assert bean_definition.hasStereotype(Singleton)  # <3>
