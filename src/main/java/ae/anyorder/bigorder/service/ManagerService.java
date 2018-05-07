package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.apiModel.Area;
import ae.anyorder.bigorder.apiModel.Category;
import ae.anyorder.bigorder.apiModel.UpdateStatus;
import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.dto.PaginationDto;
import ae.anyorder.bigorder.dto.SendNotification;

/**
 * Created by Frank on 5/01/2018.
 */
public interface ManagerService {

    void saveArea(Area areaEntity) throws Exception;

    void updateArea(Long id, Area areaEntity) throws Exception;

    void saveCategory(Category category) throws Exception;

    void updateCategory(Long id, Category category) throws Exception;

    void sendPushNotification(SendNotification notification) throws Exception;

    PaginationDto getMerchantList(Page page) throws Exception;

    void changeMerchantStatus(Long merchantId, UpdateStatus status) throws Exception;

}
