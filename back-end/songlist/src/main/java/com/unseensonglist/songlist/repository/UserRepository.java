package com.unseensonglist.songlist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unseensonglist.songlist.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

}
