package study.multidatasourcejpa.repository.slave;

import org.springframework.data.jpa.repository.JpaRepository;
import study.multidatasourcejpa.domain.User;
import study.multidatasourcejpa.repository.UserRepository;

public interface UserSlaveRepository extends JpaRepository<User, Long>, UserRepository {
}
