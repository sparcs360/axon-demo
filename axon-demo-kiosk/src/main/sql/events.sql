SELECT
   E.TIME_STAMP
,   E.TYPE AS AggregateType
,   E.AGGREGATE_IDENTIFIER AS AggregateId
,   RIGHT(E.PAYLOAD_TYPE, LENGTH(E.PAYLOAD_TYPE)-11) AS EventName
-- ,   UTF8TOSTRING(E.META_DATA) AS Metadata
,   UTF8TOSTRING(E.PAYLOAD) AS Payload
FROM DOMAIN_EVENT_ENTRY E
ORDER BY
   E.TYPE
,   E.AGGREGATE_IDENTIFIER
,   E.SEQUENCE_NUMBER
