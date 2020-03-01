/* eslint-disable no-unused-vars */
/* eslint-disable no-console */

import Vue from 'vue'
import VueRouter from 'vue-router'
import Board from '../components/board/Board.vue'
import Login from '../components/user/Login.vue'
import EditPost from '../components/post/EditPost.vue'
import ViewPost from '../components/post/ViewPost.vue'
import NotFound from '../components/errors/NotFound.vue'
import store from '../state/store'
import axios from "axios";
import HttpStatus from "http-status-codes";

Vue.use(VueRouter);

const isProduction = process.env.NODE_ENV === 'production';
if (!isProduction) {
  axios.interceptors.response.use(response => {
    console.log(response);
    return response;
  }, error => {
    console.warn(`axios response error : ${error}`);
    return Promise.reject(error)
  })
}

axios.interceptors.response.use(
    response => {
      const status = response ? response.status : null;
      if (status === HttpStatus.NON_AUTHORITATIVE_INFORMATION) {
        alert("사용자 정보가 없거나 암호가 틀렸습니다.");
      }
      return response;
    },
    error => {
      const status = error.response ? error.response.status : null;
      if (status === HttpStatus.BAD_REQUEST) {
        if (error.response.data.message)
          alert(error.response.data.message);
        else
          alert(JSON.stringify(error.response));
        return error;
      }
      if( status === HttpStatus.UNAUTHORIZED ||
          status === HttpStatus.METHOD_NOT_ALLOWED) {
        store.commit('auth/setCurrentUser', null)
        return router.push('/login', null, null)
      }
      return Promise.reject(error);
    });

const routes = [
  {
    path: '/',
    name: 'home',
    component: Board,
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/board/:pageNumber',
    name: 'board',
    component: Board,
    props: (route) => ({ pageNumber: route.params.pageNumber }),
  },
  {
    path: '/post/create',
    name: 'create',
    component: EditPost,
    props: {mode: 'create'},
    meta: { authorize: ["USER"] }
  },
  {
    path: '/post/:postId/edit',
    name: 'edit',
    component: EditPost,
    props: (route) => ({ mode: 'modify', postId: route.params.postId }),
    meta: { authorize: ["USER"] }
  },
  {
    path: '/post/:postId',
    name: 'view',
    component: ViewPost,
    props: (route) => ({ postId: route.params.postId })
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../components/about/About.vue')
  },
  { path: '*', component: NotFound }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  const { authorize } = to.meta;

  if (authorize) {
    if (!store.getters['auth/loggedIn']) {
      return next({ path: '/login', query: { redirectFrom: to.path } });
    }

    const currentUser = store.state.auth.currentUser
    if (authorize.length &&
        !currentUser.authorities.some(x => authorize.includes(x))) {

      console.info(`Authorize failed: path=${to.path}, userAuthorties=${currentUser.authorities}, needAuthorize=${authorize}`);
      return next({ path: '/' });
    }
  }

  next();
});

export default router
