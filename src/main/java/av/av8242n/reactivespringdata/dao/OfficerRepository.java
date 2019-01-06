package av.av8242n.reactivespringdata.dao;

import av.av8242n.reactivespringdata.entities.Officer;
import av.av8242n.reactivespringdata.entities.Rank;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OfficerRepository extends ReactiveMongoRepository<Officer, Rank> {
}
