<template>
    <div v-if="getVaccine">
        <div class="text-4xl text-green-700 mb-4">
            {{ getVaccine }}
        </div>
        <img :src="require(`../assets/${getVaccine}_wc.png`)">
    </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
    name: 'WordCloud',
    computed: {
        ...mapGetters('vaccine', ['getVaccine'])
    }
}
</script>