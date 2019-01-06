package av.av8242n.reactivespringdata.dao;

import av.av8242n.reactivespringdata.entities.Officer;
import av.av8242n.reactivespringdata.entities.Rank;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OfficerRepositoryTest {

    @Autowired OfficerRepository repository;

    // SQL H2 YOU USED TRANSACTIONAL. WITH MONGO IT IS A LITTLE TRICKY. NEED A COLLECTION AND THEN EMPTY IT
    private List<Officer> officers = Arrays.asList(
            new Officer(Rank.CAPTAIN, "James", "Kirk"),
            new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
            new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
            new Officer(Rank.CAPTAIN, "Jonathan", "Archer"));

    @Before
    public void setUp() throws Exception {
        repository.deleteAll() //delete all the values
                .thenMany(Flux.fromIterable(officers)) // wait for the previous step to complete and then create a flux from officer
                .flatMap(repository::save) // save the values in the repo. FlatMap because save returns a flux of officers but save returns a mono and block can happen only on mono
                .then() // wait for the above
                .block(); // block before running other actual tests
    }

    @Test
    public void save() {
        Officer lorca = new Officer(Rank.CAPTAIN, "Gabriel", "Lorca");
        StepVerifier.create(repository.save(lorca))
                .expectNextMatches(officer -> !officer.getId().equals(""))
                .verifyComplete();
    }
}