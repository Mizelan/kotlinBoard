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
        updatePagination(state, responseData) {
            const pageInfo = responseData.pageInfo;
            state.postList = responseData.postList;
            pageInfo.currentPage++;
            state.pageInfo.currentPage = pageInfo.currentPage
            state.pageInfo.currentPageGroup = Math.floor((pageInfo.currentPage - 1) / pageInfo.countOfPage);
            state.pageInfo.lastPageGroup = Math.floor((pageInfo.maxPage) / pageInfo.countOfPage);
            state.pageInfo.firstPage = 1;
            state.pageInfo.lastPage = pageInfo.maxPage;
            state.pageInfo.prevPage = Math.max(1, (state.pageInfo.currentPageGroup * pageInfo.countOfPage));
            state.pageInfo.nextPage = Math.min( pageInfo.maxPage, ((state.pageInfo.currentPageGroup + 1) * pageInfo.countOfPage) + 1);

            const showingPageCount = state.pageInfo.currentPageGroup === state.pageInfo.lastPageGroup ?
                pageInfo.maxPage % pageInfo.countOfPage :
                pageInfo.countOfPage;
            const showingStartPage = state.pageInfo.currentPageGroup * pageInfo.countOfPage + 1;

            state.pageInfo.pageList = Array.from(Array(showingPageCount), (_, i) => i + showingStartPage);
        }
    },
    actions: {
        readPostList({state, commit}, toPage) {
            return axios.get('/api/post', {params: {page: toPage, postCount: state.postCountPerPage}})
                .then(response => {
                    if (response.status === HttpStatus.OK) {
                        commit('updatePagination', response.data);
                        return response.data
                    }
                });
        },
        readPost(context, {postId}) {
            return axios.get(`/api/post/${postId}`)
                .then(response => {
                    if (response.status === HttpStatus.OK)
                        return response.data
                });
        },
        createPost(context, {title, content}) {
            return axios.post('/api/post', {
                title, content
            })
                .then(response => {
                    return response;
                });
        },
        updatePost(context, {postId, title, content}) {
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