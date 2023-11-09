package split.nplus1.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import split.nplus1.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT P FROM Person P")
    @EntityGraph(attributePaths = {"team"})
    List<Person> findAllWithTeam();
}
