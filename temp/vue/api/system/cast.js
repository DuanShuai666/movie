import request from '@/utils/request'

// 查询演员列表
export function listCast(query) {
  return request({
    url: '/system/cast/list',
    method: 'get',
    params: query
  })
}

// 查询演员详细
export function getCast(movieId) {
  return request({
    url: '/system/cast/' + movieId,
    method: 'get'
  })
}

// 新增演员
export function addCast(data) {
  return request({
    url: '/system/cast',
    method: 'post',
    data: data
  })
}

// 修改演员
export function updateCast(data) {
  return request({
    url: '/system/cast',
    method: 'put',
    data: data
  })
}

// 删除演员
export function delCast(movieId) {
  return request({
    url: '/system/cast/' + movieId,
    method: 'delete'
  })
}
