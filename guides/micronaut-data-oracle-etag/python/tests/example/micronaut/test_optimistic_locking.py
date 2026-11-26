import pytest
from micronaut.data.exceptions import OptimisticLockException
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.article import Article
from example.micronaut.article_repository import ArticleRepository
from example.micronaut.book import Book, BookDetails
from example.micronaut.book_repository import BookRepository


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False, transactional=False),
    )
    try:
        yield fixture
    finally:
        fixture.stop()


@pytest.fixture
def book_repository(my_context):
    return my_context[BookRepository]


@pytest.fixture
def article_repository(my_context):
    return my_context[ArticleRepository]


@pytest.fixture(autouse=True)
def clean_database(book_repository, article_repository):
    book_repository.deleteAll()
    article_repository.deleteAll()
    try:
        yield
    finally:
        book_repository.deleteAll()
        article_repository.deleteAll()


def test_stale_etag_prevents_an_update(book_repository):
    saved = book_repository.save(Book("Initial", BookDetails(200, 10)))
    fresh = book_repository.getById(saved.id)  # <1>
    assert fresh is not None
    assert fresh.etag is not None

    book_repository.update(Book("Updated", fresh.details, fresh.id, fresh.etag))  # <2>
    reloaded = book_repository.getById(fresh.id)
    assert reloaded is not None
    assert reloaded.title == "Updated"
    assert reloaded.etag != fresh.etag

    stale = Book("Stale", fresh.details, fresh.id, fresh.etag)  # <3>
    try:
        book_repository.update(stale)
    except OptimisticLockException:
        pass
    else:
        pytest.fail("A stale ETag must raise OptimisticLockException")

    assert book_repository.getById(fresh.id).title == "Updated"


def test_changing_an_excluded_field_does_not_change_the_etag(article_repository):
    saved = article_repository.save(Article("Oracle", "Initial notes"))
    fresh = article_repository.getById(saved.id)
    assert fresh is not None
    assert fresh.etag is not None

    article_repository.update(Article(fresh.title, "Updated notes", fresh.id, fresh.etag))
    reloaded = article_repository.getById(fresh.id)
    assert reloaded is not None
    assert reloaded.notes == "Updated notes"
    assert reloaded.etag == fresh.etag


def test_stale_etag_prevents_a_delete(book_repository):
    saved = book_repository.save(Book("Initial", BookDetails(200, 10)))
    fresh = book_repository.getById(saved.id)
    assert fresh is not None
    assert fresh.etag is not None

    book_repository.update(Book("Updated", fresh.details, fresh.id, fresh.etag))
    try:
        book_repository.delete(fresh)
    except OptimisticLockException:
        pass
    else:
        pytest.fail("A stale ETag must raise OptimisticLockException")

    reloaded = book_repository.getById(fresh.id)
    assert reloaded is not None
    assert reloaded.title == "Updated"
    assert reloaded.etag != fresh.etag
    book_repository.delete(reloaded)
    assert book_repository.getById(fresh.id) is None


def test_embedded_exclusion_preserves_etag_but_pages_change_it(book_repository):
    saved = book_repository.save(Book("Initial", BookDetails(200, 10)))
    fresh = book_repository.getById(saved.id)
    assert fresh is not None
    assert fresh.etag is not None

    book_repository.update(
        Book(fresh.title, BookDetails(fresh.details.pages, 11), fresh.id, fresh.etag)
    )
    reloaded = book_repository.getById(fresh.id)
    assert reloaded is not None
    assert reloaded.details.chapters == 11
    assert reloaded.etag == fresh.etag

    book_repository.update(
        Book(reloaded.title, BookDetails(201, reloaded.details.chapters), reloaded.id, reloaded.etag)
    )
    changed = book_repository.getById(fresh.id)
    assert changed is not None
    assert changed.details.pages == 201
    assert changed.details.chapters == 11
    assert changed.etag != fresh.etag
