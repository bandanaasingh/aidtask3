package ae.anyorder.bigorder.apiModel;

import lombok.Data;

/**
 * Created by Frank on 4/8/2018.
 */
@Data
public class UserRegister {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String mobileNumber;
    private String businessTitle;
    private String url;
}

