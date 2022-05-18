import { createWebHistory, createRouter } from "vue-router";
import TimeSerious from "@/components/TimeSerious.vue"
import TopTen from "@/components/TopTen.vue"
import WordCloud from "@/components/WordCloud.vue"

const routes = [
  {
    path: "/",
    name: "WordCloud",
    component: WordCloud,
  },
  {
    path: "/time-series",
    name: "TimeSeries",
    component: TimeSerious,
  },
  {
    path: "/top-ten",
    name: "TopTen",
    component: TopTen,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;