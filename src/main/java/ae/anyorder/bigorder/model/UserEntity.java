package ae.anyorder.bigorder.model;

import ae.anyorder.bigorder.enums.Role;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Frank on 4/2/2018.
 */
@Entity(name = "UserEntity")
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "verified_status", columnDefinition = "TINYINT(1)")
    private Boolean verifiedStatus;
    @Column(name = "verification_code")
    private String verificationCode;
    private String firstName;
    private String lastName;
    private String email;
    @Column(name = "mobile_no")
    private String mobileNumber;
    @JsonSerialize(using = JsonDateSerializer.class)
    @Column(name = "created_date")
    private Timestamp createdDate;
    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    private MerchantEntity merchant;
    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    private ClientEntity client;

    public String toString(){
        return null;
    }

}
