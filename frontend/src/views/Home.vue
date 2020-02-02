<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-sm-12 offset-md-1 col-md-10 col-lg-10 offset-lg-1">
        <h4>
          게시판
          <span class="float-right">
            <router-link to="/bbs/write" class="btn btn-info btn-sm ml-2">
              글쓰기
            </router-link>
          </span>
        </h4>
        <ul class="timeline">
          <li v-for="(data) in postList" v-bind:key="data.id" class="border-bottom pb-1">
            <router-link :to="{path: '/post/'+ data.id}">{{data.title}}</router-link>

            <span href="#" class="float-right">{{data.createdAt | formatDate}}</span>
            <p class="mb-1 pt-2">
              {{data.content | formatShortString}}
            </p>
            <p>
              <span class="float-right">
                {{data.id}} |
                <router-link :to="{path: '/post/write/'+ data.id}"
                             class="btn btn-sm btn-warning">수정</router-link>
              </span>
            </p>
            <div class="clearfix"></div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
//import HelloWorld from '@/components/HelloWorld.vue'
import moment from 'moment'

export default {
  name: 'home',
  components: {
    //HelloWorld
  },
  data() {
    return {
      postList: {},
    }
  },
  created() {
    this.readPostList();
  },
  filters: {
    formatShortString: function(str) {
      if (str.length < 10)
        return str;
      
      return str.substring(0, 7) + "...";
    },
    formatDate: function(value) {
      if (value)
        return moment(String(value)).local().format("YYYY/MM/DD hh:mm:ss")
    }
  },
  methods: {
    readPostList() {
      this.$store.dispatch('GET_POST_LIST', {})
        .then((data) => {
          this.postList = data.postList;
        })
        .catch(({message}) => {
          this.$log.error("message : ", message);
        });
    }
  }
}
</script>

<style scoped>
  ul.timeline {
    list-style-type: none;
    position: relative;
  }
  ul.timeline:before {
    content: ' ';
    background: #d4d9df;
    display: inline-block;
    position: absolute;
    left: 29px;
    width: 2px;
    height: 100%;
    z-index: 400;
  }
  ul.timeline > li {
    margin: 20px 0;
    padding-left: 20px;
  }
  ul.timeline > li:before {
    content: ' ';
    background: white;
    display: inline-block;
    position: absolute;
    border-radius: 50%;
    border: 3px solid #22c0e8;
    left: 20px;
    width: 20px;
    height: 20px;
    z-index: 400;
  }
  ul.timeline hr {
    margin: 0px;
  }
</style>
