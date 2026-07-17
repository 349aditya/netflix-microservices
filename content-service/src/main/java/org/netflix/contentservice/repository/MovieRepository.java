package org.netflix.contentservice.repository;

import org.netflix.contentservice.model.Genre;
import org.netflix.contentservice.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    List<Movie> findByGenre(Genre genre);
}
