 /* eslint-disable no-unused-vars */
 /* eslint-disable no-console */
 
import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

axios.interceptors.response.use(response => {
  console.log(response)
  return response;
}, error => {
  console.error("axios response error : ", error);
  return Promise.reject(error)
})

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
    READ_POST_LIST(context) {
      return axios.get('/api/post')
        .then(result => {
          if (result.status === 200) {
              return {
                "postList": result.data
              }
          }
        });
    },
    READ_POST(context, {postId}) {
      return axios.get(`/api/post/${postId}`)
        .then(result => {
          if (result.status === 200) {
              return result.data
          }
        });
    },
    CREATE_POST(context, {title, content}) {
      return axios.post('/api/post', {
        title, content
      })
      .then(result => {
        return result;
      });
    },
    UPDATE_POST(context, {postId, title, content}) {
      return axios.put(`/api/post/${postId}`, {
        title, content
      })
      .then(result => {
        return result;
      });
    }
  },
  modules: {
  }
})
