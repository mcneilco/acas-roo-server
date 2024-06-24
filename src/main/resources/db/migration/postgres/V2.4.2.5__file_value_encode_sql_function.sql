CREATE OR REPLACE FUNCTION encode_file_value(text)
    RETURNS text
    AS $$
    -- Function to encode a URI component
    SELECT
        string_agg(
             -- Bytes <= 1 are safe because they are part of the standard ASCII chars which don't need to be checked
            CASE WHEN bytes > 1
                -- Skip encoding of these characters
                -- - Alphanumeric characters: `0-9`, `a-z`, `A-Z`
                -- - Unreserved characters: `-`, `_`, [`.`](command:_github.copilot.openRelativePath?%5B%7B%22scheme%22%3A%22file%22%2C%22authority%22%3A%22%22%2C%22path%22%3A%22%2FUsers%2Fbbolt%2Fschrodinger%2FACAS-753-url-enncode-file-values%22%2C%22query%22%3A%22%22%2C%22fragment%22%3A%22%22%7D%5D "/Users/bbolt/schrodinger/ACAS-753-url-enncode-file-values"), `~`
                -- - Sub-delimiters (used in certain contexts but often considered safe): `!`, `$`, `&`, `'`, `(`, `)`, `*`, `+`, `,`, `;`, `=`
                OR c !~ '[/0-9a-zA-Z\-_\.~!$&()*+,;]+' THEN
                -- Replacement explaination:
                    -- convert_to(c, 'utf-8')::bytea: This part takes a column or value c and converts it to a byte array (bytea) using UTF-8 encoding. The convert_to function is used for encoding conversion, and casting the result to bytea ensures that it's treated as a binary string for further processing.
                    -- encode(..., 'hex'): The encode function then takes the binary string from step 1 and encodes it as a hexadecimal string. This is useful for safely representing binary data in a textual format, where each byte of the original binary data is represented as two hexadecimal characters.
                    -- regexp_replace(..., '(..)', E'%\\1', 'g'): Finally, this part uses the regexp_replace function to modify the hexadecimal string. It searches for every pair of characters (as matched by the regular expression (..)), and replaces each pair with a % followed by the pair of characters itself. The E'%\\1' is the replacement pattern, where \\1 refers to the first (and in this case, only) capturing group from the regular expression—essentially, each pair of hex characters. The 'g' flag at the end indicates that this replacement should be done globally across the entire string, not just the first match.
                regexp_replace(encode(convert_to(c, 'utf-8')::bytea, 'hex'), '(..)', E'%\\1', 'g')
            ELSE
                c
            END, '')
    FROM(
        SELECT
            c,
            octet_length(c) bytes
        FROM
            regexp_split_to_table($1, '') c) q;
    -- immutable means that the function will always return the same result given the same input
    -- strict means that the function will not be called if the input is null e.g. urlencode(null) will return null
$$
LANGUAGE sql
IMMUTABLE STRICT;
