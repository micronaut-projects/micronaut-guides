def test_account_requires_authentication(client):
    response = client.get("/account")
    assert response.status_code == 401  # <1>
