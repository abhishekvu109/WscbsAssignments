import { createApp } from 'vue'
import App from './App.vue'
import './index.css'
import router from './router' 

import { createStore } from 'vuex'
import { vaccine } from './store/vaccine.js'

const store = createStore({
	modules: {
        vaccine
	}
});

const app = createApp(App)

app.use(store).use(router).mount('#app')
