package Photo_Sharing_Platform.service;

import Photo_Sharing_Platform.model.Role;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository =
            mock(UserRepository.class);

    private final PasswordEncoder passwordEncoder =
            mock(PasswordEncoder.class);

    private final UserService userService =
            new UserService(userRepository, passwordEncoder);

    @Test
    void registerUserShouldHashPasswordAndSetAdminRole() {

        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@test.com");
        user.setPassword("password123");

        when(passwordEncoder.encode("password123"))
                .thenReturn("hashedPassword");

        when(userRepository.save(user))
                .thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals("hashedPassword", result.getPassword());
        assertEquals(Role.ADMIN, result.getRole());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }

    @Test
    void createTeamMemberShouldHashPasswordAndSetTeamMemberRole() {

        User user = new User();
        user.setUsername("member");
        user.setEmail("member@test.com");
        user.setPassword("password123");

        when(passwordEncoder.encode("password123"))
                .thenReturn("hashedPassword");

        when(userRepository.save(user))
                .thenReturn(user);

        User result = userService.createTeamMember(user);

        assertEquals("hashedPassword", result.getPassword());
        assertEquals(Role.TEAM_MEMBER, result.getRole());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }

    @Test
    void emailExistsShouldReturnTrueWhenEmailExists() {

        User user = new User();

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(java.util.Optional.of(user));

        assertTrue(userService.emailExists("admin@test.com"));
    }

    @Test
    void emailExistsShouldReturnFalseWhenEmailDoesNotExist() {

        when(userRepository.findByEmail("unknown@test.com"))
                .thenReturn(java.util.Optional.empty());

        assertFalse(userService.emailExists("unknown@test.com"));
    }

    @Test
    void usernameExistsShouldReturnTrueWhenUsernameExists() {

        User user = new User();

        when(userRepository.findByUsername("admin"))
                .thenReturn(java.util.Optional.of(user));

        assertTrue(userService.usernameExists("admin"));
    }

    @Test
    void usernameExistsShouldReturnFalseWhenUsernameDoesNotExist() {

        when(userRepository.findByUsername("unknown"))
                .thenReturn(java.util.Optional.empty());

        assertFalse(userService.usernameExists("unknown"));
    }
}
