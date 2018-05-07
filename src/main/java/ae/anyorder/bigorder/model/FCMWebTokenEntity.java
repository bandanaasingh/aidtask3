package ae.anyorder.bigorder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity(name = "fcm_token")
public class FCMWebTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "web_token")
    private String webToken;
    private String browser;
    private String version;
    @Column(name = "login_status")
    private Boolean loginStatus;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserEntity user;

    public String toString(){
        return null;
    }
}
