<template>
    <header>
        <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3">
            <router-link to="/" class="navbar-brand">
                kotlinBoard
            </router-link>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav mr-auto" >
                    <li class="nav-item dropdown" v-if="isAdmin">
                        <a class="nav-link dropdown-toggle" id="dropdown01" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            ADMIN
                        </a>
                        <div class="dropdown-menu" aria-labelledby="dropdown01">
                            <router-link class="dropdown-item" to="/admin/category">Category</router-link>
                            <router-link class="dropdown-item" to="/admin/userManage">UserManage</router-link>
                        </div>
                    </li>
                </ul>
                <div class="form-inline mt-2 mt-md-0">
                    <button class="btn btn-outline-info btn-sm" @click="tryLoginOrLogout">
                        {{loggedIn ? "로그아웃" : "로그인"}}
                    </button>
                </div>
            </div>
        </nav>
    </header>
</template>

<script>
    import {mapActions, mapGetters, mapState} from "vuex";

    export default {
        name: "Gnb",
        data() {
            return {}
        },
        computed: {
            ...mapState('auth', {
                currentUser: (state) => state.currentUser,
            }),
            ...mapGetters('auth', ['loggedIn', 'isAdmin', 'isUser']),
        },
        methods: {
            ...mapActions('auth', ['logIn', 'logOut', 'signUp']),
            tryLoginOrLogout() {
                if (this.loggedIn) {
                    this.logOut()
                        .then(() => {
                            if (this.$router.currentRoute.name !== 'home') {
                                this.$router.push({ name: 'home' })
                            }
                    });
                }
                else {
                    if (this.$router.currentRoute.name !== 'login') {
                        this.$router.push({ name: 'login' })
                    }
                }
            }
        }
    }
</script>

<style scoped>

</style>