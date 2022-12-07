package edu.ucsb.cs156.happiercows.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.ucsb.cs156.happiercows.entities.CowDeath;

public interface CowDeathRepository extends CrudRepository <CowDeath, Long>{

	Optional<CowDeath> getCowsKilledByCommonsIdAndUserId(Long commons_id, Long user_id );
	
    Iterable<CowDeath> getCowsKilledByCommonsId(Long commons_id);
}

