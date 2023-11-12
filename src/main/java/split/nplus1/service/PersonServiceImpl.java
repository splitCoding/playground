package split.nplus1.service;

import java.util.List;
import split.nplus1.domain.Person;
import split.nplus1.repository.PersonRepository;

public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void validateAllTeamsIn(final String teamName) {
        final List<Person> all = personRepository.findAllWithTeam();
        for (Person person : all) {
            if (!person.getTeam().getName().equals(teamName)) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<Person> getAllPerson() {
        return personRepository.findAllWithTeam();
    }
}
