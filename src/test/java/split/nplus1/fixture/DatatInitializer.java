package split.nplus1.fixture;

import java.util.List;
import java.util.stream.IntStream;
import split.nplus1.domain.Person;
import split.nplus1.domain.Team;
import split.nplus1.repository.PersonRepository;
import split.nplus1.repository.TeamRepository;

public class DatatInitializer {

    public static Team saveTeam(final TeamRepository teamRepository, final String teamName) {
        return teamRepository.save(new Team(teamName));
    }

    public static PersonInitializer savePerson(final PersonRepository personRepository, final Team team) {
        return new PersonInitializer(personRepository, team);
    }

    public static class PersonInitializer {

        private final PersonRepository personRepository;
        private final Team team;

        public PersonInitializer(final PersonRepository personRepository, final Team team) {
            this.personRepository = personRepository;
            this.team = team;
        }

        public Person execute() {
            return personRepository.save(new Person(team));
        }

        public List<Person> repeat(final int count) {
            return IntStream.range(0, count)
                    .mapToObj(i -> personRepository.save(new Person(team)))
                    .toList();
        }
    }
}
