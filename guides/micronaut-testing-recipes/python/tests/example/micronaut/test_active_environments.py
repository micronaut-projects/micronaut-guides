def test_test_environment_is_active_by_default(my_context):
    active_names = {str(name) for name in my_context.getEnvironment().getActiveNames()}  # <1>
    assert active_names == {"test"}  # <2>
