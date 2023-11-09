package split.nplus1.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import split.nplus1.domain.Person;
import split.nplus1.service.PersonService;

@RestController
@RequestMapping("person")
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAll() {
        logger.info("API CALLED : GET /person");
        return ResponseEntity.ok(personService.getAllPerson());
    }
}
