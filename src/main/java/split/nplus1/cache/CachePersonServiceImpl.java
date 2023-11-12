package split.nplus1.cache;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import split.nplus1.domain.Person;
import split.nplus1.repository.PersonRepository;
import split.nplus1.service.PersonService;

@Service
@Transactional(readOnly = true)
public class CachePersonServiceImpl implements PersonService {

    public static final String CACHE_NAME = "people";
    private final PersonRepository personRepository;

    public CachePersonServiceImpl(final PersonRepository personRepository) {
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

    @Cacheable(value = CACHE_NAME, keyGenerator = "cacheKeyGenerator")
    public List<Person> getAllPerson() {
        return personRepository.findAllWithTeam();
    }
}
