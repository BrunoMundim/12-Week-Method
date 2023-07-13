package com.mundim.WeekMethod.repository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

import com.mundim.WeekMethod.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final String ROLE_USER = "ROLE_USER";

    @Test
    public void save_shouldReturnSavedUser() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .password("password")
                .registrationDate(LocalDate.now())
                .role("ROLE_USER")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_shouldReturnMultipleUsers() {
        User user1 = User.builder()
                .name("name").email("email1@email.com").password("password")
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();
        User user2 = User.builder()
                .name("name").email("email2@email.com").password("password")
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void findById_shouldReturnNonNullUser() {
        User user = User.builder()
                .name("name").email("email1@email.com").password("password")
                .registrationDate(LocalDate.now()).role(ROLE_USER).build();

        userRepository.save(user);

        User findedUser = userRepository.findById(user.getId()).get();

        assertThat(findedUser).isNotNull();
        assertThat(findedUser.getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    public void findByEmail_shouldReturnNonNullUser() {
        User user = User.builder()
                .name("name").email("email@email.com").password("password")
                .registrationDate(LocalDate.now()).role(ROLE_USER).build();

        userRepository.save(user);

        User findedUser = userRepository.findByEmail(user.getEmail());

        assertThat(findedUser).isNotNull();
        assertThat(findedUser.getEmail()).isEqualTo("email@email.com");
    }

    @Test
    public void updateUser_shouldReturnNonNullUser() {
        User user = User.builder()
                .name("name").email("email@email.com").password("password")
                .registrationDate(LocalDate.now()).role(ROLE_USER).build();

        User savedUser = userRepository.save(user);
        user.setEmail("email2@email.com");
        User updatedUser = userRepository.save(user);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo("email2@email.com");
    }

    @Test
    public void deleteById_shouldReturnNullUser() {
        User user = User.builder()
                .name("name").email("email1@email.com").password("password")
                .registrationDate(LocalDate.now()).role(ROLE_USER).build();

        userRepository.save(user);

        userRepository.deleteById(user.getId());
        User findedUser = userRepository.findById(user.getId()).orElse(null);

        assertThat(findedUser).isNull();
    }

}
