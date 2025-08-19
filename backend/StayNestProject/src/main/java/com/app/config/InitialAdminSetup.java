package com.app.config;

import com.app.dao.UserDao;
import com.app.entities.User;
import com.app.entities.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialAdminSetup implements CommandLineRunner {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public InitialAdminSetup(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userDao.countByRole(UserRole.ROLE_ADMIN) == 0) {
            User admin = new User(
                "Admin",
                "User",
                "admin@staynest.com",
                passwordEncoder.encode("AdminPassword123#"),
                "1234567890",
                UserRole.ROLE_ADMIN,
                true,
                false
            );
            userDao.save(admin);
            System.out.println("Initial admin created with email: admin@staynest.com");
        }
    }
}