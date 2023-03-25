package study.multidatasourcejpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.multidatasourcejpa.domain.User;
import study.multidatasourcejpa.repository.UserRepository;
import study.multidatasourcejpa.service.DataSourceSelector;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class GatewayController {

    private final DataSourceSelector dataSourceSelector;

    // DB 분산저장 알고리즘
    // parameter name값이 a~i로 시작하면 master로 연결,
    // parameter name값이 j~z로 시작하면 slave로 연결

    // http://localhost:8080/gateway?name=aa&age=20     // master에 저장
    // http://localhost:8080/gateway?name=zz&age=20     // slave에 저장
    @GetMapping("/gateway")
    public User gatewayController(@RequestParam("name") String name, @RequestParam("age") int age) {
        UserRepository userRepository = dataSourceSelector.getUserRepository(name);
        return userRepository.save(new User(name, age));
    }

    @GetMapping("/gateway/{name}")
    public User findUserByName(@PathVariable("name") String name) {
        UserRepository userRepository = dataSourceSelector.getUserRepository(name);
        User findUser = userRepository.findByName(name);
        return Objects.requireNonNullElseGet(findUser, () -> new User("not exists", 0));
    }
}
