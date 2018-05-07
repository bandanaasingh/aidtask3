package ae.anyorder.bigorder.dto;

import lombok.Data;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
public class AuthToken {
    private String token;

    public AuthToken(){

    }

    public AuthToken(String token){
        this.token = token;
    }
}
