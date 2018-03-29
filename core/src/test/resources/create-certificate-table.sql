-- Table: public.certificate

-- DROP TABLE public.certificate;

CREATE TABLE public.certificate
(
    id integer NOT NULL DEFAULT nextval('gift_certificate_id_seq'::regclass),
    name character varying(120) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default" NOT NULL,
    price numeric(10,2) NOT NULL,
    creation_date date NOT NULL,
    modification_date date,
    duration_days integer NOT NULL,
    CONSTRAINT gift_certificate_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.certificate
    OWNER to postgres;