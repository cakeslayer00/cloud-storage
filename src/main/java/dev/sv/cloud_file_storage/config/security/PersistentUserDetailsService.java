package dev.sv.cloud_file_storage.config.security;

import dev.sv.cloud_file_storage.entity.User;
import dev.sv.cloud_file_storage.exception.UserNotFoundException;
import dev.sv.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class PersistentUserDetailsService implements UserDetailsService {

    private static final String INVALID_FORM_INPUT = "Invalid username or password";

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = repository.findByUsername(username);
        User user = opt.orElseThrow(() -> new UserNotFoundException(INVALID_FORM_INPUT));

        return new PersistentUserDetails(user);
    }
}
