package project.pandemie.data;

import java.util.*;

public class Round {

    public static class Builder {
        private int round;
        private String outcome;
        private int points;
        private CityWrapper cityWrapper;
        private Collection<Events> events;

        public Builder(int round, String outcome) {
            this.round = round;
            this.outcome = outcome;
        }

        public Builder withPoints(int points) {
            this.points = points;
            return this;
        }

        public Builder withCities(CityWrapper cityWrapper) {
            this.cityWrapper = cityWrapper;
            return this;
        }

        public Builder withEvents(Collection<Events> events) {
            this.events = events;
            return this;
        }

        public Round build() {
            Round r = new Round();
            r.round = round;
            r.outcome = outcome;
            r.points = points;
            r.cityWrapper = cityWrapper;
            r.events = events;
//            r.worldPopulation = calculatePopulation();
//            r.infectedPopulation = calculateInfected();
            r.pathogens = getPathogens();

            return r;
        }

        private List<Pathogen> getPathogens() {
            List<Pathogen> pathogenList = new ArrayList<>();

            for (Events e : events) {
                if (e.getPathogen() != null) {
                    pathogenList.add(e.getPathogen());
                }
            }
            return pathogenList;
        }

    }

    private Round() {
    }

    private String outcome;
    private int round;
    private int points;
    private CityWrapper cityWrapper;
    private Collection<Events> events;
    private List<Pathogen> pathogens;


    public String getOutcome() {
        return outcome;
    }

    public int getRound() {
        return round;
    }

    public int getPoints() {
        return points;
    }

    public Map<String, City> getCities() {
        return cityWrapper.getCities();
    }

    public Collection<Events> getEvents() {
        return events;
    }

    public CityWrapper getCityWrapper() {
        return cityWrapper;
    }

    public Collection<Pathogen> getPathogens() {
        return pathogens;
    }


    public Collection<City> getInfectedCities() {
        Collection<City> col = new ArrayList<>();
        for (City c : cities.values()) {
            if (c.isInfected()) {
                col.add(c);
            }
        }
        return col;
    }

    public Collection<City> getInfectedCitiesbyScore(boolean descending) {
        Collection<City> col = new ArrayList<>();
        for (City c : getCityByScore(descending)) {
            if (c.isInfected()) {
                col.add(c);
            }
        }
        return col;
    }

    public Collection<City> getCityByScore(boolean descending) {
        for (City c : cities.values()) {

            /*
            Smaller cities are less important
             */
            if (c.getPopulation() < getBiggestPopulation() * 0.6) {
                c.setScore(c.getScore() + 1);
            } else if (c.getPopulation() < getBiggestPopulation() * 0.3) {
                c.setScore(c.getScore() + 2);
            }

            /*
            Cities that are not infected yet are more important
             */
            if (!c.isInfected()) {
                c.setScore(c.getScore() - 2);
            } else {
                /*
                Cities with low infection % are more at risk
                 */
                float infected = (((float) c.getInfectedPopulation()) / ((float) c.getPopulation()) * 100f);
                if (infected < 20) {
                    c.setScore(c.getScore() - 2);
                }
            }
        }

        ArrayList<City> col = new ArrayList<>(cities.values());

        col.sort((a, b) -> a.getScore() - b.getScore());
        if (descending) {
            col.sort(Collections.reverseOrder());
        }

        return col;
    }

    @Override
    public String toString() {
        return "Round{" +
                "outcome='" + outcome + '\'' +
                ", round=" + round +
                ", points=" + points +
                ", worldPopulation=" + worldPopulation +
                ", infectedPopulation=" + infectedPopulation +
                ", percentInfected=" + getPercentInfected() +
                '}';
    }
}
