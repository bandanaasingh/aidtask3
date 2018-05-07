package ae.anyorder.bigorder.util;

import ae.anyorder.bigorder.config.AuthenticatedUser;
import ae.anyorder.bigorder.enums.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Iterator;
/**
 * Created by Frank on 4/25/2018.
 */
@Component
public class SessionManager {
    public AuthenticatedUser getPrincipal() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getUserName() {
        return getPrincipal().getUsername();
    }

    public Role getRole() {
        Iterator iterator = getPrincipal().getAuthorities().iterator();
        String role = String.valueOf(iterator.next());
        return Role.valueOf(role);
    }

    public String getFirstName() {
        return getPrincipal().getFirstName();
    }

    public String getLastName() {
        return getPrincipal().getLastName();
    }

    public String getMobileNumber() {
        return getPrincipal().getMobileNumber();
    }

    public String getMerchantId() {
        return getPrincipal().getMerchantId();
    }

    public String getStoreManagerId() {
        return getPrincipal().getStoreManagerId();
    }

    public Long getUserId() {
        return getPrincipal().getId();
    }

    public boolean isAnonymousUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }
}
