package study.multidatasourcejpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import study.multidatasourcejpa.domain.User;
import study.multidatasourcejpa.repository.master.UserMasterRepository;
import study.multidatasourcejpa.repository.slave.UserSlaveRepository;
import study.multidatasourcejpa.service.DataSourceSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class UserRepository implements UserRepositoryHelper {

    private final UserMasterRepository masterRepository;
    private final UserSlaveRepository slaveRepository;
    private final DataSourceSelector dataSourceSelector;

    public void flushMaster() {
        masterRepository.flush();
    }

    public void flushSlave() {
        slaveRepository.flush();
    }

    @Override
    public void flush() {
        masterRepository.flush();
        slaveRepository.flush();
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        String name = entity.getName();
        UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(name);
        return userRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        List<S> users = new ArrayList<>();
        for (S user : entities) {
            UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(user);
            S savedUser = userRepository.save(user);
            users.add(savedUser);
        }
        this.flush();
        return users;
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        for (User user : entities) {
            UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(user);
            userRepository.delete(user);
        }
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        masterRepository.deleteAllByIdInBatch(longs);
        slaveRepository.deleteAllByIdInBatch(longs);
    }

    @Override
    public void deleteAllInBatch() {
        masterRepository.deleteAllInBatch();
        slaveRepository.deleteAllInBatch();
    }

    @Override
    public User getOne(Long aLong) {
        User findUser = masterRepository.getReferenceById(aLong);
        if (findUser == null) {
            return slaveRepository.getReferenceById(aLong);
        }
        return findUser;
    }

    @Override
    public User getById(Long aLong) {
        User findUser = masterRepository.getReferenceById(aLong);
        if (findUser == null) {
            return slaveRepository.getReferenceById(aLong);
        }
        return findUser;
    }

    @Override
    public User getReferenceById(Long aLong) {
        User findUser = masterRepository.getReferenceById(aLong);
        if (findUser == null) {
            return slaveRepository.getReferenceById(aLong);
        }
        return findUser;
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        Optional<S> findUser = masterRepository.findOne(example);
        if (findUser.isEmpty()) {
            return slaveRepository.findOne(example);
        }
        return findUser;
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        List<S> users = new ArrayList<>();
        List<S> master = masterRepository.findAll(example);
        List<S> slave = slaveRepository.findAll(example);
        users.addAll(master);
        users.addAll(slave);
        return users;
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        List<S> users = new ArrayList<>();
        List<S> master = masterRepository.findAll(example, sort);
        List<S> slave = slaveRepository.findAll(example, sort);
        users.addAll(master);
        users.addAll(slave);
        // sort 기능 추가 해야함
        return users;
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        // 페이징 기능 추가 해야함
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return masterRepository.count(example)
                + slaveRepository.count(example);
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return masterRepository.exists(example) || slaveRepository.exists(example);
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        R master = masterRepository.findBy(example, queryFunction);
        R slave = slaveRepository.findBy(example, queryFunction);
        // 이거 만들어야 함...
        return null;
    }

    @Override
    public <S extends User> S save(S entity) {
        UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(entity);
        return userRepository.save(entity);
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> users = new ArrayList<>();
        for (S entity : entities) {
            UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(entity);
            S savedUser = userRepository.save(entity);
            users.add(savedUser);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        Optional<User> master = masterRepository.findById(aLong);
        if (master.isEmpty()) {
            return slaveRepository.findById(aLong);
        }
        return master;
    }

    @Override
    public boolean existsById(Long aLong) {
        return masterRepository.existsById(aLong) || slaveRepository.existsById(aLong);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        List<User> master = masterRepository.findAll();
        List<User> slave = slaveRepository.findAll();
        users.addAll(master);
        users.addAll(slave);
        return users;
    }

    @Override
    public List<User> findAllById(Iterable<Long> longs) {
        List<User> users = new ArrayList<>();
        List<User> master = masterRepository.findAllById(longs);
        List<User> slave = slaveRepository.findAllById(longs);
        users.addAll(master);
        users.addAll(slave);
        return users;
    }

    @Override
    public long count() {
        return masterRepository.count()
                + slaveRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        masterRepository.deleteById(aLong);
        slaveRepository.deleteById(aLong);
    }

    @Override
    public void delete(User entity) {
        UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(entity);
        userRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        masterRepository.deleteAllById(longs);
        slaveRepository.deleteAllById(longs);
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        for (User entity : entities) {
            UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(entity);
            userRepository.delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        masterRepository.deleteAll();
        slaveRepository.deleteAll();
    }

    @Override
    public List<User> findAll(Sort sort) {
        List<User> users = new ArrayList<>();
        List<User> master = masterRepository.findAll(sort);
        List<User> slave = slaveRepository.findAll(sort);
        users.addAll(master);
        users.addAll(slave);
        return users;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<User> master = masterRepository.findAll(pageable);
        Page<User> slave = slaveRepository.findAll(pageable);
        return null;
    }

    @Override
    public User findByName(String name) {
        UserRepositoryHelper userRepository = dataSourceSelector.getUserRepository(name);
        return userRepository.findByName(name);
    }
}
