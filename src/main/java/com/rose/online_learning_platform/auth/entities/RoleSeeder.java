package com.rose.online_learning_platform.auth.entities;

import com.rose.online_learning_platform.auth.repository.RoleRepository;
import com.rose.online_learning_platform.commons.enums.RolesEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            // Create roles if they don't already exist
            Role userRole = new Role();
            userRole.setName(RolesEnum.USER);
            userRole.setDescription("Standard user role");
            roleRepository.save(userRole);

            Role insROle = new Role();
            insROle.setName(RolesEnum.INSTRUCTOR);
            insROle.setDescription("Standard role role");
            roleRepository.save(insROle);

            Role approverRole = new Role();
            approverRole.setName(RolesEnum.APPROVER);
            approverRole.setDescription("approverRole role role");
            roleRepository.save(approverRole);

            Role adminRole = new Role();
            adminRole.setName(RolesEnum.SUPER_ADMIN);
            adminRole.setDescription("Administrator role");
            roleRepository.save(adminRole);

            Role financeRole = new Role();
            financeRole.setName(RolesEnum.STUDENT);
            financeRole.setDescription("STudnt manager role");
            roleRepository.save(financeRole);

            System.out.println("Roles inserted successfully.");
        } else {
            System.out.println("Roles already exist. Skipping insertion.");
        }
    }
}
