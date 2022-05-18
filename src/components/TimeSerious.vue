<template>
    <div v-if="getVaccine">
        <div class="text-4xl text-green-700 mb-4">
            {{ getVaccine }}
        </div>

        <div v-if="getTimeserious">
            <img :src="'data:image/jpeg;base64,'+getTimeserious" alt="">
        </div>
    </div>
</template>

<script>
import { mapGetters,mapActions } from 'vuex'

export default {
    name: 'TimeSerious',
    computed: {
        ...mapGetters('vaccine', ['getVaccine','getTimeserious'])
    },
    methods: {
        ...mapActions('vaccine', ['fetchTimeserious'])
    },
    watch: {
        getVaccine: {
            immediate: true,
            handler () {
                this.fetchTimeserious()
            }
        },
    }
}
</script>