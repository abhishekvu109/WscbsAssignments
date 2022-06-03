<template>
    <div v-if="getVaccine">
        <div class="text-4xl text-green-700 mb-3">
            {{ getVaccine }}
        </div>

        <div v-if="toptens" class="mb-20">
            <div class="mt-5 text-2xl text-gray-600">Top 10 most positively charged tweets</div>
            <table class="w-full border-collapse border border-gray-400 table-auto">
                <thead>
                    <tr>
                        <th class="border border-gray-300 bg-green-50 w-1/2">Text</th>
                        <th class="border border-gray-300 bg-green-50 w-1/4">Polarity</th>
                        <th class="border border-gray-300 bg-green-50 w-1/4">Date</th>
                    </tr>
                </thead>
                <tbody :key="index" v-for="(tweet,index) in toptens.positive">
                    <tr>
                        <td class="border border-gray-400 p-1">{{ tweet.tweet }}</td>
                        <td class="border border-gray-400 p-1">{{ tweet.polarity }}</td>
                        <td class="border border-gray-400 p-1">{{ tweet.date }}</td>
                    </tr>
                </tbody>
            </table>

            <div class="mt-12 text-2xl text-gray-600">Top 10 most negatively charged tweets</div>
            <table class="w-full border-collapse border border-gray-400 table-auto mb-10">
                <thead>
                    <tr>
                        <th class="border border-gray-300 bg-red-50 w-1/2">Text</th>
                        <th class="border border-gray-300 bg-red-50 w-1/4">Polarity</th>
                        <th class="border border-gray-300 bg-red-50 w-1/4">Date</th>
                    </tr>
                </thead>
                <tbody :key="index" v-for="(tweet,index) in toptens.negative">
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
import { mapGetters } from 'vuex'
import Pfizer_tt from '../assets/Pfizer_tt.json'
import Moderna_tt from '../assets/Pfizer_tt.json'
import AstraZeneca_tt from '../assets/Pfizer_tt.json'
import Sputnik_tt from '../assets/Pfizer_tt.json'
import Sinovac_tt from '../assets/Pfizer_tt.json'
import Sinopharm_tt from '../assets/Pfizer_tt.json'

export default {
    name: 'TopTen',
    computed: {
        ...mapGetters('vaccine', ['getVaccine'])
    },
    data() {
        return {
            toptens: Object
        }
    },
    methods: {
        getJson() {
            if (this.getVaccine == 'Pfizer') this.toptens = Pfizer_tt;
            else if (this.getVaccine == 'Moderna') this.toptens = Moderna_tt;
            else if (this.getVaccine == 'AstraZeneca') this.toptens = AstraZeneca_tt;
            else if (this.getVaccine == 'Sputnik') this.toptens = Sputnik_tt;
            else if (this.getVaccine == 'Sinovac') this.toptens = Sinovac_tt;
            else if (this.getVaccine == 'Sinopharm') this.toptens = Sinopharm_tt;
        }
    },
    watch: {
        getVaccine: {
            immediate: true,
            handler () {
                this.getJson()
            }
        },
    }
}
</script>