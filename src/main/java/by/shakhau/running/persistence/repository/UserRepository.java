package by.shakhau.running.persistence.repository;

import by.shakhau.running.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByNameAndPassword(String name, String password);
    UserEntity findByName(String name);
}
