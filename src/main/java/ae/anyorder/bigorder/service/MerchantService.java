package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.apiModel.StoreBrand;
import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.dto.PaginationDto;
import ae.anyorder.bigorder.model.CategoryEntity;
import ae.anyorder.bigorder.model.ComboItemEntity;
import ae.anyorder.bigorder.model.StoreBrandEntity;

import java.util.List;

/**
 * Created by Frank on 4/15/2018.
 */
public interface MerchantService {

    void saveStoreBrand(Long merchantId, StoreBrand store) throws Exception;

    void updateStoreBrand(Long merchantId, Long brandId, StoreBrand storeBrand) throws Exception;

    StoreBrandEntity getBrandDetails(Long brandId) throws Exception;

    void saveServingArea(Long storeId, List<Long> areaIds) throws Exception;

    void updateServingArea(Long storeId, List<Long> areaIds) throws Exception;

    PaginationDto storeListByStatus(long userId, Page page) throws Exception;

    List<CategoryEntity> getParentCategories() throws Exception;

    List<CategoryEntity> getActiveParentCategories() throws Exception;

    ComboItemEntity getComboItemDetail(long id) throws Exception;

    List<ComboItemEntity> getStoreItemList(long brandId) throws Exception;

    List<CategoryEntity> getStoreCategoryWiseItemList(long brandId) throws Exception;
}
