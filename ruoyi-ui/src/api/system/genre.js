import request from '@/utils/request'

// 查询电影类型列表
export function listGenre(query) {
  return request({
    url: '/system/genre/list',
    method: 'get',
    params: query
  })
}

// 查询电影类型详细
export function getGenre(id) {
  return request({
    url: '/system/genre/' + id,
    method: 'get'
  })
}

// 新增电影类型
export function addGenre(data) {
  return request({
    url: '/system/genre',
    method: 'post',
    data: data
  })
}

// 修改电影类型
export function updateGenre(data) {
  return request({
    url: '/system/genre',
    method: 'put',
    data: data
  })
}

// 删除电影类型
export function delGenre(id) {
  return request({
    url: '/system/genre/' + id,
    method: 'delete'
  })
}
