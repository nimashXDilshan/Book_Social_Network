package com.nimash.book_network.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// this repository part is the calling part to the database using the auto generated queries (naming conventions)

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String role);//query is automatically generated based on the naming conventions(Spring data JPA)



}
