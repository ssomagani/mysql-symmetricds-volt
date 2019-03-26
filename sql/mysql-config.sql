delete from sym_trigger_router;
delete from sym_trigger;
delete from sym_router;
delete from sym_channel;
delete from sym_node_group_link;
delete from sym_node_group;
delete from sym_node_host;
delete from sym_node_identity;
delete from sym_node_security;
delete from sym_node;

insert into sym_node_group (node_group_id) values ('voltdb-dest');
insert into sym_node_group (node_group_id) values ('mysql-src');

insert into sym_node_group_link(source_node_group_id, target_node_group_id, data_event_action) values ('mysql-src', 'voltdb-dest', 'P');
insert into sym_node_group_link(source_node_group_id, target_node_group_id, data_event_action) values ('voltdb-dest', 'mysql-src', 'W');

insert into sym_router (router_id, source_node_group_id, target_node_group_id, create_time, last_update_time) values ('mysql-2-voltdb', 'mysql-src', 'voltdb-dest', current_timestamp, current_timestamp);

insert into sym_trigger (trigger_id, source_table_name, channel_id, last_update_time, create_time) values ('item_trigger', 'item', 'default', current_timestamp, current_timestamp);

insert into sym_trigger_router(trigger_id, router_id, initial_load_order, create_time, last_update_time) values ('item', 'mysql-2-voltdb', 1, current_timestamp, current_timestamp);
