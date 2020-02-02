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

  import SimpleMDE from 'simplemde';
  import 'simplemde/dist/simplemde.min.css'

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

      const postId = this.postId
      this.$store.dispatch('READ_POST', {postId})
      .then(result => {
        this.title = result.title;
        this.simpleMde.value(result.content)
      })
      .catch(({message}) => {
        this.$log.error("err : ", message);
      });
    },
    methods: {
      saveData() {
        const title = this.title;
        const content = this.simpleMde.value();

        if (this.mode === 'create') {
          this.$store.dispatch('CREATE_POST', {title, content})
            .then(result => {
              if (result.status === 200) {
                router.push('/')
              }
            })
            .catch(({message}) => {
              this.$log.error("err : ", message);
            });
        } else if (this.mode === 'modify') {
          const postId = this.postId
          this.$store.dispatch('UPDATE_POST', {postId, title, content})
            .then(result => {
              if (result.status === 200) {
                router.push('/')
              }
            })
            .catch(({message}) => {
              this.$log.error("err : ", message);
            });
        }
      }
    }
  }
</script>

<style scoped>

</style>
