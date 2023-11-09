package split.nplus1.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import split.nplus1.domain.Person;
import split.nplus1.domain.Team;
import split.nplus1.repository.PersonRepository;
import split.nplus1.repository.TeamRepository;

@Service
public class DataInitializeService {

    private final PersonRepository personRepository;
    private final TeamRepository teamRepository;

    public DataInitializeService(final PersonRepository personRepository, final TeamRepository teamRepository) {
        this.personRepository = personRepository;
        this.teamRepository = teamRepository;
    }

    @PostConstruct
    public void postConstruct() {
        final Team team = teamRepository.save(new Team("team1"));
        for (int i = 0; i < 100; i++) {
            personRepository.save(new Person(team));
        }
    }
}
