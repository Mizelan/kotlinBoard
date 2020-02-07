import Vue from 'vue'
import VueRouter from 'vue-router'
import Board from '../components/board/Board.vue'
import EditPost from '../components/post/EditPost.vue'
import ViewPost from '../components/post/ViewPost.vue'
import NotFound from '../components/errors/NotFound.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Board
  },
  {
    path: '/board/:pageNumber',
    name: 'board',
    component: Board,
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
