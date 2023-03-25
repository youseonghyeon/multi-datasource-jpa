package study.multidatasourcejpa.repository.master;

import org.springframework.data.jpa.repository.JpaRepository;
import study.multidatasourcejpa.domain.User;

public interface UserMasterRepository extends JpaRepository<User, Long> {
}
