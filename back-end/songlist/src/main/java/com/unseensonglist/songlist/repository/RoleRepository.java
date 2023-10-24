package com.unseensonglist.songlist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unseensonglist.songlist.model.ERole;
import com.unseensonglist.songlist.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(ERole roleUser);

}
