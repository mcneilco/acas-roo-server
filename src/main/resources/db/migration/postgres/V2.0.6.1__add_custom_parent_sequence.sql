--
-- Name: custom_lot_pkseq; Type: SEQUENCE;
--
-- DROP SEQUENCE IF EXISTS custom_parent_pkseq;

CREATE SEQUENCE custom_parent_pkseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('custom_parent_pkseq', (select greatest(count(*)+1 , max(parent_number)+1) from parent));
