<template>
  <div class="row">
    <div class="col">
      <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
          <li v-if=!isFirstPageGroup() class="page-item">
            <a class="page-link" @click="movePage(pageInfo.firstPage)" tabindex="-1">&laquo;</a>
          </li>
          <li v-if=!isFirstPageGroup() class="page-item">
            <a class="page-link" @click="movePage(pageInfo.prevPage)" tabindex="-1">&lt;</a>
          </li>
          <li
            v-for="pageNumber in pageInfo.pageList"
            class="page-item"
            :class="{active: isActivePage(pageNumber)}"
            v-bind:key="pageNumber"
          >
            <router-link :to="`/board/${pageNumber}`">
              <a class="page-link">{{pageNumber}}</a>
            </router-link>
          </li>

          <li v-if=!isLastPageGroup() class="page-item">
            <a class="page-link" @click="movePage(pageInfo.nextPage)" tabindex="-1">&gt;</a>
          </li>
          <li v-if=!isLastPageGroup() class="page-item">
            <a class="page-link" @click="movePage(pageInfo.lastPage)" tabindex="-1">&raquo;</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script>
import {mapActions, mapState} from 'vuex'

export default {
  name: "Pagination",
  computed: mapState('post', {
    pageInfo: 'pageInfo'
  }),
  methods: {
    ...mapActions('post', ['readPostList', 'readPost', 'createPost', 'updatePost']),
    movePage(pageIndex) {
      this.readPostList(pageIndex);
    },
    isActivePage(pageNumber) {
      return pageNumber === this.pageInfo.currentPage;
    },
    isFirstPageGroup() {
      return this.pageInfo.currentPageGroup === 0;
    },
    isLastPageGroup() {
      return this.pageInfo.currentPageGroup === this.pageInfo.lastPageGroup;
    }
  }
};
</script>

<style scoped>
</style>
