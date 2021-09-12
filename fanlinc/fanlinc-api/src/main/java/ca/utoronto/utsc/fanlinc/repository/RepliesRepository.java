package ca.utoronto.utsc.fanlinc.repository;

import ca.utoronto.utsc.fanlinc.model.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepliesRepository extends MongoRepository<Reply, String> {
}
