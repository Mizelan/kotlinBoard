import Vue from 'vue'
import VueRouter from 'vue-router'
import Board from '../components/board/Board.vue'
import Login from '../components/user/Login.vue'
import EditPost from '../components/post/EditPost.vue'
import ViewPost from '../components/post/ViewPost.vue'
import NotFound from '../components/errors/NotFound.vue'
import store from '../state/store'

Vue.use(VueRouter)

const requireAuth = () => (to, from, next) => {
  if (store.getters['auth/isAuthenticated']) {
    return next();
  }

  next('/login')
}

const requireAuthAdmin = () => (to, from, next) => {
  if (store.getters['auth/isAdmin']) {
    return next();
  }

  next('/login')
}

const routes = [
  {
    path: '/',
    name: 'home',
    component: Board,
    beforeEnter: requireAuth()
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
    beforeEnter: requireAuth()
  },
  {
    path: '/post/create',
    name: 'create',
    component: EditPost,
    props: {mode: 'create'},
    beforeEnter: requireAuthAdmin()
  },
  {
    path: '/post/:postId/edit',
    name: 'edit',
    component: EditPost,
    props: (route) => ({ mode: 'modify', postId: route.params.postId })
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
})

export default router
