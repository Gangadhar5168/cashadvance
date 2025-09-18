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
            Role admin = roleRepo.save(new Role(null, "ADMIN"));
            Role manager = roleRepo.save(new Role(null, "MANAGER"));
            Role supervisor = roleRepo.save(new Role(null, "SUPERVISOR"));

            userRepo.save(new User(null, "admin", encoder.encode("admin123"), 100000.0, 5000.0, Set.of(admin)));
            userRepo.save(new User(null, "manager", encoder.encode("manager123"), 80000.0, 4000.0, Set.of(manager)));
            userRepo.save(new User(null, "supervisor", encoder.encode("supervisor123"), 60000.0, 3000.0, Set.of(supervisor)));
        };
    }
}
