CREATE OR REPLACE JSON RELATIONAL DUALITY VIEW student_view AS
SELECT JSON {
    '_id': s.id,
    'name': s.name,
    s.extras AS FLEX COLUMN,
    'classes': [
        SELECT JSON {
            'id': sc.id,
            'class': (
                SELECT JSON {
                    'classID': c.id,
                    'name': c.name
                }
                FROM TBL_CLASS c
                WITH INSERT UPDATE
                WHERE sc."CLASS_ID"=c."ID"
            )
        }
        FROM TBL_STUDENT_CLASSES sc
        WITH UPDATE INSERT DELETE
        WHERE s."ID"=sc."STUDENT_ID"
    ]
}
FROM TBL_STUDENT s WITH UPDATE INSERT DELETE;
