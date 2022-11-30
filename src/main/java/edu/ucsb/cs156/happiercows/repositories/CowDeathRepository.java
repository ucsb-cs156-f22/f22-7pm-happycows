package edu.ucsb.cs156.happiercows.repositories;

import java.util.Optional;

import edu.ucsb.cs156.happiercows.entities.CowDeath;
import org.springframework.data.repository.CrudRepository;

public interface CowDeathRepository extends CrudRepository <CowDeath, Long>{
	//@Query("SELECT sum(uc.cowsKilled) from cow_death uc where uc.commonsId=:commonsId and uc.userId=:userId")
	Iterable<CowDeath> getCowsKilled(Long commonsId, Long userId );

	//@Query("SELECT sum(uc.cowsKilled) from cow_death uc where uc.commonsId=:commonsId")
    Optional<Integer> getCowsKilled(Long commonsId);
}