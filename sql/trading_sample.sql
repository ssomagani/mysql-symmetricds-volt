------------------------------------------------------------------------------
-- Sample Data
------------------------------------------------------------------------------

-- Items to sell and their prices
insert into order_item (order_id, item_id) values (1, 'IBM');
insert into order_item (order_id, item_id) values (2, 'IBM');
insert into order_item (order_id, item_id) values (3, 'GOOG');

insert into order_price (order_id, price) values (1, 120);
insert into order_price (order_id, price) values (2, '122');
insert into order_price (order_id, price) values (3, '1230');

------------------------------------------------------------------------------
-- Clear and load SymmetricDS Configuration
------------------------------------------------------------------------------

delete from sym_trigger_router;
delete from sym_trigger;
delete from sym_router;
delete from sym_channel where channel_id in ('sale_transaction', 'item');
delete from sym_node_group_link;
delete from sym_node_group;
delete from sym_node_host;
delete from sym_node_identity;
delete from sym_node_security;
delete from sym_node;

------------------------------------------------------------------------------
-- Channels
------------------------------------------------------------------------------

-- Channel "orders" for tables related to sales and refunds
insert into sym_channel 
(channel_id, processing_order, max_batch_size, enabled, description)
values('orders_channel', 1, 1, 1, 'Order data from sources');

------------------------------------------------------------------------------
-- Node Groups
------------------------------------------------------------------------------

insert into sym_node_group (node_group_id) values ('orders');
insert into sym_node_group (node_group_id) values ('compliance');

------------------------------------------------------------------------------
-- Node Group Links
------------------------------------------------------------------------------

-- Orders sends changes to Compliance when Compliance pulls from Orders
insert into sym_node_group_link (source_node_group_id, target_node_group_id, data_event_action) values ('orders', 'compliance', 'W');

------------------------------------------------------------------------------
-- Triggers
------------------------------------------------------------------------------

-- Triggers for tables on "orders" channel
insert into sym_trigger 
(trigger_id,source_table_name,channel_id,last_update_time,create_time)
values('order_item_trgr','order_item','orders_channel',current_timestamp,current_timestamp);

insert into sym_trigger 
(trigger_id,source_table_name,channel_id,last_update_time,create_time)
values('order_price_trgr','order_price','orders_channel',current_timestamp,current_timestamp);

------------------------------------------------------------------------------
-- Routers
------------------------------------------------------------------------------

-- Default router sends all data from orders to compliance 
insert into sym_router 
(router_id,source_node_group_id,target_node_group_id,router_type,create_time,last_update_time)
values('orders_2_compliance', 'orders', 'compliance', 'default',current_timestamp, current_timestamp);

------------------------------------------------------------------------------
-- Trigger Routers
------------------------------------------------------------------------------

-- Send all items to compliance
insert into sym_trigger_router 
(trigger_id,router_id,initial_load_order,last_update_time,create_time)
values('order_item_trgr','orders_2_compliance', 100, current_timestamp, current_timestamp);

-- Send all prices to compliance
insert into sym_trigger_router 
(trigger_id,router_id,initial_load_order,last_update_time,create_time)
values('order_price_trgr','orders_2_compliance', 100, current_timestamp, current_timestamp);

------------------------------------------------------------------------------
-- Transforms
------------------------------------------------------------------------------

insert into SYM_TRANSFORM_TABLE (
        transform_id, source_node_group_id, target_node_group_id, transform_point, source_table_name,
        target_table_name, update_action, delete_action, transform_order, column_policy, update_first,
        last_update_time, create_time
) values ('update-item-2-exec', 'orders', 'compliance', 'EXTRACT', 'ORDER_ITEM', 'ORDER_EXEC', 'UPDATE_COL', 'DEL_ROW', 1, 'IMPLIED', 1, current_timestamp, current_timestamp);


insert into SYM_TRANSFORM_TABLE (
        transform_id, source_node_group_id, target_node_group_id, transform_point, source_table_name,
        target_table_name, update_action, delete_action, transform_order, column_policy, update_first,
        last_update_time, create_time
) values ('update-price-2-exec', 'orders', 'compliance', 'EXTRACT', 'ORDER_PRICE', 'ORDER_EXEC', 'UPDATE_COL', 'DEL_ROW', 1, 'IMPLIED', 1, current_timestamp, current_timestamp);

