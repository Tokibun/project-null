package ca.utoronto.utsc.fanlinc.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ca.utoronto.utsc.fanlinc.model.Fandom;

public interface FandomRepository extends MongoRepository<Fandom, String> {

	@Query("{ 'displayName' : ?0 }")
	Optional<Fandom> findFandomByName(String displayName);
}
