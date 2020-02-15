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
                    <li class="nav-item dropdown" v-if="$store.getters.isAdmin">
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
                    <button class="btn btn-outline-info btn-sm" @click="로그아웃">
                        LogOut
                    </button>
                </div>
            </div>
        </nav>
    </header>
</template>

<script>
    export default {
        name: "Gnb",
        data() {
            return {}
        },
        methods: {
            logout() {
                this.$store.dispatch('LOGOUT', {})
                    .then((data) => {
                        console.log(data);
                    })
                    .catch(({message}) => this.msg = message)
            }
        }
    }
</script>

<style scoped>

</style>