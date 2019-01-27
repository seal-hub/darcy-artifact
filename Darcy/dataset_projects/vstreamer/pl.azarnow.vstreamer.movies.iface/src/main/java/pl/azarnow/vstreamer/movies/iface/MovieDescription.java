package pl.azarnow.vstreamer.movies.iface;

import pl.azarnow.vstreamer.people.iface.Person;

import java.time.Duration;
import java.util.Set;

public class MovieDescription {

    private final String name;

    private final int year;

    private final Duration duration;

    private final Person director;

    private final Set<Person> cast;

    private final Set<Category> categories;

    private MovieDescription(String name, int year, Duration duration, Person director, Set<Person> cast, Set<Category> categories) {
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.director = director;
        this.cast = cast;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public Duration getDuration() {
        return duration;
    }

    public Person getDirector() {
        return director;
    }

    public Set<Person> getCast() {
        return cast;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private int year;
        private Duration duration;
        private Person director;
        private Set<Person> cast;
        private Set<Category> categories;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withYear(int year) {
            this.year = year;
            return this;
        }

        public Builder withDirector(Person director) {
            this.director = director;
            return this;
        }

        public Builder withDuration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder withCast(Set<Person> cast) {
            this.cast = cast;
            return this;
        }

        public Builder withCategories(Set<Category> categories) {
            this.categories = categories;
            return this;
        }

        public MovieDescription build() {
            return new MovieDescription(name, year, duration, director, cast, categories);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDescription that = (MovieDescription) o;
        return year == that.year &&
                java.util.Objects.equals(name, that.name) &&
                java.util.Objects.equals(duration, that.duration) &&
                java.util.Objects.equals(director, that.director) &&
                java.util.Objects.equals(cast, that.cast) &&
                java.util.Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {

        return java.util.Objects.hash(name, year, duration, director, cast, categories);
    }

    @Override
    public String toString() {
        return "MovieDescription{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", duration=" + duration +
                ", director=" + director +
                ", cast=" + cast +
                ", categories=" + categories +
                '}';
    }
}
