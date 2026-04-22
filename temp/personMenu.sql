-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演职人员', '3', '1', 'person', 'system/person/index', 1, 0, 'C', '0', '0', 'system:person:list', '#', 'admin', sysdate(), '', null, '演职人员菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演职人员查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:person:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演职人员新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:person:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演职人员修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:person:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演职人员删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:person:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('演职人员导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:person:export',       '#', 'admin', sysdate(), '', null, '');