package com.example.cashadvance;

import com.example.cashadvance.model.Role;
import com.example.cashadvance.model.User;
import com.example.cashadvance.repository.RoleRepository;
import com.example.cashadvance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@org.springframework.context.annotation.Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedData(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            Role admin = roleRepo.findByName("ADMIN").orElseGet(() -> roleRepo.save(Role.builder().name("ADMIN").build()));
            Role manager = roleRepo.findByName("MANAGER").orElseGet(() -> roleRepo.save(Role.builder().name("MANAGER").build()));
            Role supervisor = roleRepo.findByName("SUPERVISOR").orElseGet(() -> roleRepo.save(Role.builder().name("SUPERVISOR").build()));
            Role employeeRole = roleRepo.findByName("EMPLOYEE").orElseGet(() -> roleRepo.save(Role.builder().name("EMPLOYEE").build()));

            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(User.builder()
                    .username("admin")
                    .password(encoder.encode("admin123"))
                    .salary(100000.0)
                    .allowance(5000.0)
                    .roles(Set.of(admin))
                    .build());
            }
            if (userRepo.findByUsername("manager").isEmpty()) {
                userRepo.save(User.builder()
                    .username("manager")
                    .password(encoder.encode("manager123"))
                    .salary(80000.0)
                    .allowance(4000.0)
                    .roles(Set.of(manager))
                    .build());
            }
            if (userRepo.findByUsername("supervisor").isEmpty()) {
                userRepo.save(User.builder()
                    .username("supervisor")
                    .password(encoder.encode("supervisor123"))
                    .salary(60000.0)
                    .allowance(3000.0)
                    .roles(Set.of(supervisor))
                    .build());
            }
            if (userRepo.findByUsername("employee1").isEmpty()) {
                userRepo.save(User.builder()
                    .username("employee1")
                    .password(encoder.encode("employee123"))
                    .salary(40000.0)
                    .allowance(2000.0)
                    .roles(Set.of(employeeRole))
                    .build());
            }
        };
    }
}
