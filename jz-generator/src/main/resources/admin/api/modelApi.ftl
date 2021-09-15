import request from '@/utils/request'

export function ${model.camelCaseField}List (params) {
  return request({
    url: '/${model.camelCaseField}/list',
    method: 'get',
    params
  })
}

export function ${model.camelCaseField}Info (id) {
  return request({
    url: '/${model.camelCaseField}/detail',
    method: 'get',
    params: { id: id }
  })
}

export function ${model.camelCaseField}Add (data) {
  return request({
    url: '/${model.camelCaseField}/add',
    method: 'post',
    data
  })
}

export function ${model.camelCaseField}Update (data) {
  return request({
    url: '/${model.camelCaseField}/update',
    method: 'put',
    data
  })
}

export function ${model.camelCaseField}Del (id) {
  return request({
    url: '/${model.camelCaseField}/delete',
    method: 'delete',
    data: { id: id }
  })
}

