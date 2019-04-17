CREATE TABLE ORDER_EXEC (
   ORDER_ID integer NOT NULL,
   ITEM_ID varchar(8),
   PRICE integer
);
partition table ORDER_EXEC on column ORDER_ID;


CREATE TABLE TICKER (
   SYMBOL varchar(8) NOT NULL,
   PRICE integer
);
partition table TICKER on column SYMBOL;

create stream EXEC_ORDERS partition on column order_id  export to target FILE (order_id integer not null, item_id varchar(8), price integer);

create procedure newExecOrder partition on table ORDER_EXEC column ORDER_ID
AS BEGIN 
	insert into order_exec values (?, ?, ?);
	insert into exec_orders values (?, ?, ?);
END;
