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
            <button type="button" class="btn btn-primary" @click="signup">신청</button>
        </div>
    </div>
</template>

<script>
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
            signup() {
                const userId = this.userId;
                const passWd = this.passWd;
                const passWdConfirm = this.passWdConfirm;

                this.$store.dispatch('auth/SIGNUP', {userId, passWd, passWdConfirm})
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