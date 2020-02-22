<template>
  <div class="container-fluid">
    <div class="row mb-3">
      <div class="col-lg-9 col-md-12">
        <input type="text" class="form-control" placeholder="제목을 입력해주세요." v-model="title"/>
      </div>
      <div class="col-lg-3 col-md-12">
      </div>
    </div>
    <div class="row">
      <div class="col">
        <textarea id="post_textarea"></textarea>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <button type="button" class="btn btn-block btn-outline-success" @click="saveData">
          저장
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  import router from '@/router'
  import store from '@/state/store'
  import SimpleMDE from 'simplemde';
  import 'simplemde/dist/simplemde.min.css'
  import HttpStatus from 'http-status-codes'
  import {mapActions} from "vuex";

  export default {
    data() {
      return {
        title: '',
        simpleMde: null
      }
    },
    props: ['mode', 'postId'],
    mounted() {
      this.simpleMde = new SimpleMDE({
        element: document.getElementById("post_textarea"),
        spellChecker: false,
      });

      if (this.mode === 'modify') {
        this.tryReadPost(this.postId);
      }      
    },
    methods: {
      ...mapActions('post', ['readPostList', 'readPost', 'createPost', 'updatePost']),
      saveData() {
        const title = this.title;
        const content = this.simpleMde.value();
        const postId = this.postId

        switch (this.mode) {
          case 'create':
            this.tryCreatePost({title, content});
            break;
          case 'modify':
            this.tryUpdatePost({postId, title, content});
            break;
          default:
            this.$log.error(`unknown edit post mode ${this.mode}.`);
        }
      },
      async returnToHomePath() {
        await router.push('/')
      },
      tryReadPost(postId) {
        store.dispatch("post/readPost", {postId})
        .then(result => {
          this.title = result.title;
          this.simpleMde.value(result.content);
        });
      },
      tryCreatePost(params) {
        this.createPost(params)
        .then(result => {
          if (result.status === HttpStatus.OK)
            this.returnToHomePath()
        });
      },
      tryUpdatePost(params) {
        this.updatePost(params)
        .then(result => {
          if (result.status === HttpStatus.OK)
            this.returnToHomePath()
        });
      }
    }
  }
</script>

<style scoped>

</style>
