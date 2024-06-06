package com.sopromadze.blogapi.config;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadRoles();
    }

    private void loadRoles() {
        RoleName[] roleNames = new RoleName[] { RoleName.ROLE_ADMIN, RoleName.ROLE_USER };

        Arrays.stream(roleNames).forEach(roleName -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(role -> log.info("Role {} already exists", role.getName()),
                    () -> {
                        Role roleToCreate = new Role(roleName);
                        roleRepository.save(roleToCreate);
                        log.info("Role {} created", roleToCreate.getName());
                    });
        });
    }

}
