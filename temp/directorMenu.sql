-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导演', '3', '1', 'director', 'system/director/index', 1, 0, 'C', '0', '0', 'system:director:list', '#', 'admin', sysdate(), '', null, '导演菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导演查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:director:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导演新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:director:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导演修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:director:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导演删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:director:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导演导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:director:export',       '#', 'admin', sysdate(), '', null, '');