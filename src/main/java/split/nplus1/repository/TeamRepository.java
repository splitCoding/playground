package split.nplus1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import split.nplus1.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

//    @EntityGraph(attributePaths = {}, type = EntityGraphType.FETCH)
//    Page<Team> findAll(final Pageable pageable);
}
