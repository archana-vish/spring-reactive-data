package av.av8242n.reactivespringdata.controller;

import av.av8242n.reactivespringdata.dao.OfficerRepository;
import av.av8242n.reactivespringdata.entities.Officer;
import av.av8242n.reactivespringdata.entities.Rank;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class OfficerControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private OfficerRepository repository;

    private List<Officer> officers = Arrays.asList(
            new Officer(Rank.CAPTAIN, "James", "Kirk"),
            new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
            new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
            new Officer(Rank.CAPTAIN, "Jonathan", "Archer"));

    @Before
    public void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(repository::save)
                .doOnNext(System.out::println)
                .then()
                .block();
    }

    @Test
    public void testGetAllOfficers() {
        client.get().uri("/officers")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Officer.class)
                .hasSize(5)
                .consumeWith(System.out::println);
    }

    @Test
    public void testGetOfficer() {
        client.get().uri("/officers/{id}", officers.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Officer.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testCreateOfficer() {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");

        client.post().uri("/officers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(officer), Officer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.first").isEqualTo("Nyota")
                .jsonPath("$.last").isEqualTo("Uhuru")
                .consumeWith(System.out::println);
    }

}