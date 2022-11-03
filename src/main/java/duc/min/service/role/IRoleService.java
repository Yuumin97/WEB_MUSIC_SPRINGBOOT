package duc.min.service.role;

import duc.min.model.Role;
import duc.min.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName roleName);
}
