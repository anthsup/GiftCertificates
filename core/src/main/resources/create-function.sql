CREATE OR REPLACE FUNCTION searchLike(_col TEXT, _q TEXT)
  RETURNS TABLE(id INTEGER) LANGUAGE SQL AS
$$
SELECT id
FROM
  certificate
WHERE
  CASE _col
  WHEN 'name'
    THEN name
  WHEN 'description'
    THEN description
  ELSE NULL
  END
  ILIKE '%' || _q || '%'
$$;