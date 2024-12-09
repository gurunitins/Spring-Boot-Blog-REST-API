package com.sopromadze.blogapi.repository.v2;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.v2.MUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Repository
public interface RUserRepository extends JpaRepository<MUser, Long> {
	Optional<MUser> findByUserName(@NotBlank String userName);

	Optional<MUser> findById(@NotBlank String userName);

	List<MUser> findByHotel_Id(Long hotelId);

	Optional<MUser> findByEmail(@NotBlank String email);

	Boolean existsByUserName(@NotBlank String userName);

	Boolean existsByEmail(@NotBlank String email);

	Boolean existsByPhone(@NotBlank String phone);

	Optional<MUser> findByEmailOrPhone(String userName, String email);

	Optional<MUser> findByFirstNameOrLastName(String firstName, String LastName);

	default MUser getUser(UserPrincipal currentUser) {
		return getUserByName(currentUser.getUsername());
	}

	default MUser getUserByName(String userName) {
		return findByUserName(userName)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userName", userName));
	}


}
