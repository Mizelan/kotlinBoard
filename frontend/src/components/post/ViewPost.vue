<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <h1 id="post_title" style="border-bottom: 1px solid gold; padding-bottom: 5px;">{{title}}</h1>
      </div>
      <div class="col-12 text-right">
          <span id="post_created_user_id" style="margin-right: 5px; font-weight: bolder;">
            {{postId}}
          </span>
        <span id="post_created_at">
            {{createdAt | formatDate}}
          </span>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <textarea id="bbsDetail"></textarea>
      </div>
    </div>
  </div>
</template>

<script>
  import store from '@/state/store'
  import SimpleMDE from 'simplemde';
  import 'simplemde/dist/simplemde.min.css'
  import FilterHelpers from '@/utils/filter-helper.js'
  import {mapActions} from "vuex";

  export default {
    name: "ViewPost",
    data() {
      return {
        simpleMde: null,
        title: '',
        createdAt: ''
      }
    },
    props: ['postId'],
    mounted() {
      this.tryReadPost();
    },
    filters: {
      formatDate: FilterHelpers.toLocalDateTimeString
    },
    methods: {
      ...mapActions('post', ['readPostList', 'readPost', 'createPost', 'updatePost']),
      tryReadPost: function () {
        this.simpleMde = new SimpleMDE({
          element: document.getElementById("bbsDetail"),
          spellChecker: false,
          toolbar: false,
        });

        const postId = this.postId;
        store.dispatch("post/readPost", {postId})
          .then(result => {
            this.title = result.title;
            this.simpleMde.value(result.content);
            this.simpleMde.togglePreview();
            this.createdAt = result.createdAt;
          });
      }
    }
  }
</script>

<style>
  .CodeMirror {
    border: none;
  }

  .editor-preview {
    background: #FFFFFF;
  }
</style>
<style scoped>

</style>
