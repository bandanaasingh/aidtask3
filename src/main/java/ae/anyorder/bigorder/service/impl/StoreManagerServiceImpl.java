package ae.anyorder.bigorder.service.impl;

import ae.anyorder.bigorder.abs.AbstractManager;
import ae.anyorder.bigorder.apiModel.FCMWebToken;
import ae.anyorder.bigorder.apiModel.ComboItem;
import ae.anyorder.bigorder.apiModel.Items;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.exception.MyException;
import ae.anyorder.bigorder.model.*;
import ae.anyorder.bigorder.repository.*;
import ae.anyorder.bigorder.service.StoreManagerService;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.ImageUtil;
import ae.anyorder.bigorder.util.MessageBundle;
import ae.anyorder.bigorder.util.ReturnJsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Frank on 4/10/2018.
 */
@Service
public class StoreManagerServiceImpl extends AbstractManager implements StoreManagerService{
    private static Logger log = Logger.getLogger(StoreManagerService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    FCMWebTokenRepository fcmWebTokenRepository;

    @Autowired
    StoreBrandRepository storeBrandRepository;

    @Autowired
    ComboItemRepository comboItemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    MessageBundle messageBundle;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    ReturnJsonUtil returnJsonUtil;

    @Autowired
    ItemRepository itemRepository;

    @Override
    public void saveFCMWebToken(Long userId, FCMWebToken fcmWebToken) throws Exception {
        log.info("Saving User Web Token");
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity==null)
            throw new MyException("USR001");
        FCMWebTokenEntity userWebTokenEntity = fcmWebTokenRepository.findByTokenAndUserId(fcmWebToken.getWebToken(), userId);
        if (userWebTokenEntity == null)
            userWebTokenEntity = new FCMWebTokenEntity();
        userWebTokenEntity.setVersion(fcmWebToken.getVersion());
        userWebTokenEntity.setBrowser(fcmWebToken.getBrowser());
        userWebTokenEntity.setWebToken(fcmWebToken.getWebToken());
        userWebTokenEntity.setUser(userEntity);
        fcmWebTokenRepository.saveAndFlush(userWebTokenEntity);
    }

    @Override
    public void saveItem(long brandId, ComboItem comboItem) throws Exception {
        log.info("Saving combo item");
        StoreBrandEntity storeBrandEntity = storeBrandRepository.findByBrandId(brandId);
        if(storeBrandEntity==null)
            throw new MyException("STR001");
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(comboItem.getCategoryId());
        if(categoryEntity==null)
            throw new MyException("CAT001");
        ComboItemEntity comboItemEntity = new ComboItemEntity();
        comboItemEntity.setAvailableStartTime(comboItem.getAvailableStartTime());
        comboItemEntity.setAvailableEndTime(comboItem.getAvailableEndTime());
        comboItemEntity.setDescription(comboItem.getDescription());
        comboItemEntity.setOverview(comboItem.getOverview());
        comboItemEntity.setUnitPrice(comboItem.getUnitPrice());
        comboItemEntity.setMinPerson(comboItem.getMinPerson());
        comboItemEntity.setName(comboItem.getName());
        comboItemEntity.setOverview(comboItem.getOverview());
        comboItemEntity.setCategory(categoryEntity);
        comboItemEntity.setStoreBrand(storeBrandEntity);
        for(Items item: comboItem.getItems()){
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setName(item.getName());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setDescription(item.getDescription());
            itemEntity.setName(item.getName());
            itemEntity.setComboItem(comboItemEntity);
            itemRepository.saveAndFlush(itemEntity);
        }

        //Image upload
        String s3PathLogo = "";
        if(comboItem.getImageUrl()!=null) {
            log.info("Uploading brand logo to S3 Bucket ");
            String dir = generalUtil.separateString("/", "Merchant_" + storeBrandEntity.getMerchant().getId(), "Brand_" + storeBrandEntity.getId());
            boolean isLocal = messageBundle.isLocalHost();
            String itemName = "brand_logo" + (isLocal ? "_tmp_" : "_") + storeBrandEntity.getId() + System.currentTimeMillis();
            s3PathLogo = imageUtil.saveImageToBucket(comboItem.getImageUrl(), itemName, dir, true);
            comboItemEntity.setImageUrl(s3PathLogo);
        }
        comboItemRepository.saveAndFlush(comboItemEntity);
    }

    @Override
    public void updateItem(String itemId, ComboItem comboItem) throws Exception {
        log.info("Updating items");
        ComboItemEntity comboItemEntity = comboItemRepository.findByComboItemId(Long.parseLong(itemId));
        if(comboItemEntity==null)
            throw new MyException("CITM001");
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(comboItem.getCategoryId());
        if(categoryEntity==null)
            throw new MyException("CAT001");
        comboItemEntity.setAvailableStartTime(comboItem.getAvailableStartTime());
        comboItemEntity.setAvailableEndTime(comboItem.getAvailableEndTime());
        comboItemEntity.setDescription(comboItem.getDescription());
        comboItemEntity.setOverview(comboItem.getOverview());
        comboItemEntity.setUnitPrice(comboItem.getUnitPrice());
        comboItemEntity.setMinPerson(comboItem.getMinPerson());
        comboItemEntity.setName(comboItem.getName());
        comboItemEntity.setOverview(comboItem.getOverview());
        comboItemEntity.setCategory(categoryEntity);

        List<ItemEntity> itemList = comboItemEntity.getItems();

        List<Long> ids = new ArrayList<>();
        Map<Long, ItemEntity> itemEntityMap = new HashMap<>();
        for(Items item: comboItem.getItems()) {
            if(item.getId()!=null){
                ids.add(item.getId());
            }
        }

        for (ItemEntity item : itemList) {
            itemEntityMap.put(item.getId(), item);
        }



        /*for (StoreEntity dbStore:dbStores){
            if(dbStore.getId() != null)
                dbStoreWithId.put(dbStore.getId(), dbStore);
        }*/


        for(ItemEntity item : itemList){
            Boolean removeItem = Boolean.TRUE;
            for(Items items : comboItem.getItems()){
                if(items.getId()==null){
                    //add item
                    ItemEntity itemEntity = new ItemEntity();
                    itemEntity.setName(item.getName());
                    itemEntity.setDescription(item.getDescription());
                    itemEntity.setQuantity(item.getQuantity());
                    itemEntity.setName(item.getName());
                    itemEntity.setComboItem(comboItemEntity);
                    //itemRepository.saveAndFlush(itemEntity);
                    itemList.add(itemEntity);
                } else if(ids.contains(item.getId())){
                    //update stuff here
                    item.setName(item.getName());
                    item.setDescription(item.getDescription());
                    item.setQuantity(item.getQuantity());
                    item.setName(item.getName());
                }
            }
        }
        comboItemEntity.setItems(itemList);

       /* Iterator items = itemEntityMap.entrySet().iterator();
        while (items.hasNext()) {
            Map.Entry item = (Map.Entry) items.next();
            item.get
        }*/

        /*for(Items item: comboItem.getItems()){
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setName(item.getName());
            itemEntity.setDescription(item.getDescription());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setName(item.getName());
            itemEntity.setComboItem(comboItemEntity);
            itemRepository.saveAndFlush(itemEntity);
        }*/

        /*for(ComboItem.Item item: comboItem.getItems()){
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setName(item.getName());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setDescription(item.getDescription());
            itemEntity.setName(item.getName());
        }*/

        //Image upload
        String s3PathLogo = "";
        if(comboItem.getImageUrl()!=null) {
            log.info("Uploading brand logo to S3 Bucket ");
            String dir = generalUtil.separateString("/", "Merchant_" + comboItemEntity.getStoreBrand().getMerchant().getId(), "Brand_" + comboItemEntity.getStoreBrand().getId());
            boolean isLocal = messageBundle.isLocalHost();
            String itemName = "brand_logo" + (isLocal ? "_tmp_" : "_") + comboItemEntity.getStoreBrand().getId() + System.currentTimeMillis();
            s3PathLogo = imageUtil.saveImageToBucket(comboItem.getImageUrl(), itemName, dir, true);
            comboItemEntity.setImageUrl(s3PathLogo);
        }
        comboItemRepository.saveAndFlush(comboItemEntity);
    }

    @Override
    public List<AreaEntity> getAreas() throws Exception {
        List<AreaEntity> parentAreas = areaRepository.findParentAreas();
        List<AreaEntity> areas = new ArrayList<>();

        String fields = "id,name,child,status";
        Map<String, String> assoc = new HashMap<>();
        Map<String, String> subAssoc = new HashMap<>();

        assoc.put("child", "id,name,child,status");
        subAssoc.put("child", "id,name,child,latitude,longitude,street,status");
        for (AreaEntity area : parentAreas) {
            areas.add((AreaEntity) returnJsonUtil.getJsonObject(area, fields, assoc));
        }
        return areas;
    }

    @Override
    public AreaEntity getArea(long id) throws Exception {
        AreaEntity areaEntity = areaRepository.findByAreaId(id);
        if (areaEntity == null)
            throw new MyException("ARE001");
        String fields = "id,name,latitude,longitude,street,parent,status,child";
        Map<String, String> assoc = new HashMap<>();
        Map<String, String> subAssoc = new HashMap<>();
        assoc.put("parent", "id,name,parent,status");
        assoc.put("child", "id,name,status");
        subAssoc.put("parent", "id,name,parent,status");
        return (AreaEntity) returnJsonUtil.getJsonObject(areaEntity, fields, assoc);
    }

    @Override
    public List<AreaEntity> getActiveAreas() throws Exception {
        log.info("Getting active area and subarea");
        List<AreaEntity> parentAreas = areaRepository.findActiveParentAreas(Status.ACTIVE);
        List<AreaEntity> areas = new ArrayList<>();

        String fields = "id,name,child,status";
        Map<String, String> assoc = new HashMap<>();
        Map<String, String> subAssoc = new HashMap<>();

        assoc.put("child", "id,name,child,status");
        subAssoc.put("child", "id,name,child,latitude,longitude,street,status");
        parseActiveAreaTree(parentAreas);
        for (AreaEntity area : parentAreas) {
            areas.add((AreaEntity) returnJsonUtil.getJsonObject(area, fields, assoc));
        }
        return areas;
    }
}
