package com.example.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByMobile(String mobile);
}