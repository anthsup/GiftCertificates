-- Table: public.tag

-- DROP TABLE public.tag;

CREATE TABLE public.tag
(
    id integer NOT NULL DEFAULT nextval('tag_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tag_pkey PRIMARY KEY (id),
    CONSTRAINT tag_name_key UNIQUE (name)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tag
    OWNER to postgres;