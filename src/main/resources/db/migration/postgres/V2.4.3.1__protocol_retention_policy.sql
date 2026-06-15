-- ACAS-ROO: Annotate protocols with deleted experiment retention days
-- Adds a protocol_value for 'deleted experiment retention days' to protocols labeled 'Vial Inventory' if not already present

WITH new_txn AS (
    INSERT INTO ls_transaction (id, comments, recorded_date, version)
    VALUES (nextval('value_pkseq'), 'deleted experiment retention days', now(), 0)
    RETURNING id
)
INSERT INTO protocol_value
    (id, ls_type, ls_kind, ls_type_and_kind, numeric_value, ls_transaction, recorded_by, recorded_date, version, protocol_state_id, deleted, ignored, public_data)
SELECT
    nextval('value_pkseq'), 'numericValue', 'deleted experiment retention days', 'numericValue_deleted experiment retention days', 7, new_txn.id, 'bbolt', now(), 0, ps.id, false, false, false
FROM
    protocol p
    JOIN protocol_label pl ON p.id = pl.protocol_id
    JOIN protocol_state ps ON p.id = ps.protocol_id
    CROSS JOIN new_txn nt
WHERE
    pl.label_text = 'Vial Inventory'
    AND pl.ignored = false
    AND pl.deleted = false
    AND pl.ls_type = 'name'
    AND pl.ls_kind = 'protocol name'
    AND p.ignored = false
    AND p.deleted = false
    AND ps.ignored = false
    AND ps.deleted = false
    AND ps.ls_type = 'metadata'
    AND ps.ls_kind = 'protocol metadata'
    AND NOT EXISTS (
        SELECT 1 FROM protocol_value pv
        WHERE pv.protocol_state_id = ps.id
          AND pv.ls_kind = 'deleted experiment retention days'
          AND pv.ignored = false
          AND pv.deleted = false
    );
