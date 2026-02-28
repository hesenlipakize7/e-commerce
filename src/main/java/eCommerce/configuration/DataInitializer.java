package eCommerce.configuration;

import eCommerce.model.entity.User;
import eCommerce.model.enums.Role;
import eCommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "adminpatris@gmail.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setName("Patris");
            admin.setSurname("Patrisyo");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("patris777"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
