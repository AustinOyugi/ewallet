package com.doorstep.springproject.models.userdata;

import com.doorstep.springproject.models.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "userName"
        }),
        @UniqueConstraint(columnNames = {
                "emailAddress"
        })
})
public class User extends DateAudit {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String userId;

    @Size(min = 3, max = 20)
    private String fullName;

    @NotNull
    @NotEmpty
    private String userName;

    @Email
    @NotNull
    private String emailAddress;

    private String pictureUrl;

    @Column
    @Size(max = 15)
    private String mobileNumber;

    @Size(max = 20)
    @Column
    private String country;

    @Size(max = 100)
    @JsonIgnore
    private String password;

    @Column
    private short accountActivated;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    public User(){

    }

    public User( @Size(max = 20) String fullName, @Size(max = 15) String userName,
                 @Email @Size(max = 40) String emailAddress,
                  @Size(max = 20) String country, @Size(max = 15) String mobileNumber,
                  @Size(max = 100) String password) {
        this.fullName = fullName;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.country = country;
        this.mobileNumber = mobileNumber;
        this.password = password;
    }
}