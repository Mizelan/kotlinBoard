import { mapState, mapGetters, mapActions } from 'vuex'
import store from '@/state/store'

export const authComputed = {
    ...mapState('auth', {
        currentUser: (state) => state.currentUser,
    }),
    ...mapGetters('auth', ['loggedIn', 'isAdmin', 'isUser']),
}

export const authMethods = mapActions('auth', ['logIn', 'logOut', 'signUp'])

export const postComputed = {
    ...mapState('post', {
        pageInfo: (state) => state.pageInfo,
        postList: (state) => state.postList,
        postCountPerPage: (state) => state.postCountPerPage,
    }),
    ...mapGetters('post', []),
}

export const postMethods = mapActions('post', ['readPostList', 'readPost', 'createPost', 'updatePost'])

export function readFirstBoardPage(pageNumber) {
    return store.dispatch("post/readPostList", pageNumber)
}

export function readPost(postId) {
    return store.dispatch("post/readPost", {postId})
}