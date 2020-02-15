import Vue from 'vue'
import App from './components/App.vue'
import router from './router'
import store from './state/store'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import VueLogger from 'vuejs-logger';
import axios from 'axios'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

const isProduction = process.env.NODE_ENV === 'production';

Vue.use(VueLogger, {
  isEnabled: true,
  logLevel : isProduction ? 'error' : 'debug',
  stringifyArguments : false,
  showLogLevel : true,
  showMethodName : true,
  separator: '|',
  showConsoleColors: true
})
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)

if (!isProduction) {
  axios.interceptors.response.use(response => {
    console.log(response)
    return response;
  }, error => {
    console.error("axios response error : ", error);
    return Promise.reject(error)
  })
}

Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
