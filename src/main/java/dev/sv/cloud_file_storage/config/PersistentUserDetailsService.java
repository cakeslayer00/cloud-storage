package dev.sv.cloud_file_storage.config;

import dev.sv.cloud_file_storage.entity.User;
import dev.sv.cloud_file_storage.exception.UserNotFoundException;
import dev.sv.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PersistentUserDetailsService implements UserDetailsService {

    private static final String INVALID_FORM_INPUT = "Invalid username or password";

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = repository.findByUsername(username);
        User user = opt.orElseThrow(() -> new UserNotFoundException(INVALID_FORM_INPUT));

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }
        };
    }
}
