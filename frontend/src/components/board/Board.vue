<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-12">
        <div class="row">
          <div class="col-lg-10">
            <h2 class="title-1 m-b-25">게시판</h2>
          </div>
          <div class="col-lg-2 text-center">
            <router-link to="/post/create" class="btn btn-info">글쓰기</router-link>
          </div>
        </div>
        <div class="table-responsive table--no-card m-b-40">
          <table class="table table-striped table-sm">
            <thead class="thead-dark">
            <tr>
              <th style="max-width: 100px; width: 5%">#</th>
              <th style="width: 45%" class="text-center">제목</th>
              <th style="width: 15%" class="text-center">글쓴이</th>
              <th style="width: 10%" class="text-center">조회</th>
              <th style="width: 20%" class="text-center">날짜</th>
              <th style="width: 5%" class="text-center"></th>
            </tr>
            </thead>
            <tbody>

              <tr v-for="(data) in postList" v-bind:key="data.id">
                <td>{{data.id}}</td>
                <td class="text-center"><router-link :to="{path: `/post/${data.id}`}">{{data.title}}</router-link></td>
                <td class="text-center">{{data.authorName === null ? "(deleted user)" : data.authorName}}</td>
                <td class="text-center">{{0}}</td>
                <td class="text-center">{{data.createdAt | formatDate}}</td>
                <td class="text-center"><router-link :to="{path: `/post/${data.id}/edit`}" class="btn btn-sm btn-warning">수정</router-link></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row d-flex justify-content-center">
      <Pagination/>
    </div>
  </div>
</template>

<script>
  /* eslint-disable no-unused-vars */
  import {mapActions, mapState} from 'vuex'
  import store from '@/state/store'
  import FilterHelpers from '@/utils/filter-helper.js';
  import Pagination from '@/components/board/Pagination';

export default {
  name: "board",
  components: {
    Pagination
  },
  props: ['pageNumber'],
  computed: mapState('post', {
    postList: 'postList',
    pageInfo: 'pageInfo'
  }),
  methods: {
    ...mapActions('post', ['readPostList', 'readPost', 'createPost', 'updatePost']),
  },
  watch: {
    '$route' (to, from) {
      store.dispatch("post/readPostList", to.params.pageNumber || 1)
    }
  },
  beforeRouteEnter(to, from, next) {
    store.dispatch("post/readPostList", to.params.pageNumber || 1)

      .then(result => {
        next();
      });
  },
  filters: {
    formatShortString: FilterHelpers.toStringEllipsis,
    formatDate: FilterHelpers.toLocalDateTimeString
  },
};
</script>

<style scoped>
ul.timeline {
  list-style-type: none;
  position: relative;
}
ul.timeline:before {
  content: " ";
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
  content: " ";
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
