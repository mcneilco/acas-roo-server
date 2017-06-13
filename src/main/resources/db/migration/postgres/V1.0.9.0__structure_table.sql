CREATE EXTENSION IF NOT EXISTS rdkit;

CREATE TABLE IF NOT EXISTS chem_structure
(
  id bigint NOT NULL,
  code_name character varying(255) NOT NULL,
  mol_structure text NOT NULL,
  smiles character varying(1000),
  ignored boolean NOT NULL,
  deleted boolean NOT NULL,
  recorded_by character varying(255) NOT NULL,
  recorded_date timestamp without time zone NOT NULL,
  modified_by character varying(255),
  modified_date timestamp without time zone,
  ls_transaction bigint,
  version integer,
  rdkmol mol,
  CONSTRAINT chem_structure_pkey PRIMARY KEY (id),
  CONSTRAINT chem_structure_code_name_key UNIQUE (code_name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE chem_structure
  OWNER TO acas;
GRANT ALL ON TABLE chem_structure TO acas;


DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  c.relkind
   FROM   pg_class     c
   JOIN   pg_namespace n ON n.oid = c.relnamespace
   WHERE  c.relname = 'chem_structure_pkseq'      -- sequence name here
   AND    n.nspname = 'acas';  -- schema name here

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE chem_structure_pkseq
  		START WITH 1
  		INCREMENT BY 1
  		NO MINVALUE
  		NO MAXVALUE
  		CACHE 1;
	  ALTER TABLE chem_structure_pkseq
  		OWNER TO acas;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  c.relkind
   FROM   pg_class     c
   JOIN   pg_namespace n ON n.oid = c.relnamespace
   WHERE  c.relname = 'chem_structure_pkseq'      -- sequence name here
   AND    n.nspname = 'acas';  -- schema name here

   IF NOT FOUND THEN       -- name is free
      CREATE INDEX chem_structure_mol_structure
  		ON chem_structure(mol_structure);
   ELSIF _kind = 'I' THEN  -- index exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  c.relkind
   FROM   pg_class     c
   JOIN   pg_namespace n ON n.oid = c.relnamespace
   WHERE  c.relname = 'chem_structure_pkseq'      -- sequence name here
   AND    n.nspname = 'acas';  -- schema name here

   IF NOT FOUND THEN       -- name is free
   	CREATE INDEX chem_structure_smiles
  		ON chem_structure(smiles);
   ELSIF _kind = 'I' THEN  -- index exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  c.relkind
   FROM   pg_class     c
   JOIN   pg_namespace n ON n.oid = c.relnamespace
   WHERE  c.relname = 'rdk_mols_idx'      -- sequence name here
   AND    n.nspname = 'acas';  -- schema name here

   IF NOT FOUND THEN       -- name is free
   	CREATE INDEX rdk_mols_idx
  		ON chem_structure USING gist(rdkmol);
   ELSIF _kind = 'I' THEN  -- index exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;

--fingerprint table
CREATE TABLE IF NOT EXISTS chem_structure_fps
(
	id bigint NOT NULL,
	torsionbv bfp,
	mfp2 bfp,
	ffp2 bfp
)
WITH (
  OIDS=FALSE
);
ALTER TABLE chem_structure
  OWNER TO acas;
GRANT ALL ON TABLE chem_structure_fps TO acas;

--fingerprint indexes
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  c.relkind
   FROM   pg_class     c
   JOIN   pg_namespace n ON n.oid = c.relnamespace
   WHERE  c.relname = 'fps_ttbv_idx'      -- sequence name here
   AND    n.nspname = 'acas';  -- schema name here

   IF NOT FOUND THEN       -- name is free
   	CREATE INDEX fps_ttbv_idx
  		ON chem_structure_fps USING gist(torsionbv);
   ELSIF _kind = 'I' THEN  -- index exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  c.relkind
   FROM   pg_class     c
   JOIN   pg_namespace n ON n.oid = c.relnamespace
   WHERE  c.relname = 'fps_mfp2_idx'      -- sequence name here
   AND    n.nspname = 'acas';  -- schema name here

   IF NOT FOUND THEN       -- name is free
   	CREATE INDEX fps_mfp2_idx
  		ON chem_structure_fps USING gist(mfp2);
   ELSIF _kind = 'I' THEN  -- index exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  c.relkind
   FROM   pg_class     c
   JOIN   pg_namespace n ON n.oid = c.relnamespace
   WHERE  c.relname = 'fps_ffp2_idx'      -- sequence name here
   AND    n.nspname = 'acas';  -- schema name here

   IF NOT FOUND THEN       -- name is free
   	CREATE INDEX fps_ffp2_idx
  		ON chem_structure_fps USING gist(ffp2);
   ELSIF _kind = 'I' THEN  -- index exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;


CREATE OR REPLACE FUNCTION build_rdkmol_func () RETURNS trigger AS '
	BEGIN
		NEW.rdkmol = mol_from_ctab(NEW.mol_structure::cstring);
		NEW.smiles = mol_to_smiles(NEW.rdkmol);
		INSERT INTO chem_structure_fps(id, torsionbv, mfp2, ffp2)
		VALUES (NEW.id, torsionbv_fp(NEW.rdkmol), morganbv_fp(NEW.rdkmol), featmorganbv_fp(NEW.rdkmol));
		RETURN NEW;
	END;
	' LANGUAGE plpgsql;


DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT INTO _kind  tgname
	FROM pg_trigger
	WHERE NOT tgisinternal
	AND tgname = 'build_rdkmol_trg';  -- trigger name here

   IF NOT FOUND THEN       -- name is free
   	CREATE TRIGGER build_rdkmol_trg BEFORE INSERT OR UPDATE
   		ON chem_structure FOR EACH ROW
   		EXECUTE PROCEDURE build_rdkmol_func ();
   ELSIF _kind = 'I' THEN  -- index exists
      -- do nothing?
   ELSE                    -- conflicting object of different type exists
      -- do somethng!
   END IF;
END
$do$;

----function to build similarity searching fingerprints
create or replace function get_mfp2_neighbors_smiles(smiles text)
    returns table(id bigint, rdkmol mol, similarity double precision) as
  $$
  select id,rdkmol,tanimoto_sml(morganbv_fp(mol_from_smiles($1::cstring)),mfp2) as similarity
  from chem_structure_fps join chem_structure using (id)
  where morganbv_fp(mol_from_smiles($1::cstring))%mfp2
  order by morganbv_fp(mol_from_smiles($1::cstring))<%>mfp2;
  $$ language sql stable ;
  
create or replace function get_mfp2_neighbors_mol(queryMol mol)
    returns table(id bigint, rdkmol mol, similarity double precision) as
  $$
  select id,rdkmol,tanimoto_sml(morganbv_fp($1),mfp2) as similarity
  from chem_structure_fps join chem_structure using (id)
  where morganbv_fp($1)%mfp2
  order by morganbv_fp($1)<%>mfp2;
  $$ language sql stable ;
  