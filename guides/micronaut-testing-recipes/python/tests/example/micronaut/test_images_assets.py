def test_images_are_accessible(client):
    response = client.get("/assets/images/micronaut_stacked_black.png")  # <1>
    assert response.status_code == 200  # <2>
    assert len(response.content) > 0  # <3>
