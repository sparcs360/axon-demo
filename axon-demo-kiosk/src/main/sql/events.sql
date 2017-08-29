SELECT
   E.TIME_STAMP
,   CONCAT(E.TYPE, ' #', E.AGGREGATE_IDENTIFIER) AS Aggregate
,   E.PAYLOAD_TYPE AS EventName
-- ,   UTF8TOSTRING(E.META_DATA) AS Metadata
,   UTF8TOSTRING(E.PAYLOAD) AS Payload
FROM DOMAIN_EVENT_ENTRY E
ORDER BY
   E.TYPE
,   E.AGGREGATE_IDENTIFIER
,   E.SEQUENCE_NUMBER