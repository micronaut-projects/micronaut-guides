from example.micronaut.oci_identity_domain_open_id_authentication_mapper import (
    OciIdentityDomainOpenIdAuthenticationMapper,
)


def test_resolve_roles_from_oci_groups():
    mapper = OciIdentityDomainOpenIdAuthenticationMapper()

    assert mapper.resolve_roles(
        {
            "groups": [
                {
                    "name": "ROLE_ADMIN",
                    "id": "cab3a10ad56935ca1726464bcaa12a34",
                },
            ],
        }
    ) == ["ROLE_ADMIN"]
    assert mapper.resolve_roles({}) == []
    assert mapper.resolve_roles({"groups": "foo"}) == []
