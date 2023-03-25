package study.multidatasourcejpa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import study.multidatasourcejpa.domain.User;
import study.multidatasourcejpa.repository.UserRepository;
import study.multidatasourcejpa.repository.master.UserMasterRepository;
import study.multidatasourcejpa.repository.slave.UserSlaveRepository;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSourceSelector {

    private final UserMasterRepository userMasterRepository;
    private final UserSlaveRepository userSlaveRepository;

    public UserRepository getUserRepository(User user) {
        return this.getUserRepository(user.getName());
    }

    public UserRepository getUserRepository(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("username is a required input!!");
        }

        // 이름이 a~i 로 시작하면 master DB에 저장하게 됨
        if (Pattern.matches("^[a-iA-I]*", name)) {
            log.info("Master Repository Selected");
            return userMasterRepository;
        } else {
            // 이름이 j~z로 시작하면 slave DB에 저장하게 됨
            log.info("Slave Repository Selected");
            return userSlaveRepository;
        }
    }
}
