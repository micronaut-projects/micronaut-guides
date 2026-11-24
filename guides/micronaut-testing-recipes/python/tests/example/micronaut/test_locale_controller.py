def test_locale_is_saved_in_a_cookie(client):
    response = client.post("/locale", data={"locale": "es"}, allow_redirects=False)  # <1>
    assert response.status_code == 303  # <2>
    assert response.cookies.get("locale") == "es"  # <3>
