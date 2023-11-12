package split.nplus1.service;

import java.util.List;
import split.nplus1.domain.Person;

public interface PersonService {

    void validateAllTeamsIn(final String teamName);

    List<Person> getAllPerson();
}
