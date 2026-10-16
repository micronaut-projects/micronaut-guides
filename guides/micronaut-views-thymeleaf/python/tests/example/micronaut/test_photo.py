from example.micronaut.photo import Photo


def test_photo_is_a_serdeable_dataclass():
    photo = Photo(
        albumId=1,
        title="accusamus beatae ad facilis cum similique qui sunt",
        url="https://via.placeholder.com/600/92c952",
        thumbnailUrl="https://via.placeholder.com/150/92c952",
    )

    assert photo.albumId == 1
    assert photo.title == "accusamus beatae ad facilis cum similique qui sunt"
