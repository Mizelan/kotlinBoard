<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link> |

      <!-- TODO: github ReadMe.md 를 표시하기-->
      <!-- TODO: 페이지 상단에 페이지 로딩 프로그레스바 추가하기-->
      <router-link to="/about">About</router-link>
    </div>
    <router-view/>
  </div>
</template>

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
