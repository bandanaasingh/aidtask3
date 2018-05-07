package ae.anyorder.bigorder.abs;

import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.model.AreaEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 4/9/2018.
 */
public class AbstractManager {
    Logger log = Logger.getLogger(AbstractManager.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    public String getServerName() {
        return httpServletRequest.getServerName();
    }


    public String getServerUrl() {
        return httpServletRequest.getRequestURL().toString().replace(httpServletRequest.getServletPath(), "").trim();
    }

    public String getIpAddress() {
        String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null)
            ipAddress = httpServletRequest.getRemoteAddr();
        return ipAddress;
    }

    public static void parseActiveAreaTree(List<AreaEntity> parenAreas) {
        if (parenAreas != null) {
            for (AreaEntity area : parenAreas) {
                List<AreaEntity> activeChild = new ArrayList<>();
                if(area.getChild()!=null) {
                    for (AreaEntity child : area.getChild()) {
                        if (child.getStatus().equals(Status.ACTIVE)) {
                            activeChild.add(child);
                        }
                    }
                }
                if (activeChild.size() > 0)
                    area.setChild(activeChild);
                else
                    area.setChild(null);
                parseActiveAreaTree(area.getChild());
            }
        }
    }
}
