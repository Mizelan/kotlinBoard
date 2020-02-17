<template>
    <div class="form-signin bg-light mt-5 shadow-lg bg-white rounded">
        <b-modal hide-header-close hide-footer id="my-modal" title="회원 가입">
            <signup @closeModal="closeModal()"/>
        </b-modal>
        <div class="text-center mb-4">
            <h1 class="h3 mb-3 font-weight-normal">로그인</h1>
        </div>

        <div class="form-group">
            <label for="user_id">User Id</label>
            <input type="email" id="user_id" name="userId" v-model="username" class="form-control"
                   placeholder="User ID를 입력해주세요." required autofocus>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="passwd" v-model="password" class="form-control"
                   placeholder="비밀번호를 입력해주세요." required>
        </div>

        <button class="btn btn-lg btn-primary btn-block" @click="tryToLogIn">Sign in</button>

        <br>
        <div class="form-group text-center" :disabled="tryingToLogIn">
            <a href="#" class="mr-3" @click="openModal()">Sign Up</a>
            <a target="_blank" rel="noopener noreferrer" href="https://github.com/mizelan/kotlinBoard">GITHUB</a>
        </div>
    </div>
</template>

<script>
    import { authMethods } from '@/state/helpers'
    import Signup from "./Signup";

    export default {
        name: "Login",
        components: {
            Signup
        },
        data() {
            return {
                username: '',
                password: '',
                authError: null,
                tryingToLogIn: false,
            }
        },
        methods: {
            ...authMethods,
            openModal() {
                this.$bvModal.show('my-modal')
            },
            closeModal() {
                this.$bvModal.hide('my-modal')
            },
            tryToLogIn() {
                this.tryingToLogIn = true
                // Reset the authError if it existed.
                this.authError = null
                return this.logIn({
                    username: this.username,
                    password: this.password,
                })
                    .then(() => {
                        this.tryingToLogIn = false
                        console.log("loggin ok");
                        // Redirect to the originally requested page, or to the home page
                        this.$router.push(this.$route.query.redirectFrom || { name: 'home' })
                    })
                    .catch((error) => {
                        this.tryingToLogIn = false
                        this.authError = error
                    })
            },
        }
    }
</script>

<style scoped>

</style>