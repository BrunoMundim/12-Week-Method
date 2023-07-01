package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    public User(UserDTO userDTO){
        this.name = userDTO.name();
        this.email = userDTO.email();
        this.password = userDTO.password();
        this.registrationDate = LocalDate.now();
    }

}
