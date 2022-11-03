package duc.min.repository;

import duc.min.model.Role;
import duc.min.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {

  Optional<Role>findByName(RoleName roleName);
}
