package ae.anyorder.bigorder.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 10/17/2017.
 */
@Component
public class ReturnJsonUtil {
    private static final String MODEL_NAME = "ae.anyorder.bigOrder.model";
    private static final String ENUM_NAME = "ae.anyorder.bigOrder.enums";
    private static final String PERSISTENT_NAME = "org.hibernate.collection.internal.PersistentBag";
    private static final String ARRAY_NAME = "java.util.ArrayList";
    private static final String TIME_NAME = "java.sql.Time";

    public static Object getJsonObject(Object defaultObject, String fields) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object rtnObject = BeanUtils.instantiateClass(defaultObject.getClass());
        String[] arrFields = fields.split(",");
        for (String field : arrFields) {
            PropertyUtils.setProperty(rtnObject, field, PropertyUtils.getProperty(defaultObject, field));
        }
        return rtnObject;
    }


    public static Object getJsonObject(Object defaultObject, String fields, Map<String, String> assoc) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object rtnObject = BeanUtils.instantiateClass(defaultObject.getClass());
        String[] arrFields = fields.split(",");

        for (String field : arrFields) {
            PropertyUtils.setProperty(rtnObject, field, PropertyUtils.getProperty(defaultObject, field));
        }
        if (assoc != null) {
            for (Map.Entry<String, String> param : assoc.entrySet()) {
                if (PropertyUtils.getProperty(defaultObject, param.getKey()) != null) {
                    if (PropertyUtils.getProperty(defaultObject, param.getKey()).getClass().getName().equals(PERSISTENT_NAME) || PropertyUtils.getProperty(defaultObject, param.getKey()).getClass().getName().equals(ARRAY_NAME)) {
                        List<Object> assocDBs = (List<Object>) PropertyUtils.getProperty(defaultObject, param.getKey());
                        List<Object> assocRtnList = new ArrayList<>();

                        for (Object assocDB : assocDBs) {
                            Object assocRtnObj = new Object();
                            if (assocDB.getClass().toString().contains(ENUM_NAME) || assocDB.getClass().toString().contains(TIME_NAME)) {
                                assocRtnObj = assocDB;
                            } else {
                                assocRtnObj = BeanUtils.instantiateClass(assocDB.getClass());
                            }
                            if (param.getValue() != null) {
                                String assocFields = param.getValue();
                                String[] arrAssocFields = assocFields.split(",");
                                for (String assocF : arrAssocFields) {
                                    setValues(assocDB, assocRtnObj, assocF.trim());
                                }
                            }
                            assocRtnList.add(assocRtnObj);
                        }
                        PropertyUtils.setProperty(rtnObject, param.getKey(), assocRtnList);
                    } else {
                        Object assocDB = PropertyUtils.getProperty(defaultObject, param.getKey());
                        Object assocRtnObj = BeanUtils.instantiateClass(PropertyUtils.getProperty(defaultObject, param.getKey()).getClass());
                        if (param.getValue() != null) {
                            String assocFields = param.getValue();
                            String[] arrAssocFields = assocFields.split(",");
                            for (String assocF : arrAssocFields) {
                                if (PropertyUtils.getProperty(assocDB, assocF) != null) {
                                    if (PropertyUtils.getProperty(assocDB, assocF).getClass().toString().contains(MODEL_NAME)) {
                                        Object assoc2ndDB = PropertyUtils.getProperty(assocDB, assocF.trim());
                                        Object assoc2ndRtnObj = BeanUtils.instantiateClass(assoc2ndDB.getClass());
                                        PropertyUtils.setProperty(assocRtnObj, assocF.trim(), assoc2ndRtnObj);
                                    } else {
                                        PropertyUtils.setProperty(assocRtnObj, assocF.trim(), PropertyUtils.getProperty(assocDB, assocF.trim()));
                                    }
                                }
                            }
                        }
                        PropertyUtils.setProperty(rtnObject, param.getKey(), assocRtnObj);
                    }
                }
            }
        }
        return rtnObject;
    }

    public static Object getJsonObject(Object defaultObject, String fields, Map<String, String> assoc, Map<String, String> subAssoc) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        //instantiate the default object
        Object rtnObject = BeanUtils.instantiateClass(defaultObject.getClass());

        //set fields of the default object to the return object
        String[] arrFields = fields.split(",");

        for (String field : arrFields) {
            PropertyUtils.setProperty(rtnObject, field.trim(), PropertyUtils.getProperty(defaultObject, field.trim()));
        }

        //get the map of associated model and and related fields and set the values to the return object
        if (assoc != null) {
            for (Map.Entry<String, String> param : assoc.entrySet()) {

                /*check if the result of the associated model is collection or single
                 if collection iterate and set fields to return object
                 else set values of fields in return object directly
                 */
                if (PropertyUtils.getProperty(defaultObject, param.getKey()) != null) {
                    if (PropertyUtils.getProperty(defaultObject, param.getKey()).getClass().getName().equals(PERSISTENT_NAME) || PropertyUtils.getProperty(defaultObject, param.getKey()).getClass().getName().equals(ARRAY_NAME)) {

                        List<Object> assocDBs = (List<Object>) PropertyUtils.getProperty(defaultObject, param.getKey());
                        List<Object> assocRtnList = new ArrayList<>();

                        for (Object assocDB : assocDBs) {
                            Object assocRtnObj = BeanUtils.instantiateClass(assocDB.getClass());
                            if (!assocDB.getClass().getName().equals(ARRAY_NAME) && !assocDB.getClass().getName().equals(PERSISTENT_NAME) && !assocDB.getClass().toString().contains(MODEL_NAME)) {
                                assocRtnList.add(assocDB);
                            } else if (param.getValue() != null) {
                                String assocFields = param.getValue();
                                String[] arrAssocFields = assocFields.split(",");
                                for (String assocF : arrAssocFields) {
                                    /*if value of field is collection than this is the another model - iterate and set values of the fields of the related  2nd level associated  model
                                      else check if the value is  another model or not
                                      if the value is another model than iterate and set the value of the fields of the related  2nd level associated  model
                                      else set the values of fields of related model directly
                                     */

                                    setValues(assocDB, assocRtnObj, assocF.trim(), subAssoc);
                                }
                                assocRtnList.add(assocRtnObj);
                            } else {
                                assocRtnList.add(assocRtnObj);
                            }
                        }
                        PropertyUtils.setProperty(rtnObject, param.getKey(), assocRtnList);
                    } else {
                        Object assocDB = PropertyUtils.getProperty(defaultObject, param.getKey());
                        Object assocRtnObj = BeanUtils.instantiateClass(PropertyUtils.getProperty(defaultObject, param.getKey()).getClass());
                        if (param.getValue() != null) {
                            String assocFields = param.getValue();
                            String[] arrAssocFields = assocFields.split(",");
                            for (String assocF : arrAssocFields) {
                                if (PropertyUtils.getProperty(assocDB, assocF) != null ) {
                                    if (PropertyUtils.getProperty(assocDB, assocF).getClass().getName().equals(PERSISTENT_NAME) || PropertyUtils.getProperty(assocDB, assocF).getClass().getName().equals(ARRAY_NAME)) {
                                        List<Object> assoc2ndDBs = (List<Object>) PropertyUtils.getProperty(assocDB, assocF.trim());
                                        List<Object> assoc2ndRtn = new ArrayList<>();
                                        for (Object assoc2ndDB : assoc2ndDBs) {
                                            Object assoc2ndRtnObj = new Object();
                                            if (assoc2ndDB.getClass().toString().contains(ENUM_NAME) || assoc2ndDB.getClass().toString().contains(TIME_NAME)) {
                                                assoc2ndRtnObj = assoc2ndDB;
                                            } else {
                                                assoc2ndRtnObj = BeanUtils.instantiateClass(assoc2ndDB.getClass());
                                            }
                                            if (subAssoc != null) {
                                                String accoc2ndFs = subAssoc.get(assocF.trim());
                                                if (accoc2ndFs != null) {
                                                    String[] arrAssoc2ndFields = accoc2ndFs.split(",");
                                                    for (String accoc2ndF : arrAssoc2ndFields) {
                                                        //PropertyUtils.setProperty(assoc2ndRtnObj, accoc2ndF,  PropertyUtils.getProperty(assoc2ndDB, accoc2ndF));
                                                        setValues(assoc2ndDB, assoc2ndRtnObj, accoc2ndF.trim(), subAssoc);
                                                    }
                                                }
                                            }
                                            assoc2ndRtn.add(assoc2ndRtnObj);
                                        }
                                        PropertyUtils.setProperty(assocRtnObj, assocF.trim(), assoc2ndRtn);
                                    } else if (PropertyUtils.getProperty(assocDB, assocF).getClass().toString().contains(MODEL_NAME)) {
                                        //PropertyUtils.setProperty(assocRtn, assocF,  PropertyUtils.getProperty(assocDB, assocF));
                                        Object assoc2ndDB = PropertyUtils.getProperty(assocDB, assocF.trim());
                                        Object assoc2ndRtnObj = BeanUtils.instantiateClass(assoc2ndDB.getClass());
                                        if (subAssoc != null) {
                                            String accoc2ndFs = subAssoc.get(assocF.trim());
                                            if (accoc2ndFs != null) {
                                                String[] arrAssoc2ndFields = accoc2ndFs.split(",");
                                                for (String accoc2ndF : arrAssoc2ndFields) {
                                                    //PropertyUtils.setProperty(assoc2ndRtnObj, accoc2ndF,  PropertyUtils.getProperty(assoc2ndDB, accoc2ndF));
                                                    setValues(assoc2ndDB, assoc2ndRtnObj, accoc2ndF.trim(), subAssoc);
                                                }
                                            }
                                        }
                                        PropertyUtils.setProperty(assocRtnObj, assocF.trim(), assoc2ndRtnObj);
                                    } else {
                                        PropertyUtils.setProperty(assocRtnObj, assocF.trim(), PropertyUtils.getProperty(assocDB, assocF.trim()));
                                    }
                                }
                            }
                        }
                        PropertyUtils.setProperty(rtnObject, param.getKey(), assocRtnObj);
                    }
                }
            }
        }
        return rtnObject;
    }


    private static void setValues(Object assocDB, Object assocRtnObj, String assocF, Map<String, String> subAssoc) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (PropertyUtils.getProperty(assocDB, assocF) != null) {
            if (PropertyUtils.getProperty(assocDB, assocF).getClass().getName().equals(PERSISTENT_NAME) || PropertyUtils.getProperty(assocDB, assocF).getClass().getName().equals(ARRAY_NAME)) {
                List<Object> assoc2ndDBs = (List<Object>) PropertyUtils.getProperty(assocDB, assocF.trim());
                List<Object> assoc2ndRtn = new ArrayList<>();
                for (Object assoc2ndDB : assoc2ndDBs) {
                    if (assoc2ndDB.getClass().getName().toString().contains(ENUM_NAME) || assoc2ndDB.getClass().toString().contains(TIME_NAME)) {
                        assoc2ndRtn.add(assoc2ndDB);
                    } else {
                        Object assoc2ndRtnObj = BeanUtils.instantiateClass(assoc2ndDB.getClass());
                        if (subAssoc != null) {
                            String accoc2ndFs = subAssoc.get(assocF.trim());
                            if (accoc2ndFs != null) {
                                String[] arrAssoc2ndFields = accoc2ndFs.split(",");
                                for (String accoc2ndF : arrAssoc2ndFields) {
                                    setValues(assoc2ndDB, assoc2ndRtnObj, accoc2ndF.trim(), subAssoc);
                                }
                            }
                        }
                        assoc2ndRtn.add(assoc2ndRtnObj);
                    }
                }
                PropertyUtils.setProperty(assocRtnObj, assocF.trim(), assoc2ndRtn);
            } else {
                if (PropertyUtils.getProperty(assocDB, assocF.trim()).getClass().toString().contains(MODEL_NAME)) {
                    Object assoc2ndRtnObj = BeanUtils.instantiateClass(PropertyUtils.getProperty(assocDB, assocF.trim()).getClass());
                    if (subAssoc != null) {
                        String accoc2ndFs = subAssoc.get(assocF.trim());
                        if (accoc2ndFs != null) {
                            String[] arrAssoc2ndFields = accoc2ndFs.split(",");
                            for (String accoc2ndF : arrAssoc2ndFields) {
                                setValues(PropertyUtils.getProperty(assocDB, assocF.trim()), assoc2ndRtnObj, accoc2ndF.trim(), subAssoc);
                            }
                        }
                    }
                    PropertyUtils.setProperty(assocRtnObj, assocF.trim(), assoc2ndRtnObj);
                } else {
                    PropertyUtils.setProperty(assocRtnObj, assocF.trim(), PropertyUtils.getProperty(assocDB, assocF.trim()));
                }
            }
        }
    }

    private static void setValues(Object assocDB, Object assocRtnObj, String assocF) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (PropertyUtils.getProperty(assocDB, assocF) != null) {
            if (PropertyUtils.getProperty(assocDB, assocF).getClass().getName().equals(PERSISTENT_NAME) || PropertyUtils.getProperty(assocDB, assocF).getClass().getName().equals(ARRAY_NAME)) {
                List<Object> assoc2ndDBs = (List<Object>) PropertyUtils.getProperty(assocDB, assocF.trim());
                List<Object> assoc2ndRtn = new ArrayList<>();
                for (Object assoc2ndDB : assoc2ndDBs) {
                    Object assoc2ndRtnObj = BeanUtils.instantiateClass(assoc2ndDB.getClass());
                    assoc2ndRtn.add(assoc2ndRtnObj);
                }
                PropertyUtils.setProperty(assocRtnObj, assocF.trim(), assoc2ndRtn);
            } else {
                if (PropertyUtils.getProperty(assocDB, assocF.trim()).getClass().toString().contains(MODEL_NAME)) {
                    Object assoc2ndRtnObj = BeanUtils.instantiateClass(PropertyUtils.getProperty(assocDB, assocF.trim()).getClass());

                    PropertyUtils.setProperty(assocRtnObj, assocF.trim(), assoc2ndRtnObj);
                } else {
                    PropertyUtils.setProperty(assocRtnObj, assocF.trim(), PropertyUtils.getProperty(assocDB, assocF.trim()));
                }
            }
        }
    }
}