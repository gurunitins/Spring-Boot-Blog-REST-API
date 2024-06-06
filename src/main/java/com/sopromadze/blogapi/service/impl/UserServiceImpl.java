package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.*;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Address;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.Geo;
import com.sopromadze.blogapi.model.user.UserEntity;
import com.sopromadze.blogapi.payload.*;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.RoleRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
                currentUser.getLastName());
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserProfile getUserProfile(String username) {
        UserEntity userEntity = userRepository.getUserByName(username);

        Long postCount = postRepository.countByCreatedBy(userEntity.getId());

        return new UserProfile(userEntity.getId(), userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getCreatedAt(), userEntity.getEmail(), userEntity.getAddress(), userEntity.getPhone(), userEntity.getWebsite(),
                userEntity.getCompany(), postCount);
    }

    @Override
    public UserEntity addUser(UserEntity userEntity) {
        if (userRepository.existsByUsername(userEntity.getUsername())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
            throw new BadRequestException(apiResponse);
        }

        if (userRepository.existsByEmail(userEntity.getEmail())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
            throw new BadRequestException(apiResponse);
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        userEntity.setRoles(roles);

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity updateUser(UserEntity newUserEntity, String username, UserPrincipal currentUser) {
        UserEntity userEntity = userRepository.getUserByName(username);
        if (userEntity.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            userEntity.setFirstName(newUserEntity.getFirstName());
            userEntity.setLastName(newUserEntity.getLastName());
            userEntity.setPassword(passwordEncoder.encode(newUserEntity.getPassword()));
            userEntity.setAddress(newUserEntity.getAddress());
            userEntity.setPhone(newUserEntity.getPhone());
            userEntity.setWebsite(newUserEntity.getWebsite());
            userEntity.setCompany(newUserEntity.getCompany());

            return userRepository.save(userEntity);

        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username);
        throw new UnauthorizedException(apiResponse);

    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
        if (!userEntity.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete profile of: " + username);
            throw new AccessDeniedException(apiResponse);
        }

        userRepository.deleteById(userEntity.getId());

        return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        UserEntity userEntity = userRepository.getUserByName(username);
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException("User role not set")));
        roles.add(
                roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
        return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + username);
    }

    @Override
    public ApiResponse removeAdmin(String username) {
        UserEntity userEntity = userRepository.getUserByName(username);
        List<Role> roles = new ArrayList<>();
        roles.add(
                roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
        return new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + username);
    }

    @Override
    public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
        UserEntity userEntity = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));
        Geo geo = Geo.builder()
                .lat(infoRequest.getLat())
                .lng(infoRequest.getLng())
                .build();
        Address address = Address.builder()
                .street(infoRequest.getStreet())
                .suite(infoRequest.getSuite())
                .city(infoRequest.getCity())
                .zipcode(infoRequest.getZipcode())
                .geo(geo)
                .build();
        Company company = Company.builder()
                .name(infoRequest.getCompanyName())
                .catchPhrase(infoRequest.getCatchPhrase())
                .bs(infoRequest.getBs())
                .build();
        if (userEntity.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            userEntity.setAddress(address);
            userEntity.setCompany(company);
            userEntity.setWebsite(infoRequest.getWebsite());
            userEntity.setPhone(infoRequest.getPhone());
            UserEntity updatedUserEntity = userRepository.save(userEntity);

            Long postCount = postRepository.countByCreatedBy(updatedUserEntity.getId());

            return new UserProfile(updatedUserEntity.getId(), updatedUserEntity.getUsername(),
                    updatedUserEntity.getFirstName(), updatedUserEntity.getLastName(), updatedUserEntity.getCreatedAt(),
                    updatedUserEntity.getEmail(), updatedUserEntity.getAddress(), updatedUserEntity.getPhone(), updatedUserEntity.getWebsite(),
                    updatedUserEntity.getCompany(), postCount);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile", HttpStatus.FORBIDDEN);
        throw new AccessDeniedException(apiResponse);
    }

}
