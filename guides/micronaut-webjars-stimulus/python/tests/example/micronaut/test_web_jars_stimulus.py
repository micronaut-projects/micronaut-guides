def test_stimulus_via_webjars_available(client):
    response = client.get("/webjars/hotwired__stimulus/3.2.1/dist/stimulus.js")

    assert response.status_code == 200
    assert "class Application" in response.text
