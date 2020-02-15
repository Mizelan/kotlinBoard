/* eslint-disable no-unused-vars */
/* eslint-disable no-console */
import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import HttpStatus from 'http-status-codes'
import router from '../router'
import AuthHelper from "./auth-helper"
import VueJwtDecode from "vue-jwt-decode";

Vue.use(Vuex)

export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production',

  state: {
    auth: {
      authority: [],
      jws: null,
    },
    pageInfo: {
      currentPage: Number,
      firstPage: Number,
      lastPage: Number,
      prevPage: Number,
      nextPage: Number,
      currentPageGroup: Number,
      lastPageGroup: Number,
      pageList: Array,
    },
    postList: {},
    postCountPerPage: 5,
  },
  getters: {
    isAdmin(state) {
      return AuthHelper.isAdmin()
    },
    isUser(state) {
      return AuthHelper.isUser()
    },
    isAuthenticated(state) {
      return localStorage.getItem(AuthHelper.jwtTokenName)
      //console.log("check: " + (state.auth.jws !== null))
      //return (state.auth.jws !== null);
    }
  },
  mutations: {
    LOGIN(state, {data}) {
      localStorage.setItem(AuthHelper.jwtTokenName, data.data)
    },
    LOGOUT(state) {
      localStorage.removeItem(AuthHelper.jwtTokenName)
      state.auth.jws = null;
    },
    AUTH_SETTING(state) {
      try {
        const jwtToken = localStorage.getItem(AuthHelper.jwtTokenName)
        this.state.auth = jwtToken ? VueJwtDecode.decode(jwtToken).authorities.split(',') : [];
        this.state.jws = jwtToken;
      } catch (error) {
        console.error(error)
      }
    },
    UPDATE_PAGINATION(state, responseData) {
      const pageInfo = responseData.pageInfo;
      this.state.postList = responseData.postList;
      this.state.pageInfo.currentPage = pageInfo.currentPage
      this.state.pageInfo.currentPageGroup = Math.floor((pageInfo.currentPage - 1) / pageInfo.countOfPage);
      this.state.pageInfo.lastPageGroup = Math.floor((pageInfo.maxPage - 1) / pageInfo.countOfPage);
      this.state.pageInfo.firstPage = 1;
      this.state.pageInfo.lastPage = pageInfo.maxPage - 1;
      this.state.pageInfo.prevPage = Math.max(1, (this.state.pageInfo.currentPageGroup * pageInfo.countOfPage));
      this.state.pageInfo.nextPage = Math.min( pageInfo.maxPage, ((this.state.pageInfo.currentPageGroup + 1) * pageInfo.countOfPage) + 1);

      const showingPageCount = this.state.pageInfo.currentPageGroup === this.state.pageInfo.lastPageGroup ?
          (pageInfo.maxPage - 1) % pageInfo.countOfPage :
          pageInfo.countOfPage;
      const showingStartPage = this.state.pageInfo.currentPageGroup * pageInfo.countOfPage + 1;

      this.state.pageInfo.pageList = Array.from(Array(showingPageCount), (_, i) => i + showingStartPage);
    }
  },
  actions: {
    LOGIN({commit}, {userId, passWd}) {
      let form = new FormData();
      form.append('userId', userId);
      form.append('passWd', passWd);

      return axios.post('/api/user/login', form)
          .then(({data}) => {
            commit('LOGIN', {data});
            router.push("/");

          }).catch(({data}) => {
            console.log("에러 ", data);
          });
    },
    SIGNUP({commit}, {userId, passWd, passWdConfirm}) {
      let form = new FormData();
      form.append('userId', userId);
      form.append('passWd', passWd);
      form.append('confirmPassWd', passWdConfirm);

      return axios.post('/api/user/signup', form)
          .then((result) => {
            return result;
          }).catch(({data}) => {
            console.log("에러 ", data);
          });
    },
    LOGOUT({commit}) {
      commit('LOGOUT');
      router.push('/login');
    },
    READ_POST_LIST(context, toPage) {
      return axios.get('/api/post', {params: { page: toPage, postCount: this.state.postCountPerPage} })
        .then(response => {
          if (response.status === HttpStatus.OK) {
            context.commit('UPDATE_PAGINATION', response.data);
            return response.data
          }
        });
    },
    READ_POST(context, {postId}) {
      return axios.get(`/api/post/${postId}`)
        .then(response => {
          if (response.status === HttpStatus.OK)
              return response.data
        });
    },
    CREATE_POST(context, {title, content}) {
      return axios.post('/api/post', {
        title, content
      })
      .then(response => {
        return response;
      });
    },
    UPDATE_POST(context, {postId, title, content}) {
      return axios.put(`/api/post/${postId}`, {
        title, content
      })
      .then(response => {
        return response;
      });
    }
  },
  modules: {
  }
})
