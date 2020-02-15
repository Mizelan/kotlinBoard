/* eslint-disable no-unused-vars */
/* eslint-disable no-console */
import VueJwtDecode from "vue-jwt-decode";
import axios from "axios";
import HttpStatus from "http-status-codes";
import router from "../../router";

const jwtTokenName = "jws";

const postModule = {
    state: {
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
    },
    mutations: {
        UPDATE_PAGINATION(state, responseData) {
            const pageInfo = responseData.pageInfo;
            state.postList = responseData.postList;
            state.pageInfo.currentPage = pageInfo.currentPage
            state.pageInfo.currentPageGroup = Math.floor((pageInfo.currentPage - 1) / pageInfo.countOfPage);
            state.pageInfo.lastPageGroup = Math.floor((pageInfo.maxPage - 1) / pageInfo.countOfPage);
            state.pageInfo.firstPage = 1;
            state.pageInfo.lastPage = pageInfo.maxPage - 1;
            state.pageInfo.prevPage = Math.max(1, (state.pageInfo.currentPageGroup * pageInfo.countOfPage));
            state.pageInfo.nextPage = Math.min( pageInfo.maxPage, ((state.pageInfo.currentPageGroup + 1) * pageInfo.countOfPage) + 1);

            const showingPageCount = state.pageInfo.currentPageGroup === state.pageInfo.lastPageGroup ?
                (pageInfo.maxPage - 1) % pageInfo.countOfPage :
                pageInfo.countOfPage;
            const showingStartPage = state.pageInfo.currentPageGroup * pageInfo.countOfPage + 1;

            state.pageInfo.pageList = Array.from(Array(showingPageCount), (_, i) => i + showingStartPage);
        }
    },
    actions: {
        READ_POST_LIST({state, commit}, toPage) {
            return axios.get('/api/post', {params: {page: toPage, postCount: state.postCountPerPage}})
                .then(response => {
                    if (response.status === HttpStatus.OK) {
                        commit('UPDATE_PAGINATION', response.data);
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
    }
}

export default postModule