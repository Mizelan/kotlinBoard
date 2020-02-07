import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import EditPost from '../views/EditPost.vue'
import ViewPost from '../views/ViewPost.vue'
import NotFound from '../views/Errors/NotFound.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/board/:pageNumber',
    name: 'board',
    component: Home,
    props: (route) => ({ pageNumber: route.params.pageNumber })
  },
  {
    path: '/post/create',
    name: 'create',
    component: EditPost,
    props: {mode: 'create'}
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
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  { path: '*', component: NotFound }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
