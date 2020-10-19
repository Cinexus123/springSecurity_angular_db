package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Authority;
import entity.UserRoleName;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findByName(UserRoleName name);
}
