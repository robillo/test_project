package com.robillo.test_project.network.model;

import java.util.List;

/**
 * Created by robinkamboj on 17/02/18.
 */

public class AllDetails {

    private List<WorldpopulationBean> worldpopulation;

    public List<WorldpopulationBean> getWorldpopulation() {
        return worldpopulation;
    }

    public void setWorldpopulation(List<WorldpopulationBean> worldpopulation) {
        this.worldpopulation = worldpopulation;
    }

    public static class WorldpopulationBean {
        /**
         * rank : 1
         * country : China
         * population : 1,354,040,000
         * flag : http://www.androidbegin.com/tutorial/flag/china.png
         */

        private int rank;
        private String country;
        private String population;
        private String flag;

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPopulation() {
            return population;
        }

        public void setPopulation(String population) {
            this.population = population;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
