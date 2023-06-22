import request from '@/utils/request'

/**
 * 获取列表数据
 */
export function getVOList(params) {
  return request({
    url: '/xhr/v1/advise/listAll',
    method: 'post',
    data: params
  })
}

/**
 * 获取收藏列表数据
 */
export function getStars(query) {
  return request({
    url: '/xhr/v1/stars/list',
    method: 'get',
    params: query
  })
}


/**
 * 获取区域信息下拉框
 */
export function getRegion() {
  return request({
    url: '/xhr/v1/getData/region',
    method: 'get',
  })
}

/**
 * 获取批次信息下拉框
 */
export function getBatch() {
  return request({
    url: '/xhr/v1/getData/batch',
    method: 'get',
  })
}

/**
 * 获取大学分类下拉框
 */
export function getSchoolType() {
  return request({
    url: '/xhr/v1/getData/schooltype',
    method: 'get',
  })
}

/**
 * 获取专业分类下拉框
 */
export function getMajorType() {
  return request({
    url: '/xhr/v1/getData/majortype',
    method: 'get',
  })
}

/**
 *填报志愿
 */
export function submitVolunteer(params) {
  return request({
    url: '/xhr/v1/volunteer/addVolunteer',
    method: 'post',
    data: params
  })
}

/**
 * 获取当前信息
 */
export function getCurrentInfo() {
  return request({
    url: '/xhr/v1/volunteer/getCurrent',
    method: 'get',
  })
}

/**
 * 更改收藏状态
 */
export function changeStarStatus(id) {
  return request({
    url: '/xhr/v1/stars/star/' + id,
    method: 'get',
  })
}
