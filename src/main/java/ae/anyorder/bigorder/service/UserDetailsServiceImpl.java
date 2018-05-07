package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.config.AuthenticatedUser;
import ae.anyorder.bigorder.enums.Role;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.exception.MyException;
import ae.anyorder.bigorder.model.UserEntity;
import ae.anyorder.bigorder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Frank on 4/2/2018.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = userRepository.findByUsername(userName);
            if (userEntity == null) {
                System.out.println("User not found! " + userName);
                throw new MyException("USR001");
            }
            List<GrantedAuthority> authorities = buildUserAuthority(userEntity.getRole());
            return buildUserForAuthentication(userEntity, authorities);
        }catch (Exception e){}
        return null;
    }

    private AuthenticatedUser buildUserForAuthentication(UserEntity userEntity, List<GrantedAuthority> authorities) {
        Boolean enabled = false;
        if(userEntity.getStatus().equals(Status.ACTIVE))
            enabled = true;
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userEntity.getUsername(),
                userEntity.getPassword(), enabled, true, true, true, authorities);

        authenticatedUser.setId(userEntity.getId());
        authenticatedUser.setFirstName(userEntity.getFirstName());
        authenticatedUser.setLastName(userEntity.getLastName());
        return authenticatedUser;
    }

    private List<GrantedAuthority> buildUserAuthority(Role role) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        try {
            setAuths.add(new SimpleGrantedAuthority(role.toString()));
            return new ArrayList<GrantedAuthority>(setAuths);
        }catch (Exception e) {
            throw e;
        }
    }
}
