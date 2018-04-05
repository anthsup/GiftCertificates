SET DATABASE SQL SYNTAX PGS TRUE;

CREATE TABLE certificate
(
  id                SERIAL                 NOT NULL,
  name              CHARACTER VARYING(120) NOT NULL,
  description       TEXT,
  price             NUMERIC(10, 2)         NOT NULL,
  creation_date     DATE,
  modification_date DATE,
  duration_days     INTEGER,
  CONSTRAINT gift_certificate_pkey PRIMARY KEY (id)
);

CREATE TABLE tag
(
  id   SERIAL                NOT NULL,
  name CHARACTER VARYING(50) NOT NULL,
  CONSTRAINT tag_pkey PRIMARY KEY (id),
  CONSTRAINT tag_name_key UNIQUE (name)
);

CREATE TABLE certificate_tag
(
  certificate_id INTEGER NOT NULL,
  tag_id         INTEGER NOT NULL,
  CONSTRAINT id PRIMARY KEY (certificate_id, tag_id),
  CONSTRAINT certificate_tag_certificate_id_fkey FOREIGN KEY (certificate_id)
  REFERENCES certificate (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT certificate_tag_tag_id_fkey FOREIGN KEY (tag_id)
  REFERENCES tag (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);
