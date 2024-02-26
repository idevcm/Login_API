package org.caicoders.login_api.repositories;

import org.caicoders.login_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    User save(User user);
    void delete(User user);
}
