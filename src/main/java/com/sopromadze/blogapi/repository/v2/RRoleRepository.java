package com.sopromadze.blogapi.repository.v2;

//import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.v2.role.MRole;
import com.sopromadze.blogapi.model.v2.role.MRoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RRoleRepository extends JpaRepository<MRole, Long> {
	Optional<MRole> findByName(MRoleName name);
}
