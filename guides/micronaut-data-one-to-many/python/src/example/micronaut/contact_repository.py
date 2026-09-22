from java.util import Optional
from micronaut.data.annotation import Join, Query
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .contact_complete import ContactComplete
from .contact_entity import ContactEntity
from .contact_preview import ContactPreview


@JdbcRepository(dialect=Dialect.H2)  # <1>
class ContactRepository(CrudRepository[ContactEntity, int]):  # <2>
    @Join(value="phones", type=Join.Type.LEFT_FETCH)  # <3>
    def getById(self, id: int) -> Optional[ContactEntity]: ...

    @Query(value="select id, first_name, last_name from contact where id = :id")  # <4>
    def findPreviewById(self, id: int) -> Optional[ContactPreview]: ...

    @Query(value="""
        select c.id, c.first_name, c.last_name, group_concat(p.phone) as phones
        from contact c
        left outer join phone p on c.id = p.contact_id
        where c.id = :id
        group by c.id
        """)  # <5>
    def findCompleteById(self, id: int) -> Optional[ContactComplete]: ...
