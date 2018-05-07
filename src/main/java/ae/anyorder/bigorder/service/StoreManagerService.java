package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.apiModel.FCMWebToken;
import ae.anyorder.bigorder.apiModel.ComboItem;
import ae.anyorder.bigorder.model.AreaEntity;

import java.util.List;

/**
 * Created by Frank on 4/10/2018.
 */
public interface StoreManagerService {

    void saveFCMWebToken(Long userId, FCMWebToken fcmWebToken) throws Exception;

    void saveItem(long brandId, ComboItem comboItem) throws Exception;

    void updateItem(String itemId, ComboItem comboItem) throws Exception;

    List<AreaEntity> getAreas() throws Exception;

    AreaEntity getArea(long id) throws Exception;

    List<AreaEntity> getActiveAreas() throws Exception;
}
