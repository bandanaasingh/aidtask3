package ae.anyorder.bigorder.config;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Data
public class AuthenticatedUser extends org.springframework.security.core.userdetails.User{
    private Long id;
    private String merchantId;
    private String storeManagerId;
    private String profileImage;
    private String businessTitle;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String website;
    private String emailAddress;

    public AuthenticatedUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthenticatedUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

    }
}

