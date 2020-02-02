 /* eslint-disable no-unused-vars */
 
import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
    GET_POST_LIST(context) {
      return axios.get('/post')
        .then(result => {
          if (result.status === 200) {
              return {
                "postList": result.data
              }
          }
        });
    },
    CREATE_POST(context, {title, content}) {
      return axios.post('/post', {
        title, content
      })
        .then(result => {
          return result;
        });
    },
  },
  modules: {
  }
})
