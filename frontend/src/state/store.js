/* eslint-disable no-unused-vars */
/* eslint-disable no-console */
import Vue from 'vue'
import Vuex from 'vuex'
import modules from './modules'
import dispatchActionForAllModules from "./dispatch-action-for-all-modules";

Vue.use(Vuex)

const store = new Vuex.Store({
  modules,
  strict: process.env.NODE_ENV !== 'production',
})

export default store

dispatchActionForAllModules('init')