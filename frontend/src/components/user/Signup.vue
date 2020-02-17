<template>
    <div>
        <div class="modal-body">
            <div class="form-group">
                <label for="signup_user_id">User Id</label>
                <input type="email" id="signup_user_id" v-model="userId" class="form-control" placeholder="User ID를 입력해주세요." required autofocus>
            </div>
            <div class="form-group">
                <label for="signup_password">Password</label>
                <input type="password" id="signup_password" v-model="passWd" class="form-control" placeholder="비밀번호를 입력해주세요." required>
            </div>
            <div class="form-group">
                <label for="signup_password_confirm">Password</label>
                <input type="password" id="signup_password_confirm" v-model="passWdConfirm" class="form-control" placeholder="비밀번호를 한번 더 입력해주세요."
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
    import { authMethods } from '@/state/helpers'
    import HttpStatus from 'http-status-codes'

    export default {
        name: "Signup",
        data() {
            return {
                userId: '',
                passWd: '',
                passWdConfirm: ''
            }
        },
        methods: {
            ...authMethods,
            trySignUp() {
                const userId = this.userId;
                const passWd = this.passWd;
                const passWdConfirm = this.passWdConfirm;

                this.signUp({
                    userId: userId,
                    passWd: passWd,
                    confirmPassWd: passWdConfirm})
                .then(result => {
                    console.log(result)
                    if (result.status === HttpStatus.CREATED) {
                        this.$emit('closeModal', true);
                    }
                })
            }
        }

    }
</script>

<style scoped>

</style>