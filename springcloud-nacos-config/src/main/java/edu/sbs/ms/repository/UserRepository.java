package edu.sbs.ms.repository;

import edu.sbs.ms.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByAlias(final String alias);

    List<User> findTop10ByAliasOrderByIdDesc(String userAlias);

}
