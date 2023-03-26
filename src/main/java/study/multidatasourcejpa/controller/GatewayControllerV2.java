package study.multidatasourcejpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.multidatasourcejpa.domain.User;
import study.multidatasourcejpa.repository.UserRepositoryHelper;
import study.multidatasourcejpa.service.DataSourceSelector;

@RestController
@RequiredArgsConstructor
public class GatewayControllerV2 {

    private final DataSourceSelector dataSourceSelector;

    // DB 분산저장 알고리즘
    // parameter name값이 a~i로 시작하면 master로 연결,
    // parameter name값이 j~z로 시작하면 slave로 연결

    // http://localhost:8080/gateway?name=aa&age=20     // master에 저장
    // http://localhost:8080/gateway?name=zz&age=20     // slave에 저장


    @GetMapping("/gateway-v2")
    public User gatewayController(@RequestParam("name") String name, @RequestParam("age") int age) {
        UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(name);
        return userRepository.save(new User(name, age));
    }

    @GetMapping("/gateway-v2/{name}")
    public User findUserByName(@PathVariable("name") String name) {
        UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(name);
        return userRepository.findByName(name);
    }
}
