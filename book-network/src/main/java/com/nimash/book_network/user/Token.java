package com.nimash.book_network.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter // lombok automatically create getters & setters for all entity attributes
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity//The @Entity annotation in Java is used to specify that the class is an entity and is mapped to a database table
public class Token{

    @Id
    @GeneratedValue
    private Integer id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime validatedAt;

    @ManyToOne  // one token only belongs to the specific user
    @JoinColumn(name="userId",nullable = false) // in this token table we want to add userId column because each token has specific user & this column cannot be null
    private User user;


}
