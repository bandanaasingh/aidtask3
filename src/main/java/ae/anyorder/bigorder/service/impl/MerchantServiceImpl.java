package ae.anyorder.bigorder.service.impl;

import ae.anyorder.bigorder.apiModel.ComboItem;
import ae.anyorder.bigorder.apiModel.StoreBrand;
import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.dto.PaginationDto;
import ae.anyorder.bigorder.enums.Role;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.exception.MyException;
import ae.anyorder.bigorder.model.*;
import ae.anyorder.bigorder.repository.*;
import ae.anyorder.bigorder.service.MerchantService;
import ae.anyorder.bigorder.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.*;

/**
 * Created by Frank on 4/15/2018.
 */
@Service
public class MerchantServiceImpl implements MerchantService{
    private static final Logger log = Logger.getLogger(MerchantServiceImpl.class);

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    StoreBrandRepository storeBrandRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    StoreAreaRepository storeAreaRepository;

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    AmazonUtil amazonUtil;

    @Autowired
    ReturnJsonUtil returnJsonUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ComboItemRepository comboItemRepository;

    @Override
    public void saveStoreBrand(Long merchantId, StoreBrand storeBrand) throws Exception {
        log.info("++++++++++++ Saving Store " + storeBrand.getStores().size() + " +++++++++++++++");
        MerchantEntity merchantEntity = merchantRepository.findByMerchantId(merchantId);
        if (merchantEntity == null)
            throw new MyException("MRC003");

        StoreBrandEntity brandExists = storeBrandRepository.findByName(storeBrand.getName().trim());
        if (brandExists != null)
            throw new MyException("VLD023");

        String brandLogo = storeBrand.getLogo();
        StoreBrandEntity saveStoreBrand = new StoreBrandEntity();
        saveStoreBrand.setName(storeBrand.getName());
        saveStoreBrand.setStatus(Status.ACTIVE);
        saveStoreBrand.setMerchant(merchantEntity);
        saveStoreBrand.setDescription(storeBrand.getDescription());
        saveStoreBrand.setServingDistance(storeBrand.getServingDistance());
        saveStoreBrand.setMinOrderAmount(storeBrand.getMinOrderAmount());
        saveStoreBrand.setPlaceOrderBefore(storeBrand.getOrderPlaceBefore());

        List<StoreEntity> stores = new ArrayList<>();
        for (StoreBrand.Store store : storeBrand.getStores()) {
            StoreEntity saveStore = new StoreEntity();
            saveStore.setGivenLocation(store.getGivenLocation());
            saveStore.setAddressNote(store.getAddressNote());
            saveStore.setLatitude(store.getLatitude());
            saveStore.setLongitude(store.getLongitude());
            saveStore.setContactNo(store.getContactNo());
            saveStore.setCreatedDate(DateUtil.getCurrentTimestampSQL());
            saveStore.setStatus(Status.ACTIVE);
            saveStore.setStoreBrand(saveStoreBrand);
            if(store.getAreaId()!=null)
                saveStore.setArea(areaRepository.findByAreaId(store.getAreaId()));
            stores.add(saveStore);
        }

        saveStoreBrand.setStores(stores);
        storeBrandRepository.saveAndFlush(saveStoreBrand);

        if (brandLogo != null && !brandLogo.isEmpty() && brandLogo != null && !brandLogo.isEmpty()) {
            log.info("Uploading brand logo and image to S3 Bucket ");
            String dir = GeneralUtil.separateString("/", "Merchant_" + merchantEntity.getId(), "Brand_" + saveStoreBrand.getId());
            boolean isLocal = MessageBundle.isLocalHost();
            String brandLogoName = "brand_logo" + (isLocal ? "_tmp_" : "_") + saveStoreBrand.getId() + System.currentTimeMillis();
            String s3PathLogo = imageUtil.saveImageToBucket(brandLogo, brandLogoName, dir, true);
            saveStoreBrand.setLogo(s3PathLogo);
            storeBrandRepository.saveAndFlush(saveStoreBrand);
        }
    }

    @Override
    public void updateStoreBrand(Long merchantId, Long brandId, StoreBrand storeBrand) throws Exception {
        log.info("Updating store Brand");
        StoreBrandEntity storeBrandEntity = storeBrandRepository.findByBrandId(brandId);
        if (storeBrandEntity == null)
            throw new MyException("STR001");
        Boolean isBrandNameExist = storeBrandRepository.findByName(storeBrand.getName())==null?false:true;
        if (isBrandNameExist && !storeBrand.getName().equals(storeBrandEntity.getName()))
            throw new MyException("STR003");

        List<StoreEntity> storeEntities = new ArrayList<>();

        List<Long> storeIds = new ArrayList<>();
        List<Long> dbStoreIdList = new ArrayList<>();
        Map<Long, StoreEntity> dbStoreMap = new HashMap<>();

        for (StoreEntity store1 : storeBrandEntity.getStores()) {
            dbStoreIdList.add(store1.getId());
            dbStoreMap.put(store1.getId(), store1);
        }

        for (StoreBrand.Store store : storeBrand.getStores()) {
            StoreEntity storeEntity = storeRepository.findByStoreId(store.getId());
            AreaEntity areaEntity = areaRepository.findByAreaId(store.getAreaId());
            if (areaEntity == null)
                throw new MyException("ARE001");
            if (storeEntity == null) {
                storeEntity = new StoreEntity();
                storeEntity.setStatus(Status.ACTIVE);
                storeEntity.setLatitude(store.getLatitude());
                storeEntity.setLongitude(store.getLongitude());
                storeEntity.setStoreBrand(storeBrandEntity);
            }
            storeEntity.setContactNo(store.getContactNo());
            storeEntity.setArea(areaEntity);
            storeRepository.saveAndFlush(storeEntity);
            storeIds.add(storeEntity.getId());
            storeEntities.add(storeEntity);
        }

        for (Long dbStore : dbStoreIdList) {
            if (!storeIds.contains(dbStore)) {
                dbStoreMap.get(dbStore).setStatus(Status.DELETE);
            }
        }
        storeBrandEntity.setStores(storeEntities);
        storeBrandEntity.setName(storeBrand.getName());
        String brandLogo = storeBrand.getLogo();
        String dbBrandLogo = storeBrandEntity.getLogo();
        storeBrandEntity.setDescription(storeBrand.getDescription());
        storeBrandEntity.setServingDistance(storeBrand.getServingDistance());
        storeBrandEntity.setMinOrderAmount(storeBrand.getMinOrderAmount());
        storeBrandEntity.setPlaceOrderBefore(storeBrand.getOrderPlaceBefore());
        if(!brandLogo.equals(dbBrandLogo))
            storeBrandEntity.setLogo(null);
        storeBrandRepository.saveAndFlush(storeBrandEntity);

        if (brandLogo != null && !brandLogo.isEmpty() && !brandLogo.equals(dbBrandLogo) && !brandLogo.startsWith("http")) {
            log.info("Uploading brand logo to S3 Bucket ");
            if (dbBrandLogo != null && !dbBrandLogo.isEmpty())
                amazonUtil.deleteFileFromBucket(amazonUtil.getAmazonS3Key(dbBrandLogo));
            String dir = GeneralUtil.separateString("/", "Merchant_" + storeBrandEntity.getMerchant().getId(), "Brand_" + storeBrandEntity.getId());
            boolean isLocal = MessageBundle.isLocalHost();
            String brandLogoName = "brand_logo" + (isLocal ? "_tmp_" : "_") + storeBrandEntity.getId() + System.currentTimeMillis();
            String s3PathLogo = imageUtil.saveImageToBucket(brandLogo, brandLogoName, dir, true);
            storeBrandEntity.setLogo(s3PathLogo);
            storeBrandRepository.saveAndFlush(storeBrandEntity);
        }
    }

    @Override
    public StoreBrandEntity getBrandDetails(Long brandId) throws Exception {
        StoreBrandEntity storeBrandEntity = storeBrandRepository.findByBrandId(brandId);
        if (storeBrandEntity == null)
            throw new MyException("STR001");
        String fields = "name,logo,status,description,servingDistance,minOrderAmount,placeOrderBefore";
        StoreBrandEntity returnStoreBrand = (StoreBrandEntity) returnJsonUtil.getJsonObject(storeBrandEntity, fields);

        String field = "latitude,longitude,contactNo,status,givenLocation,addressNote,area";
        Map<String, String> assoc = new HashMap<>();
        Map<String, String> subAssoc = new HashMap<>();
        assoc.put("area", "id,name,latitude,longitude");
        List<StoreEntity> stores = new ArrayList<>();
        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setId(1L);
        for(StoreEntity storeEntity : storeRepository.findByBrandId(brandId)){
            StoreEntity store = (StoreEntity) returnJsonUtil.getJsonObject(storeEntity, field, assoc);
            store.getArea().setParent(areaEntity);
            stores.add(store);
        }
        returnStoreBrand.setStores(stores);
        /*subAssoc.put("parent", "parent,name");*/
        return returnStoreBrand;

    }

    @Override
    public void saveServingArea(Long storeId, List<Long> areaIds) throws Exception {
        StoreEntity storeEntity = storeRepository.findByStoreId(storeId);
        if (storeEntity == null)
            throw new MyException("STR002");
        List<StoreAreaEntity> storesAreas = new ArrayList<>();
        for (Long areaId : areaIds) {
            AreaEntity areaEntity = areaRepository.findByAreaId(areaId);
            if (areaEntity == null)
                throw new MyException("STR004");
            StoreAreaEntity storeAreaEntity = new StoreAreaEntity();
            storeAreaEntity.setStore(storeEntity);
            storeAreaEntity.setArea(areaEntity);
            storesAreas.add(storeAreaEntity);
        }
        storeEntity.setStoresAreas(storesAreas);
        storeRepository.saveAndFlush(storeEntity);
    }

    @Override
    public void updateServingArea(Long storeId, List<Long> areaIds) throws Exception {
        StoreEntity storeEntity = storeRepository.findByStoreId(storeId);
        if (storeEntity == null)
            throw new MyException("STR002");

        List<AreaEntity> savedArea = areaRepository.findAll();
        List<Long> dbStoreIds = new ArrayList<>();
        for(StoreAreaEntity storeArea : storeEntity.getStoresAreas()) {
            dbStoreIds.add(storeArea.getArea().getId());
        }
        List<StoreAreaEntity> toSavedAreas = new ArrayList<>();
        for (AreaEntity area : savedArea) {
            if (areaIds.contains(area.getId()) && !dbStoreIds.contains(area.getId())) {
                StoreAreaEntity storesAreaEntity = new StoreAreaEntity();
                storesAreaEntity.setArea(area);
                storesAreaEntity.setStore(storeEntity);
                toSavedAreas.add(storesAreaEntity);
            }
        }
        storeAreaRepository.deleteStoresAreaNotInId(storeEntity.getId(), areaIds);
        storeAreaRepository.saveAll(toSavedAreas);
    }

    @Override
    public PaginationDto storeListByStatus(long userId, Page page) throws Exception{
        log.info("Getting store list of user: " + userId);
        PaginationDto paginationDto = new PaginationDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new MyException("USR001");
        Integer totalRows = 0;
        List<StoreBrandEntity> storeBrandEntities = new ArrayList<>();
        if (userEntity.getRole().equals(Role.ROLE_MERCHANT)) {
            //totalRows = storeBrandRepository.getTotalNumberOfStoreByStatus(userEntity.getMerchant().getMerchantId(), page.getStatus());
            if (page != null) {
                page.setTotalRows(totalRows);
            }
            storeBrandEntities = storeBrandRepository.getBrandListByStatus(userEntity.getMerchant().getId(), page.getStatus());
        } else if (userEntity.getRole().equals(Role.ROLE_ADMIN) || userEntity.getRole().equals(Role.ROLE_MANAGER) || userEntity.getRole().equals(Role.ROLE_CSR)) {
            //totalRows = storeBrandRepository.getTotalNumberOfStoreByStatus(null, page.getStatus());
            if (page != null) {
                page.setTotalRows(totalRows);
            }
            storeBrandEntities = storeBrandRepository.getBrandListByStatus(page.getStatus());
        }
        paginationDto.setNumberOfRows(totalRows);

        String fields = "id,name,logo,status,stores";
        Map<String, String> assoc = new HashMap<>();
        List<StoreBrandEntity> returnStores = new ArrayList<>();
        assoc.put("stores", "id,addressNote,latitude,longitude,status");
        for (StoreBrandEntity storeBrand : storeBrandEntities) {
            List<StoreEntity> stores = new ArrayList<>();
            for (StoreEntity storeEntity : storeBrand.getStores()) {
                if (!storeEntity.getStatus().equals(Status.DELETE)) {
                    stores.add(storeEntity);
                }
            }
            storeBrand.setStores(stores);
            returnStores.add((StoreBrandEntity) returnJsonUtil.getJsonObject(storeBrand, fields, assoc));
        }
        paginationDto.setData(returnStores);
        return paginationDto;
    }

    @Override
    public List<CategoryEntity> getParentCategories() throws Exception {
        log.info("++++++++++++ Getting Parent Categories +++++++++++++++");
        List<CategoryEntity> categories = new ArrayList<>();
        String fields = "id,name,status";
        for(CategoryEntity category: categoryRepository.findParentCategories()){
            categories.add((CategoryEntity) returnJsonUtil.getJsonObject(category, fields));
        }
        return categories;
    }

    @Override
    public List<CategoryEntity> getActiveParentCategories() throws Exception {
        log.info("++++++++++++ Getting Active Parent Categories +++++++++++++++");
        List<CategoryEntity> categories = new ArrayList<>();
        String fields = "id,name,status";
        for(CategoryEntity category: categoryRepository.findActiveParentCategories()){
            categories.add((CategoryEntity) returnJsonUtil.getJsonObject(category, fields));
        }
        return categories;
    }

    @Override
    public ComboItemEntity getComboItemDetail(long id) throws Exception {
        log.info("++++++++++++ getting item detail +++++++++++++++");
        ComboItemEntity comboItemEntity = comboItemRepository.findByComboItemId(id);
        if (comboItemEntity == null) {
            throw new MyException("ITM001");
        }

        String fields = "id,name,imageUrl,description,overview,unitPrice,minPerson,availableStartTime,availableEndTime,storeBrand,category,items";
        Map<String, String> assoc = new HashMap<>();

        assoc.put("storeBrand", "id,name,logo");
        assoc.put("category", "id,name");
        assoc.put("items", "id,name,quantity,description");
        return (ComboItemEntity) ReturnJsonUtil.getJsonObject(comboItemEntity, fields, assoc);
    }

    @Override
    public List<ComboItemEntity> getStoreItemList(long brandId) throws Exception {
        log.info("++++++++++++ getting item detail +++++++++++++++");
        StoreBrandEntity storeBrandEntity = storeBrandRepository.findByBrandId(brandId);
        if (storeBrandEntity == null)
            throw new MyException("STR001");
        String fields = "id,name,imageUrl,description,overview,unitPrice,minPerson,availableStartTime,availableEndTime,storeBrand,category";
        Map<String, String> assoc = new HashMap<>();
        assoc.put("storeBrand","id,name,logo");
        assoc.put("category","id,name");
        List<ComboItemEntity> comboItems = new ArrayList<>();
        for(ComboItemEntity comboItem: storeBrandEntity.getComboItems()){
            comboItems.add((ComboItemEntity) ReturnJsonUtil.getJsonObject(comboItem, fields, assoc));
        }
        return comboItems;
    }

    @Override
    public List<CategoryEntity> getStoreCategoryWiseItemList(long brandId) throws Exception {
        log.info("++++++++++++ getting item detail +++++++++++++++");
        StoreBrandEntity storeBrandEntity = storeBrandRepository.findByBrandId(brandId);
        if (storeBrandEntity == null)
            throw new MyException("STR001");
        List<Long> ids = new ArrayList<>();



        String fields = "id,name,comboItems";
        Map<String, String> assoc = new HashMap<>();
        assoc.put("comboItems","id,name,imageUrl,description,overview,unitPrice");
        List<CategoryEntity> categories = new ArrayList<>();
        for(CategoryEntity category : categoryRepository.findActiveParentCategories()){
            categories.add((CategoryEntity) ReturnJsonUtil.getJsonObject(category, fields, assoc));
        }
        return categories;
    }
}
