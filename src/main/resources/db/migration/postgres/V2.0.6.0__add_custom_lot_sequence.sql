--
-- Name: custom_lot_pkseq; Type: SEQUENCE;
--
-- DROP SEQUENCE IF EXISTS custom_lot_pkseq;

CREATE SEQUENCE custom_lot_pkseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('custom_lot_pkseq', (select count(*)+1 from lot));