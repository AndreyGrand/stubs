package ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manaev on 8/2/16.
 *
 */

/**
 * <p/>
 * заполняем map для классов запросов ответов
 */
@Service
public class CRMClassBoundsImpl implements CRMClassBounds {

    private static final Logger log = LoggerFactory.getLogger(CRMClassBoundsImpl.class);

    private Map<String, ReqResClass> knownClasses = new HashMap<>();

    private final String[] scanningPackages = new String[]{"ru.sbrf.efs.integration.esb.fs"};

    public Map<String, ReqResClass> getKnownClasses() {
        return knownClasses;
    }

    private Map<String, Map<String, String>> getVisibleNames() throws CRMMessageProcessingException {
        Map<String, Map<String, String>> visibleNames = new HashMap<>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(true);

        scanner.addIncludeFilter(new AnnotationTypeFilter(XmlRootElement.class));

        for (String packageEntry : scanningPackages) {
            for (BeanDefinition bd : scanner.findCandidateComponents(packageEntry)) {
                String className = bd.getBeanClassName();
                /**
                 * это плохо, но у классов запросов-ответов, всегда такое окончание
                 */
                if (className.toLowerCase().endsWith("rqtype") || className.toLowerCase().endsWith("rstype")) {
                    /**
                     * нашли пакет с запросом или ответом до сервиса ксш, надо попробовать найти название сервиса - он идет сразуже после packageEntry,
                     * отсекаем имя пакета и берем первое!(опять плохо) вхождение до '.'
                     */
                    String temp = className.replace(packageEntry.concat("."), "").split("\\.")[0].toLowerCase();

                    /**
                     * отичается от имени класса, а в сообщениях приходит именно оно(возможно несовпадение регистра)
                     */
                    String bindingName = ((ScannedGenericBeanDefinition) bd).getMetadata().getAnnotationAttributes(XmlRootElement.class.getName()).get("name").toString();
                    /**
                     * есть надежда, что угадали - косвенная проверка - есть srv в начале имени сервиса
                     */
                    if (!temp.toLowerCase().startsWith("srv")) {
                        throw new CRMMessageProcessingException("Unable to load visible names - java classes of operations are unknown");
                    }
                    if (!visibleNames.containsKey(temp)) {
                        visibleNames.put(temp, new HashMap<String, String>());
                    }
                    visibleNames.get(temp).put(bindingName.toLowerCase(), className);
                }
            }
        }
        return visibleNames;
    }

    /**
     * загрузка всех возможных классов проаннотированных XmlRootElement в указанных в scaningPackages пакетах, имещих отношение к операциям в КСШ
     * @throws CRMMessageProcessingException
     */
    @PostConstruct
    private void loadVisibleNames() throws CRMMessageProcessingException {

        knownClasses.clear();

        Map<String, Map<String, String>> visibleNames = getVisibleNames();
        /**
         * идем по составленным именая и заполняем knownClasses
         */
        try {
            for (Map.Entry<String, Map<String, String>> entry : visibleNames.entrySet()) {
                String baseKey = entry.getKey();
                Map.Entry<String, String> rqPart = getBySuffix(entry.getValue(), "rq");
                Map.Entry<String, String> rsPart = getBySuffix(entry.getValue(), "rs");
                Class reqClass = Class.forName(rqPart.getValue());
                Class resClass = Class.forName(rsPart.getValue());
                /**
                 * добавляем в двух порядках, так так неизвестно как будут приходить запросы
                 */
                knownClasses.put(baseKey.concat("_").concat(rqPart.getKey()), new ReqResClass(reqClass, resClass));
                knownClasses.put(baseKey.concat("_").concat(rsPart.getKey()), new ReqResClass(resClass, reqClass));
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new CRMMessageProcessingException(e.getMessage());
        }

    }

    private Map.Entry<String, String> getBySuffix(Map<String, String> entry, String suffix) throws CRMMessageProcessingException {
        for (Map.Entry<String, String> rEntry : entry.entrySet()) {
            if (rEntry.getKey().endsWith(suffix)) {
                return rEntry;
            }
        }
        throw new CRMMessageProcessingException("Unable to load visible names - java classes of operations are unknown");
    }

    /**
     * получение классов запроса и ответа для операции, если имя сервиса пусто,
     * вернет первое повавшееся из map knownClasses, где после '_' в ключе идет @param operationName
     *
     * @param operationName фактически указание конкретного name у какого-то класса аннотированного XmlRootElement.class
     * @return классы для данной операции
     */
    @Override
    public ReqResClass getBoundClasses(String operationName) {
        for(Map.Entry<String,ReqResClass> entry : knownClasses.entrySet()){
            if(entry.getKey().split("_")[1].equals(operationName.toLowerCase())){
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * получение классов имени запроса по названию операции
     *
     * @param operationName фактически указание конкретного name у какого-то класса аннотированного XmlRootElement.class
     * @return классы для данной операции
     */
    @Override
    public String getBoundServiceName(String operationName) {
        for(Map.Entry<String,ReqResClass> entry : knownClasses.entrySet()){
            /**
             * первая часть - имчя сервиса, вторая - операции
             */
            String[] tarr = entry.getKey().split("_");
            if(tarr[1].equals(operationName.toLowerCase())){
                return tarr[0];
            }
        }
        return null;
    }

    /**
     * получение классов запроса и ответа для сервиса и операции(плохо себе представляю политику маршрутизации, целиком гипотеза)
     *
     * @param serviceName имя сервиса = пакет в котором лежит клиент до сервиса КСШ
     * @param operationName фактически указание конкретного name у какого-то класса аннотированного XmlRootElement.class
     * @return классы для данной операции
     */
    @Override
    public ReqResClass getBoundClasses(String serviceName, String operationName) {
        return knownClasses.get(serviceName.toLowerCase().concat("_").concat(operationName.toLowerCase()));
    }



    public static class ReqResClass {

        final Class reqClass;

        final Class respClass;

        public ReqResClass(Class reqClass, Class respClass) {
            this.reqClass = reqClass;
            this.respClass = respClass;
        }

        public Class getReqClass() {
            return reqClass;
        }

        public Class getRespClass() {
            return respClass;
        }
    }
}
