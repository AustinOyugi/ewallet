package com.doorstep.springproject.models.payments;

import com.doorstep.springproject.models.audit.DateAudit;
import com.doorstep.springproject.models.userdata.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Wallet extends DateAudit {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String walletId;

    private String accountName;

    @Column(unique = true)
    private String accountNumber;

    private String balance;

    @ManyToOne
    private User walletuser;

    public Wallet(String accountName, String accountNumber,  User walletuser) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = "0.00";
        this.walletuser = walletuser;
    }
}
