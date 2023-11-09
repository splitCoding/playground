package split.nplus1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import split.nplus1.domain.Person;
import split.nplus1.repository.PersonRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void validateAllTeamsIn(final String teamName) {
        final List<Person> all = personRepository.findAllWithTeam();
        for (Person person : all) {
            if (!person.getTeam().getName().equals(teamName)) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Cacheable(value = "getAllPerson")
    public List<Person> getAllPerson() {
        return personRepository.findAllWithTeam();
    }
}
