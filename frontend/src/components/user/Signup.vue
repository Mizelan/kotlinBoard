<template>
    <div>
        <div class="modal-body">
            <div class="form-group">
                <label for="signup_user_id">User Id</label>
                <input type="email" id="signup_user_id" v-model="username" class="form-control" placeholder="User ID를 입력해주세요." required autofocus>
            </div>
            <div class="form-group">
                <label for="signup_password">Password</label>
                <input type="password" id="signup_password" v-model="password" class="form-control" placeholder="비밀번호를 입력해주세요." required>
            </div>
            <div class="form-group">
                <label for="signup_password_confirm">Password</label>
                <input type="password" id="signup_password_confirm" v-model="confirmPassword" class="form-control" placeholder="비밀번호를 한번 더 입력해주세요."
                       required>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="$emit('closeModal', true)">닫기</button>
            <button type="button" class="btn btn-primary" @click="trySignUp">신청</button>
        </div>
    </div>
</template>

<script>
    import HttpStatus from 'http-status-codes'
    import {mapActions} from "vuex";

    export default {
        name: "Signup",
        data() {
            return {
                username: '',
                password: '',
                confirmPassword: ''
            }
        },
        methods: {
            ...mapActions('auth', ['logIn', 'logOut', 'signUp']),
            trySignUp() {
                const username = this.username;
                const password = this.password;
                const confirmPassword = this.confirmPassword;

                this.signUp({
                    username: username,
                    password: password,
                    confirmPassword: confirmPassword})
                .then(response => {
                    if (response.status === HttpStatus.CREATED) {
                        this.$emit('closeModal', true);
                        this.$router.push(this.$route.query.redirectFrom || { name: 'home' })
                    }
                })
            }
        }

    }
</script>

<style scoped>

</style>