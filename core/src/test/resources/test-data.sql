INSERT INTO certificate(
	name, description, price, creation_date, modification_date, duration_days, id)
	VALUES ('certificate', 'the usual certificate', 13.5, '2018-03-31 10:15:30', '2018-03-31 10:15:30', 12, 1);

INSERT INTO certificate(
	name, description, price, creation_date, modification_date, duration_days, id)
	VALUES ('certificate1', 'the cool certificate', 14, '2018-04-01 10:15:30', '2018-04-01 10:15:30', 10, 2);

INSERT INTO certificate(
	name, description, price, creation_date, modification_date, duration_days, id)
	VALUES ('certificate2', 'the coolest certificate', 18, '2018-03-30 10:15:30', '2018-03-30 10:15:30', 100, 3);

INSERT INTO tag(name, id)
	VALUES ('happy', 1);

INSERT INTO tag(name, id)
	VALUES ('birthday', 2);

INSERT INTO tag(name, id)
	VALUES ('holiday', 3);

INSERT INTO tag(name, id)
	VALUES ('gift', 4);

INSERT INTO certificate_tag(certificate_id, tag_id)
	VALUES (1, 1);

INSERT INTO certificate_tag(certificate_id, tag_id)
	VALUES (1, 2);

INSERT INTO certificate_tag(certificate_id, tag_id)
	VALUES (2, 1);

INSERT INTO certificate_tag(certificate_id, tag_id)
	VALUES (2, 3);

INSERT INTO certificate_tag(certificate_id, tag_id)
	VALUES (3, 4);

INSERT INTO certificate_tag(certificate_id, tag_id)
	VALUES (3, 1);

INSERT INTO certificate_tag(certificate_id, tag_id)
	VALUES (3, 3);
