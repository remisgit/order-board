
DROP TABLE IF EXISTS T_ORDER;
CREATE TABLE T_ORDER (
 ID              NUMBER(19) PRIMARY KEY,
 USER_ID         VARCHAR2(64),
 ORDER_QUANTITY  DECIMAL(10, 2) NOT NULL,
 ORDER_PRICE     NUMBER(10) NOT NULL,
 BUY_SELL        VARCHAR2(4),
 CREATED_TS      TIMESTAMP
);

CREATE SEQUENCE SEQ_ORDER START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;


CREATE OR REPLACE VIEW V_ORDER_SUMMARY AS
SELECT
 SUM (ORDER_QUANTITY) AS ORDER_QUANTITY
 ,ORDER_PRICE
 ,BUY_SELL
FROM T_ORDER
GROUP BY ORDER_PRICE, BUY_SELL
ORDER BY
    CASE
        WHEN BUY_SELL = 'SELL' THEN ORDER_PRICE
    END ASC,
    CASE
        WHEN BUY_SELL = 'BUY' THEN ORDER_PRICE
    END DESC
;

--inserts for initial ORDER table state
INSERT INTO "T_ORDER" (ID, USER_ID, ORDER_QUANTITY, ORDER_PRICE, BUY_SELL) VALUES (nextval('SEQ_ORDER'), 'USER_1', '3.5', '300', 'BUY');
INSERT INTO "T_ORDER" (ID, USER_ID, ORDER_QUANTITY, ORDER_PRICE, BUY_SELL) VALUES (nextval('SEQ_ORDER'), 'USER_2', '4.0', '340', 'BUY');
INSERT INTO "T_ORDER" (ID, USER_ID, ORDER_QUANTITY, ORDER_PRICE, BUY_SELL) VALUES (nextval('SEQ_ORDER'), 'USER_3', '4.0', '340', 'SELL');
INSERT INTO "T_ORDER" (ID, USER_ID, ORDER_QUANTITY, ORDER_PRICE, BUY_SELL) VALUES (nextval('SEQ_ORDER'), 'USER_2', '5.0', '340', 'SELL');
INSERT INTO "T_ORDER" (ID, USER_ID, ORDER_QUANTITY, ORDER_PRICE, BUY_SELL) VALUES (nextval('SEQ_ORDER'), 'USER_3', '2.1', '740', 'SELL');