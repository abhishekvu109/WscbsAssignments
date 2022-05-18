<template>
  <div class="grid grid-cols-6 h-screen bg-gray-50">
    <div class="col-start-2 col-span-4 bg-white shadow-lg">
      <div class="w-full text-center py-4 text-gray-800">Web Services and Cloud-Based Systems</div>
      <div class="w-full text-center text-4xl p-1 text-blue-600 mb-4">Covid-19 Vaccine Tweets Analysis</div>

      <div class="grid grid-cols-2">
        <div class="flex justify-center">
          <form @change="setVaccine()" >
            <label for="vaccine" class="mr-3">Choose vaccine: </label>
            <select name="end-time" v-model="vaccine"
              class="bg-gray-100 shadow-xl rounded-md py-2 px-8 focus:outline-none">
              <option value="" disabled selected hidden>Vaccine</option>
              <option value="Pfizer">Pfizer</option>
              <option value="BioNTech">BioNTech</option>
              <option value="Moderna">Moderna</option>
              <option value="AstraZeneca">AstraZeneca</option>
              <option value="Sputnik">Sputnik V</option>
              <option value="Sinovac">Sinovac</option>
              <option value="Sinopharm">Sinopharm</option>
            </select>
          </form>
        </div>

        <div class="flex justify-around">
            <router-link :class="routeName == 'WordCloud' ? 'nav-active-btn' : 'nav-disactivated-btn'" 
              :to="{name: 'WordCloud' }">Word cloud</router-link>
            <router-link :class="routeName == 'TimeSerious' ? 'nav-active-btn' : 'nav-disactivated-btn'" 
              :to="{name: 'TimeSeries' }">Time series</router-link>
            <router-link :class="routeName == 'TopTen' ? 'nav-active-btn' : 'nav-disactivated-btn'" 
              :to="{name: 'TopTen' }">Top 10</router-link>
        </div>
      </div>

      <div class="px-16 py-4">
        <router-view />
      </div>

      <footer class="fixed inset-x-0 bottom-0 p-4 grid grid-cols-4 bg-gray-100">
        <div class="col-start-2 col-span-2 flex justify-around text-gray-500 text-lg">
          <div>Abhishek Choudhury</div>
          <div>Asror Akbarkhodjaev</div>
          <div>Shekhar Suman</div>
        </div>
      </footer>
    </div>
  </div>
</template>

<script>

import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'App',
  data() {
    return {
      vaccine: ''
    }
  },
  computed: {
    ...mapGetters('vaccine', ['getVaccine']),
    routeName(){
       return this.$route.name
    },
  },
  methods: {
    ...mapActions('vaccine', ['addVaccine']),

    setVaccine() {
      this.addVaccine(this.vaccine);
    }
  },
  mounted() {
    if (sessionStorage.getItem('vaccine')) {
      this.vaccine = sessionStorage.getItem('vaccine')
      this.setVaccine()
    }
  }
}
</script>
