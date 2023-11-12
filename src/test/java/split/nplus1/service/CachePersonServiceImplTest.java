package split.nplus1.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import split.nplus1.cache.CachePersonServiceImpl;
import split.nplus1.domain.Person;
import split.nplus1.domain.Team;
import split.nplus1.fixture.DatatInitializer;
import split.nplus1.repository.PersonRepository;
import split.nplus1.repository.TeamRepository;

@SpringBootTest
class CachePersonServiceImplTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PersonService personService;

    @DisplayName("메서드가 두번째 호출될 때 부터 SpringBoot Cache 에 해당 메서드 키에 대응하는 캐쉬가 존재한다.")
    @Test
    void getAllPerson() {
        //given
        final int personCount = 10;
        final Team team = DatatInitializer.saveTeam(teamRepository, "team1");
        DatatInitializer.savePerson(personRepository, team).repeat(personCount);
        personService.getAllPerson();

        //when
        //then
        assertSoftly(softly -> {
            softly.assertThat(cacheManager.getCacheNames()).contains(CachePersonServiceImpl.CACHE_NAME);

            final Cache cache = cacheManager.getCache(CachePersonServiceImpl.CACHE_NAME);
            final List<Person> list = cache.get("getAllPerson-0", List.class);
            softly.assertThat(list).hasSize(personCount);
        });
    }
}
