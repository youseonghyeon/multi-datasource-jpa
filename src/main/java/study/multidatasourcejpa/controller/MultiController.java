package study.multidatasourcejpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.multidatasourcejpa.domain.User;
import study.multidatasourcejpa.repository.UserRepository;
import study.multidatasourcejpa.service.DataSourceSelector;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MultiController {

    private final DataSourceSelector dataSourceSelector;

//    @GetMapping("/multi")
//    public List<User> findAllUser() {
//        UserRepository userRepository = dataSourceSelector.getUserRepository();
//        return userRepository.findAll();
//    }


}
