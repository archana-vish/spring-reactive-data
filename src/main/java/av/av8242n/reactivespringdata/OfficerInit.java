package av.av8242n.reactivespringdata;

import av.av8242n.reactivespringdata.dao.OfficerRepository;
import av.av8242n.reactivespringdata.entities.Officer;
import av.av8242n.reactivespringdata.entities.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class OfficerInit implements ApplicationRunner {

    private OfficerRepository officerRepository;

    @Autowired
    public OfficerInit(OfficerRepository repository) {
        this.officerRepository = repository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        officerRepository.deleteAll()
                .thenMany(Flux.just(new Officer(Rank.CAPTAIN, "James", "Kirk"),
                        new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
                        new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
                        new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
                        new Officer(Rank.CAPTAIN, "Jonathan", "Archer")))
                .flatMap(officerRepository::save)
                .thenMany(officerRepository.findAll())
                .subscribe(System.out::println);
    }
}
