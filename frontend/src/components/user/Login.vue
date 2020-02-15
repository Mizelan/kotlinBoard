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
            <input type="email" id="user_id" name="userId" v-model="userId" class="form-control"
                   placeholder="User ID를 입력해주세요." required autofocus>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="passwd" v-model="passWd" class="form-control"
                   placeholder="비밀번호를 입력해주세요." required>
        </div>

        <button class="btn btn-lg btn-primary btn-block" @click="login">Sign in</button>

        <br>
        <div class="form-group text-center">
            <a href="#" class="mr-3" @click="openModal()">Sign Up</a>
            <a target="_blank" rel="noopener noreferrer" href="https://github.com/mizelan/kotlinBoard">GITHUB</a>
        </div>
    </div>
</template>

<script>
    import Signup from "./Signup";

    export default {
        name: "Login",
        components: {
            Signup
        },
        data() {
            return {
                userId: '',
                passWd: '',
            }
        },
        methods: {
            openModal() {
                this.$bvModal.show('my-modal')
            },
            closeModal() {
                this.$bvModal.hide('my-modal')
            },
            login() {
                const userId = this.userId;
                const passWd = this.passWd;

                this.$store.dispatch('LOGIN', {userId, passWd});
            }
        }
    }
</script>

<style scoped>

</style>