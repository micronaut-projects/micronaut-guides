def test_root_path_serves_html(client):
    response = client.get("/")

    assert response.status_code == 200
    assert "<!DOCTYPE html>" in response.text
    assert "<title>Hello Stimulus</title>" in response.text
    assert "/webjars/hotwired__stimulus/3.2.1/dist/stimulus.js" in response.text
