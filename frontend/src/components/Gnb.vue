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
                    <button class="btn btn-outline-info btn-sm" @click="tryLogOut">
                        {{loggedIn ? "로그아웃" : "로그인"}}
                    </button>
                </div>
            </div>
        </nav>
    </header>
</template>

<script>
    import { authMethods, authComputed } from '@/state/helpers'

    export default {
        name: "Gnb",
        data() {
            return {}
        },
        computed: {
            ...authComputed
        },
        methods: {
            ...authMethods,
            tryLogOut() {
                // TODO: 중복 네비게이트 오류가 발생하고 있다. routerHelper를 만들어 현재 경로를 확인하게 만들자.
                this.logOut().then(() => {
                    this.$router.push({ name: 'home' })
                });

            }
        }
    }
</script>

<style scoped>

</style>