import request from '@/utils/request'

export function askQuestion(question) {
  return request({
    url: '/movie/chat/ask',
    method: 'post',
    data: {
      question: question
    }
  })
}

export function healthCheck() {
  return request({
    url: '/movie/chat/health',
    method: 'get'
  })
}
