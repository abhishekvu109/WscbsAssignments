<template>
    <div v-if="getVaccine">
        <div class="text-4xl text-green-700 mb-4">
            {{ getVaccine }}
        </div>

        <div v-if="getWordcloud">
            <img :src="'data:image/jpeg;base64,'+getWordcloud" alt="">
        </div>
    </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
    name: 'WordCloud',
    computed: {
        ...mapGetters('vaccine', ['getVaccine','getWordcloud'])
    },
    methods: {
        ...mapActions('vaccine', ['fetchWordcloud'])
    },
    watch: {
        getVaccine: {
            immediate: true,
            handler () {
                this.fetchWordcloud()
            }
        },
    }
}
</script>