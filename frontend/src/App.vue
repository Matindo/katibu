<template>
  <div id="app">
    <NavBar />
    <router-view />
    <AppFooter />
    <AppToast />
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'
import AppFooter from './components/AppFooter.vue'
import AppToast from './components/AppToast.vue'

export default {
  name: 'App',
  components: { NavBar, AppFooter, AppToast }
}
</script>
