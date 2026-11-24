def test_home_redirects_to_contacts(client):
    response = client.get("/", allow_redirects=False)  # <1>
    assert response.status_code == 303  # <2>
    assert response.headers["Location"] == "/contacts"  # <3>
