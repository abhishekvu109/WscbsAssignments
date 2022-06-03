import axios from 'axios'

const state = {
    vaccine: '',
    wordcloud: '',
    timeserious: '',
    topten: Object
}

const getters = {
    getVaccine: (state) => state.vaccine,
    getWordcloud: (state) => state.wordcloud,
    getTimeserious: (state) => state.timeserious,
    getTopten: (state) => state.topten,
}

const actions = {
    // function for setting the vaccine
    addVaccine( { commit }, vaccine ) {
        sessionStorage.setItem('vaccine', vaccine)
        commit( 'setVaccine', vaccine );
    },

    // function for fetching the wordcloud
    async fetchWordcloud ( { commit } ) {
        await axios.get(`http://localhost:5050/wordcloud/`+state.vaccine).then((response)=> {
            if(response.data) {
                commit('setWordcloud', response.data.value)
            }
        })
    },

    // function for fetching the time series graph
    async fetchTimeserious ( { commit } ) {
        await axios.get(`http://localhost:5050/timeseries/`+state.vaccine).then((response)=> {
            if(response.data) {
                commit('setTimeserious', response.data.value)
            }
        })
    },

    // function for fetching the top ten positive and negative tweets
    async fetchTopten( { commit } ) {
        await axios.get(`http://localhost:5050/tweets/`+state.vaccine).then((response)=> {
            if(response.data) {
                commit('setTopten', JSON.parse(response.data))
            }
        })
    },
}

const mutations = {
    setVaccine: (state, vac) => state.vaccine = vac,
    setWordcloud: (state, wc) => state.wordcloud = wc,
    setTimeserious: (state, ts) => state.timeserious = ts,
    setTopten: (state, tt) => state.topten = tt,
}

export const vaccine = {
    state,
    getters,
    actions,
    mutations,
    namespaced: true
}