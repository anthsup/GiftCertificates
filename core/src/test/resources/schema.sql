SET DATABASE SQL SYNTAX PGS TRUE;

CREATE TABLE certificate
(
  id serial NOT NULL,
  name character varying(120) NOT NULL,
  description text NOT NULL,
  price numeric(10,2) NOT NULL,
  creation_date date NOT NULL,
  modification_date date,
  duration_days integer NOT NULL,
  CONSTRAINT gift_certificate_pkey PRIMARY KEY (id)
);

CREATE TABLE tag
(
  id serial NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT tag_pkey PRIMARY KEY (id),
  CONSTRAINT tag_name_key UNIQUE (name)
);

CREATE TABLE certificate_tag
(
  certificate_id integer NOT NULL,
  tag_id integer NOT NULL,
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
