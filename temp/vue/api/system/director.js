import request from '@/utils/request'

// 查询导演列表
export function listDirector(query) {
  return request({
    url: '/system/director/list',
    method: 'get',
    params: query
  })
}

// 查询导演详细
export function getDirector(movieId) {
  return request({
    url: '/system/director/' + movieId,
    method: 'get'
  })
}

// 新增导演
export function addDirector(data) {
  return request({
    url: '/system/director',
    method: 'post',
    data: data
  })
}

// 修改导演
export function updateDirector(data) {
  return request({
    url: '/system/director',
    method: 'put',
    data: data
  })
}

// 删除导演
export function delDirector(movieId) {
  return request({
    url: '/system/director/' + movieId,
    method: 'delete'
  })
}
