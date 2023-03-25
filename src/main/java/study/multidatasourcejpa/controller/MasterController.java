package study.multidatasourcejpa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.multidatasourcejpa.domain.User;
import study.multidatasourcejpa.repository.master.UserMasterRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MasterController {

    private final UserMasterRepository userRepository;

    @GetMapping("/master")
    public User masterInsert(@RequestParam("name") String name, @RequestParam("age") int age) {
        User newUser = new User(name, age);
        User savedUser = userRepository.save(newUser);
        log.info("newUser == savedUser : {}", newUser == savedUser);
        return savedUser;
    }


}
