package study.multidatasourcejpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.multidatasourcejpa.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
