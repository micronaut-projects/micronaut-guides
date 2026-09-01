WHENEVER SQLERROR EXIT SQL.SQLCODE

-- Set the CDB defaults first. This also covers a startup where the PDB has
-- not yet been opened or has no explicit parameter override.
ALTER SYSTEM SET priority_txns_high_wait_target=5 SCOPE=MEMORY;
ALTER SYSTEM SET priority_txns_medium_wait_target=10 SCOPE=MEMORY;
ALTER SYSTEM SET priority_txns_mode=ROLLBACK SCOPE=MEMORY;

-- The application connects to the default Oracle Free PDB. Set the parameters
-- in that PDB so the JDBC sessions used by the demo inherit them.
-- <2>
ALTER SESSION SET CONTAINER = FREEPDB1;

-- <3>
ALTER SYSTEM SET priority_txns_high_wait_target=5 SCOPE=MEMORY;
ALTER SYSTEM SET priority_txns_medium_wait_target=10 SCOPE=MEMORY;
-- <4>
ALTER SYSTEM SET priority_txns_mode=ROLLBACK SCOPE=MEMORY;

EXIT;
