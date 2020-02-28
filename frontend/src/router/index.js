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

axios.interceptors.response.use(response => response, error => {
  const status = error.response ? error.response.status : null;
  if (status === HttpStatus.BAD_REQUEST) {
    return alert(JSON.stringify(error.response))
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

//
// router.beforeEach((routeTo, routeFrom, next) => {
//   // If this isn't an initial page load...
//   console.log(routeFrom)
//   console.log(routeTo)
//
//   if (routeFrom.name !== null) {
//     // Start the route progress bar.
//     //NProgress.start()
//   }
//
//   // Check if auth is required on this route
//   // (including nested routes).
//   const authRequired = routeTo.matched.some((route) => route.meta.authRequired)
//
//   // If auth isn't required for the route, just continue.
//   if (!authRequired) return next()
//
//   // If auth is required and the user is logged in...
//   if (store.getters['auth/loggedIn']) {
//     // Validate the local user token...
//     return store.dispatch('auth/validate').then((validUser) => {
//       // Then continue if the token still represents a valid user,
//       // otherwise redirect to login.
//       validUser ? next() : redirectToLogin()
//     })
//   }
//
//   // If auth is required and the user is NOT currently logged in,
//   // redirect to login.
//   redirectToLogin()
//
//   function redirectToLogin() {
//     // Pass the original route to the login component
//     next({ name: 'login', query: { redirectFrom: routeTo.fullPath } })
//   }
// })
//
// router.beforeResolve(async (routeTo, routeFrom, next) => {
//   // Create a `beforeResolve` hook, which fires whenever
//   // `beforeRouteEnter` and `beforeRouteUpdate` would. This
//   // allows us to ensure data is fetched even when params change,
//   // but the resolved route does not. We put it in `meta` to
//   // indicate that it's a hook we created, rather than part of
//   // Vue Router (yet?).
//
//   try {
//     // For each matched route...
//     for (const route of routeTo.matched) {
//       await new Promise((resolve, reject) => {
//         // If a `beforeResolve` hook is defined, call it with
//         // the same arguments as the `beforeEnter` hook.
//         if (route.meta && route.meta.beforeResolve) {
//           route.meta.beforeResolve(routeTo, routeFrom, (...args) => {
//             // If the user chose to redirect...
//             if (args.length) {
//               // If redirecting to the same route we're coming from...
//               if (routeFrom.name === args[0].name) {
//                 // Complete the animation of the route progress bar.
//                 //NProgress.done()
//               }
//               // Complete the redirect.
//               next(...args)
//               reject(new Error('Redirected'))
//             } else {
//               resolve()
//             }
//           })
//         } else {
//           // Otherwise, continue resolving the route.
//           resolve()
//         }
//       })
//     }
//     // If a `beforeResolve` hook chose to redirect, just return.
//   } catch (error) {
//     return
//   }
//
//   // If we reach this point, continue resolving the route.
//   next()
// })
//
// // When each route is finished evaluating...
// router.afterEach((routeTo, routeFrom) => {
//   // Complete the animation of the route progress bar.
//   //NProgress.done()
// })

export default router
