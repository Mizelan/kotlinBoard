/* eslint-disable no-unused-vars */
/* eslint-disable no-console */
import VueJwtDecode from "vue-jwt-decode";
import axios from "axios";
import HttpStatus from "http-status-codes";
import router from "../../router";

export const state = {
    currentUser: getSavedState('auth.currentUser'),
};

export const mutations = {
    setCurrentUser(state, jwtToken) {
        if (jwtToken) {
            const decoded = VueJwtDecode.decode(jwtToken);
            state.currentUser = {}
            state.currentUser.name = decoded.sub;
            state.currentUser.authorities = decoded.authorities.split(',');
            state.currentUser.jwtToken = jwtToken;
            saveState('auth.currentUser', state.currentUser);
        } else {
            state.currentUser = null;
            deleteState('auth.currentUser');
        }

        setDefaultAuthHeaders(state);
    },
};

export const getters = {
    loggedIn(state) {
        return !!state.currentUser
    },
    isAdmin(state) {
        if (state.currentUser) {
            return state.currentUser.authorities.includes("ADMIN")
        } else {
            return false;
        }
    },
    isUser(state) {
        if (state.currentUser) {
            return state.currentUser.authorities.includes("USER")
        } else {
            return false;
        }
    },
};

export const actions = {
    init({ state, dispatch }) {
        setDefaultAuthHeaders(state)
    },
    logIn({ commit, state, dispatch, getters }, { username, password } = {}) {
        return axios
            .post('/api/user/login', { username: username, password: password })
            .then((response) => {
                const jwtToken = response.data.data;
                commit('setCurrentUser', jwtToken);
                return jwtToken;
            })
            .catch(error => {
               console.warn(`login error: ${error}`);
            });
    },
    logOut({ commit }) {
        commit('setCurrentUser', null)
    },
    signUp({commit}, {userId, passWd, confirmPassWd} = {}) {
        return axios
            .post('/api/user/signup', {userId, passWd, confirmPassWd})
            .then((response) => {
                return response;
            }).catch((error) => {
                console.log(`signup error: + ${error}`);
            });
    },
};

function getSavedState(key) {
    return JSON.parse(window.localStorage.getItem(key))
}

function saveState(key, state) {
    window.localStorage.setItem(key, JSON.stringify(state))
}

function deleteState(key, state) {
    window.localStorage.removeItem(key)
}

function setDefaultAuthHeaders(state) {
     axios.defaults.headers.common.Authorization =
         state.currentUser ? `Bearer ${state.currentUser.jwtToken}` : null;
}