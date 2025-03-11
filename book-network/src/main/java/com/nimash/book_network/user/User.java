package com.nimash.book_network.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimash.book_network.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter // lombok automatically create getters & setters for all entity attributes
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity//The @Entity annotation in Java is used to specify that the class is an entity and is mapped to a database table
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)//Enables automatic auditing (e.g., auto-filling createdDate).
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;// auto generated value//id is the primary key and will be automatically generated.

    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;

    @Column(unique = true)
    private  String email;// unique column email

    private  String password;
    private boolean accountLocked;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)//more explanation is in the word doc
    private List<Role> roles;


    @CreatedDate//Automatically stores the creation timestamp (cannot be updated).
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate//The @LastModifiedDate annotation is used to automatically set the modification timestamp when the entity is updated
    @Column(insertable = false)//The @Column(insertable = false) annotation ensures that this column is not included in SQL INSERT statements
    private LocalDateTime lastModifiedDate;



    // these are UserDetails &  Principal Interfaces Overrides
    @Override
    public String getName() {
        return email;// unique name for user entity
    }


    //UserDetails Interface method override(Inside the spring security framework)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r->new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }
    //this.roles ->This refers to the roles list in the User entity, which holds all roles assigned to the user.
    //Converts the list of Role objects into a stream to process each role efficiently.
    //.map(r -> new SimpleGrantedAuthority(r.getName()))
    //
    //For each role (r), it creates a SimpleGrantedAuthority object with the role's name.
    //SimpleGrantedAuthority is a Spring Security class that represents a granted authority (typically a role like "ROLE_ADMIN" or "ROLE_USER").
    //r.getName() fetches the role name (e.g., "ADMIN", "USER").
    //.collect() -> Converts the stream of SimpleGrantedAuthority objects into a list.



    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // this is custom method for get the full name
    public  String fullName(){
        return firstName+" "+lastName;
    }
}
