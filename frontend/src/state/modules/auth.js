/* eslint-disable no-unused-vars */
/* eslint-disable no-console */
import VueJwtDecode from "vue-jwt-decode";
import axios from "axios";
import HttpStatus from "http-status-codes";
import router from "../../router";

const jwtTokenName = "jws";

const authModule = {
    namespaced: true,
    state: {
    },
    getters: {
        checkAuthority: (state, getters) => (targetAuthority) => {
            return getters.readAuthorities.includes(targetAuthority);
        },
        isAdmin(state, getters) {
            console.log(getters)
            return getters.checkAuthority("ADMIN")
        },
        isUser(state, getters) {
            return getters.checkAuthority("USER")
        },
        isAuthenticated(state, getters) {
            return !!getters.readAuthToken;
        },
        readAuthToken() {
            return localStorage.getItem(jwtTokenName)
        },
        readAuthorities(getters) {
            try {
                const jwtToken = getters.readAuthToken;
                return jwtToken ? VueJwtDecode.decode(jwtToken).authorities.split(',') : [];
            } catch (error) {
                console.error(error)
                return [];
            }
        }
    },
    mutations: {
        LOGIN(state, {data}) {
            localStorage.setItem(jwtTokenName, data.data)
        },
        LOGOUT(state) {
            localStorage.removeItem(jwtTokenName)
        }
    },
    actions: {
        LOGIN({commit}, {userId, passWd}) {
            let form = new FormData();
            form.append('userId', userId);
            form.append('passWd', passWd);

            return axios.post('/api/user/login', form)
                .then(async ({data}) => {
                    commit('LOGIN', {data});
                    console.log(1)
                    await router.push("/")
                    console.log(2)
                })
                .catch((error) => {
                    console.log(`login error: ${error}`);
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
                }).catch((error) => {
                    console.log(`signup error: + ${error}`);
                });
        },
        async LOGOUT({commit}) {
            await router.push('/login')
        }
    },
}

export default authModule