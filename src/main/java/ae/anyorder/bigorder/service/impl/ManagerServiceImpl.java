package ae.anyorder.bigorder.service.impl;

import ae.anyorder.bigorder.abs.AbstractManager;
import ae.anyorder.bigorder.apiModel.Area;
import ae.anyorder.bigorder.apiModel.Category;
import ae.anyorder.bigorder.apiModel.UpdateStatus;
import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.dto.PaginationDto;
import ae.anyorder.bigorder.dto.PushNotification;
import ae.anyorder.bigorder.dto.SendNotification;
import ae.anyorder.bigorder.enums.PreferenceType;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.exception.MyException;
import ae.anyorder.bigorder.model.AreaEntity;
import ae.anyorder.bigorder.model.CategoryEntity;
import ae.anyorder.bigorder.model.MerchantEntity;
import ae.anyorder.bigorder.repository.AreaRepository;
import ae.anyorder.bigorder.repository.CategoryRepository;
import ae.anyorder.bigorder.repository.FCMWebTokenRepository;
import ae.anyorder.bigorder.repository.MerchantRepository;
import ae.anyorder.bigorder.service.ManagerService;
import ae.anyorder.bigorder.service.SystemPropertyService;
import ae.anyorder.bigorder.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 4/7/2018.
 */
@Service
public class ManagerServiceImpl extends AbstractManager implements ManagerService{
    private static Logger log = Logger.getLogger(ManagerServiceImpl.class);

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    FCMWebTokenRepository fcmWebTokenRepository;

    @Autowired
    FCMNotificationUtil fcmNotificationUtil;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ReturnJsonUtil returnJsonUtil;

    @Autowired
    SystemPropertyService systemPropertyService;

    @Autowired
    JavaMailSender emailSender;

    private static void parseAreaTree(AreaEntity parentAreaEntity, Integer areaId) {
        //for (Area  area: parenAreas) {
        if (parentAreaEntity != null && parentAreaEntity.getId() != null) {
            if (parentAreaEntity.getId().equals(areaId))
                throw new MyException("ARE004");
            parseAreaTree(parentAreaEntity.getParent(), areaId);
        }
    }

    @Override
    public void saveArea(Area area) throws Exception {
        log.info("Saving area " + area.getName());
        AreaEntity areaEntity = new AreaEntity();
        List<AreaEntity> parentAreas = new ArrayList<>();
        if (area.getParentId() != null) {
            AreaEntity parent = areaRepository.findByAreaId(area.getParentId());
            if (parent == null)
                throw new MyException("ARE002");
            parentAreas = areaRepository.findChildOfParentArea(parent.getId());
            areaEntity.setParent(parent);
        } else {
            parentAreas = areaRepository.findParentAreas();
        }
        if (parentAreas != null && parentAreas.size() > 0) {
            for (AreaEntity area1 : parentAreas) {
                if (area1.getName().equals(area.getName())) {
                    throw new MyException("ARE003");
                }
            }
        }
        areaEntity.setLatitude(area.getLatitude());
        areaEntity.setLongitude(area.getLongitude());
        areaEntity.setName(area.getName());
        areaEntity.setStatus(Status.ACTIVE);
        areaEntity.setStreet(area.getStreet());
        areaRepository.saveAndFlush(areaEntity);
    }

    @Override
    public void updateArea(Long areaId, Area area) throws Exception {
        log.info("Updating area " + area.getName());
        AreaEntity areaEntity = areaRepository.findByAreaId(areaId);
        if (areaEntity == null)
            throw new MyException("ARE001");
        List<AreaEntity> parentAreas = new ArrayList<>();
        if (area.getParentId() != null)
            parentAreas = areaRepository.findChildOfParentArea(area.getParentId());
        else
            parentAreas = areaRepository.findParentAreas();
        if (parentAreas != null && parentAreas.size() > 0) {
            for (AreaEntity dbAreaEntity : parentAreas) {
                if (dbAreaEntity.getName().equals(area.getName()) && !areaId.equals(dbAreaEntity.getId())) {
                    throw new MyException("ARE003");
                }
            }
        }
        areaEntity.setName(area.getName());
        areaEntity.setLatitude(area.getLatitude());
        areaEntity.setLongitude(area.getLongitude());
        areaEntity.setStreet(area.getStreet());
        areaRepository.saveAndFlush(areaEntity);
    }

    @Override
    public void saveCategory(Category category) throws Exception {
        log.info("Saving category "+category.getName());
        CategoryEntity categoryEntity = categoryRepository.findByName(category.getName());
        if(categoryEntity!=null)
            throw new MyException("CAT001");
        else {
            categoryEntity = new CategoryEntity();
            categoryEntity.setName(category.getName());
            categoryEntity.setStatus(Status.ACTIVE);
        }
        categoryRepository.saveAndFlush(categoryEntity);
    }

    @Override
    public void updateCategory(Long id, Category category) throws Exception {
        log.info("Updating category "+category.getName());
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(id);
        if(categoryEntity==null)
            throw new MyException("CAT001");
        if(!categoryEntity.getName().equals(category.getName())) {
            CategoryEntity dbcategory = categoryRepository.findByName(category.getName());
            if(dbcategory!=null)
                throw new MyException("CAT002");
        }
        categoryEntity.setName(category.getName());
        categoryEntity.setStatus(category.getStatus());
        categoryRepository.saveAndFlush(categoryEntity);
    }

    @Override
    public void sendPushNotification(SendNotification notification) throws Exception {
        log.info("Sending push Notification");
        List<String> tokens = fcmWebTokenRepository.findTokenByUserIds(notification.getUserIds());
        if(tokens.size()>0){
            Map<String, String> message = new HashMap<>();
            message.put("title", "test");
            message.put("body", "body message");
            PushNotification pushNotification = new PushNotification();
            pushNotification.setMessage(message);
            pushNotification.setTokens(tokens);
            fcmNotificationUtil.sendFCMPushNotification(pushNotification);
            log.info("Notification send successfully.");
        }
    }

    @Override
    public PaginationDto getMerchantList(Page page) throws Exception {
        log.info("----- Getting merchant list. -----");
        PaginationDto paginationDto = new PaginationDto();

        if (page != null && page.getSearchFor() != null && !page.getSearchFor().equals("")) {
            Map<String, String> fieldsMap = new HashMap<>();
            fieldsMap.put("self", "merchantId,businessTitle");
            fieldsMap.put("user", "firstName,lastName,email,mobileNumber");
            page.setSearchFields(fieldsMap);
        }
        //Integer totalRows = merchantRepository.getNumberOfMerchant();
        //page.setTotalRows(totalRows);
        //paginationDto.setNumberOfRows(totalRows);

        List<MerchantEntity> merchantEntities = merchantRepository.getMerchantList(page);

        String field = "id,businessTitle,user";
        Map<String, String> assoc = new HashMap<>();
        assoc.put("user", "id,firstName,lastName,status,email,mobileNumber");

        List<MerchantEntity> merchantList = new ArrayList<>();
        for (MerchantEntity merchantEntity : merchantEntities) {
            MerchantEntity merchantEntityFiltered = (MerchantEntity) returnJsonUtil.getJsonObject(merchantEntity, field, assoc);
            merchantList.add(merchantEntityFiltered);
        }
        paginationDto.setData(merchantList);
        return paginationDto;
    }

    @Override
    public void changeMerchantStatus(Long merchantId, UpdateStatus status) throws Exception {
        log.info("----- Changing merchant's status. -----");
        MerchantEntity merchantEntity = merchantRepository.findByMerchantId(merchantId);
        if (merchantEntity == null)
            throw new MyException("MRC001");
        if (status.getStatus() != null && !status.getStatus().equals(merchantEntity.getUser().getStatus())) {
            merchantEntity.getUser().setStatus(status.getStatus());
            merchantRepository.saveAndFlush(merchantEntity);

            log.info("Sending mail to " + merchantEntity.getUser().getEmail());

            String body = EmailUtil.notificationEmailUserStatus(merchantEntity.getUser(), getServerUrl());
            String subject = systemPropertyService.readPrefValue(PreferenceType.APPLICATION_NAME) + ": Your account has been";
            if (status.getStatus().equals(Status.ACTIVE)) {
                subject += " re-activated.";
            } else if (status.getStatus().equals(Status.INACTIVE)) {
                subject += " deactivated.";
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(merchantEntity.getUser().getEmail());
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        }
    }
}
