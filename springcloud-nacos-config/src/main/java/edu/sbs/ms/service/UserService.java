package edu.sbs.ms.service;

import javax.sql.DataSource;

import edu.sbs.ms.entity.User;
import edu.sbs.ms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
//    private final DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    public UserService(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    public List<User> getStatsForUser(String userAlias) {
        return userRepository.findTop10ByAliasOrderByIdDesc(userAlias);
    }
}