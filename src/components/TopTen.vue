<template>
    <div v-if="getVaccine">
        <div class="text-4xl text-green-700 mb-3">
            {{ getVaccine }}
        </div>

        <div v-if="getTopten">
            <div class="mt-5 mb-2 text-2xl text-gray-600">Top 10 most positively charged tweets</div>
            <table class="w-full border-collapse border border-gray-400 table-auto">
                <thead>
                    <tr>
                        <th class="border border-gray-300 bg-green-50 w-1/2">Text</th>
                        <th class="border border-gray-300 bg-green-50 w-1/4">Polarity</th>
                        <th class="border border-gray-300 bg-green-50 w-1/4">Date</th>
                    </tr>
                </thead>
                <tbody :key="index" v-for="(tweet,index) in getTopten.positive">
                    <tr>
                        <td class="border border-gray-400 p-1">{{ tweet.tweet }}</td>
                        <td class="border border-gray-400 p-1">{{ tweet.polarity }}</td>
                        <td class="border border-gray-400 p-1">{{ tweet.date }}</td>
                    </tr>
                </tbody>
            </table>

            <div class="mt-10 mb-2 text-2xl text-gray-600">Top 10 most negatively charged tweets</div>
            <table class="w-full border-collapse border border-gray-400 table-auto mb-10">
                <thead>
                    <tr>
                        <th class="border border-gray-300 bg-red-50 w-1/2">Text</th>
                        <th class="border border-gray-300 bg-red-50 w-1/4">Polarity</th>
                        <th class="border border-gray-300 bg-red-50 w-1/4">Date</th>
                    </tr>
                </thead>
                <tbody :key="index" v-for="(tweet,index) in getTopten.negative">
                    <tr>
                        <td class="border border-gray-400 p-1">{{ tweet.tweet }}</td>
                        <td class="border border-gray-400 p-1">{{ tweet.polarity }}</td>
                        <td class="border border-gray-400 p-1">{{ tweet.date }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import { mapGetters,mapActions } from 'vuex'

export default {
    name: 'TopTen',
    computed: {
        ...mapGetters('vaccine', ['getVaccine','getTopten'])
    },
    methods: {
        ...mapActions('vaccine', ['fetchTopten'])
    },
    watch: {
        getVaccine: {
            immediate: true,
            handler () {
                this.fetchTopten()
            }
        },
    }
}
</script>