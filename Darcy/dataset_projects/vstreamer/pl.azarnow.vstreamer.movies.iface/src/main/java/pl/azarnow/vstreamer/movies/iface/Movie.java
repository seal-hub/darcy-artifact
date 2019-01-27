package pl.azarnow.vstreamer.movies.iface;


import java.util.Arrays;
import java.util.UUID;

public class Movie {

    private final UUID id;

    private final MovieDescription description;

    private final byte[] content;

    public Movie(UUID id, MovieDescription description, byte[] content) {
        this.id = id;
        this.description = description;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public MovieDescription getDescription() {
        return description;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return java.util.Objects.equals(id, movie.id) &&
                java.util.Objects.equals(description, movie.description) &&
                Arrays.equals(content, movie.content);
    }

    @Override
    public int hashCode() {

        int result = java.util.Objects.hash(id, description);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", description=" + description +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
