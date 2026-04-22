-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演员', '3', '1', 'cast', 'system/cast/index', 1, 0, 'C', '0', '0', 'system:cast:list', '#', 'admin', sysdate(), '', null, '演员菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演员查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:cast:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演员新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:cast:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演员修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:cast:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演员删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:cast:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演员导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:cast:export',       '#', 'admin', sysdate(), '', null, '');