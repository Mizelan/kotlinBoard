<template>
  <div id="app">
    <!-- TODO: github ReadMe.md 를 표시하기-->
    <!-- TODO: 페이지 상단에 페이지 로딩 프로그레스바 추가하기-->
<!--    <div id="nav">-->
<!--      <router-link to="/">Home</router-link> |-->
<!--      <router-link to="/about">About</router-link>-->
<!--    </div>-->
    <gnb/>
    <router-view/>
  </div>
</template>

<script>
  import Gnb from '@/components/Gnb';

  export default {
    name: "App",
    components: {
      Gnb
    },
  }
</script>

<style lang="scss">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  //text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
