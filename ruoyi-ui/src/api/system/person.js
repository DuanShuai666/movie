import request from '@/utils/request'

// 查询演职人员列表
export function listPerson(query) {
  return request({
    url: '/system/person/list',
    method: 'get',
    params: query
  })
}

// 查询演职人员详细
export function getPerson(id) {
  return request({
    url: '/system/person/' + id,
    method: 'get'
  })
}

// 新增演职人员
export function addPerson(data) {
  return request({
    url: '/system/person',
    method: 'post',
    data: data
  })
}

// 修改演职人员
export function updatePerson(data) {
  return request({
    url: '/system/person',
    method: 'put',
    data: data
  })
}

// 删除演职人员
export function delPerson(id) {
  return request({
    url: '/system/person/' + id,
    method: 'delete'
  })
}
