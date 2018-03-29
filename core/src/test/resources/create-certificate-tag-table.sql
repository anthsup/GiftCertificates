-- Table: public.certificate_tag

-- DROP TABLE public.certificate_tag;

CREATE TABLE public.certificate_tag
(
    certificate_id integer NOT NULL,
    tag_id integer NOT NULL,
    CONSTRAINT id PRIMARY KEY (certificate_id, tag_id),
    CONSTRAINT certificate_tag_certificate_id_fkey FOREIGN KEY (certificate_id)
        REFERENCES public.certificate (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT certificate_tag_tag_id_fkey FOREIGN KEY (tag_id)
        REFERENCES public.tag (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.certificate_tag
    OWNER to postgres;