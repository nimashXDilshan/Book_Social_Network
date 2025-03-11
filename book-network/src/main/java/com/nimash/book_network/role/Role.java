package com.nimash.book_network.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimash.book_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter // lombok automatically create getters & setters for all entity attributes
@Builder//. It provides a way to construct complex objects step by step. This is particularly useful for creating immutable objects or objects that require many parameters during instantiation.
@AllArgsConstructor
@NoArgsConstructor
@Entity//The @Entity annotation in Java is used to specify that the class is an entity and is mapped to a database table
@Table(name="roles")
@EntityListeners(AuditingEntityListener.class)//Enables automatic auditing (e.g., auto-filling createdDate).
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;


    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;
    //This field in the Role class holds the list of User entities associated with a particular Role.

    @CreatedDate//Automatically stores the creation timestamp (cannot be updated).
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
//The @LastModifiedDate annotation is used to automatically set the modification timestamp when the entity is updated
    @Column(insertable = false)//The @Column(insertable = false) annotation ensures that this column is not included in SQL INSERT statements
    private LocalDateTime lastModifiedDate;
}
