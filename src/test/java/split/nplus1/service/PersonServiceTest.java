package split.nplus1.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import split.nplus1.domain.Person;
import split.nplus1.domain.Team;
import split.nplus1.repository.PersonRepository;
import split.nplus1.repository.TeamRepository;

@DataJpaTest
class PersonServiceTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager entityManager;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        System.out.println("setUp transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());
        personService = new PersonService(personRepository);

        for (int i = 0; i < 100; i++) {
            final Team team = teamRepository.saveAndFlush(new Team("team" + i));
            personRepository.saveAndFlush(new Person(team));
        }

//        teamRepository.deleteById(1L);
        teamRepository.flush();
        entityManager.clear();
    }

    @Test
    void validateAllTeamsIn() {
        System.out.println("test transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());
        Assertions.assertDoesNotThrow(() -> personService.validateAllTeamsIn("team1"));
    }
}
