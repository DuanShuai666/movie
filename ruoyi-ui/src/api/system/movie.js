import request from '@/utils/request'

// 查询电影基本信息列表
export function listMovie(query) {
  return request({
    url: '/system/movie/list',
    method: 'get',
    params: query
  })
}

// 查询电影基本信息详细
export function getMovie(id) {
  return request({
    url: '/system/movie/' + id,
    method: 'get'
  })
}

// 新增电影基本信息
export function addMovie(data) {
  return request({
    url: '/system/movie',
    method: 'post',
    data: data
  })
}

// 修改电影基本信息
export function updateMovie(data) {
  return request({
    url: '/system/movie',
    method: 'put',
    data: data
  })
}

// 删除电影基本信息
export function delMovie(id) {
  return request({
    url: '/system/movie/' + id,
    method: 'delete'
  })
}
