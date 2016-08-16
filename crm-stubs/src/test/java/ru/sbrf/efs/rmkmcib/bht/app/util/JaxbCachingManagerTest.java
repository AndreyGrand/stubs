package ru.sbrf.efs.rmkmcib.bht.app.util;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rq.GetLegalClientManagerTaskDetailsRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rq.MessageType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rq.GetLegalClientProfileRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.GetLegalClientProfileRsType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.ListOfAccountType;
import ru.sbrf.efs.rmkmcib.bht.TestUtils;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;

import javax.xml.bind.JAXBException;
import java.util.Map;


public class JaxbCachingManagerTest {

    private static final Logger log = LoggerFactory.getLogger(JaxbCachingManagerTest.class);

    private JaxbCachingManager jaxbCachingManager = null;

    @Before
    public void initJCM() {
        jaxbCachingManager = new JaxbCachingManager();
    }

    @Test
    public void testGetOperationAndRqUID() throws CRMMessageProcessingException {
        Document doc = jaxbCachingManager.getDocFromXmlString(TestUtils.PutLegalClientManagerTaskRq);
        Map<String, String> params = jaxbCachingManager.getFirstMatchingTagTextContent(doc, new String[]{"RqUID"});
        params.put(CRMMessageHeaders.OPERATION_NAME_HEADER.getName(),jaxbCachingManager.getOperationName(doc));
        assert params.get(CRMMessageHeaders.OPERATION_NAME_HEADER.getName()).equals("PutLegalClientManagerTaskRq");
        assert params.get("RqUID").equals("2093b67dcabc47dabec4b3118bc19d65");
    }

    public  ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType getTypeSrvGetLegalClientProfile(String val){
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType type1 = new ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType();
        type1.setId(val);
        type1.setMDMId(val);
        type1.setOGRN(val);
        type1.setVKO(val);
        type1.setVKOEmail(val);
        type1.setFullName(val);
        type1.setVKOPhone(val);
        type1.setSBRFKindActivity(val);
        type1.setSBRFKPP(val);
        type1.setSBRFINN(val);
        type1.setName(val);
        return type1;
    }

    @Test
    public void testMarshallingUnmarshalling() throws JAXBException, ClassNotFoundException {
        final GetLegalClientManagerTaskDetailsRqType tt = new GetLegalClientManagerTaskDetailsRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        MessageType mt = new MessageType();
        mt.setActionId("test");
        tt.setMessage(mt);

        String ttS = jaxbCachingManager.marshall(tt);
        GetLegalClientManagerTaskDetailsRqType ttc = jaxbCachingManager.unmarshall(ttS, GetLegalClientManagerTaskDetailsRqType.class);
        assert tt.getMethod().equals(ttc.getMethod());
        assert tt.getRqUID().equals(ttc.getRqUID());
        assert tt.getRqTm().equals(ttc.getRqTm());
        assert tt.getMessage().getActionId().equals(ttc.getMessage().getActionId());

        final GetLegalClientProfileRqType tt1 = new GetLegalClientProfileRqType();
        tt1.setMethod("test");
        tt1.setRqUID("test");
        tt1.setRqTm("test");
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rq.MessageType messtype1 = new ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rq.MessageType();
        messtype1.setLogin("test");
        messtype1.setPhone("test");
        tt1.setMessage(messtype1);


        String ttS1 = jaxbCachingManager.marshall(tt1);
        GetLegalClientProfileRqType ttc1 = jaxbCachingManager.unmarshall(ttS1, GetLegalClientProfileRqType.class);
        assert tt1.getMethod().equals(ttc1.getMethod());
        assert tt1.getRqUID().equals(ttc1.getRqUID());
        assert tt1.getRqTm().equals(ttc1.getRqTm());
        assert tt1.getMessage().getLogin().equals(ttc1.getMessage().getLogin());
        assert tt1.getMessage().getPhone().equals(ttc1.getMessage().getPhone());


        final GetLegalClientProfileRsType tt2 = new GetLegalClientProfileRsType();
        tt2.setMethod("test");
        tt2.setRqUID("test");
        tt2.setRqTm("test");
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.MessageType messtype2 = new ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.MessageType();
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.ListOfAccountType listOfAccountType = new ListOfAccountType();
        listOfAccountType.withAccountInfo(new AccountInfoType[]{getTypeSrvGetLegalClientProfile("test")});
        messtype2.setListOfAccount(listOfAccountType);
        tt2.setMessage(messtype2);
        String ttS2 = jaxbCachingManager.marshall(tt2);
        GetLegalClientProfileRsType ttc2 = jaxbCachingManager.unmarshall(ttS2, GetLegalClientProfileRsType.class);
        assert tt2.getMethod().equals(ttc2.getMethod());
        assert tt2.getRqUID().equals(ttc2.getRqUID());
        assert tt2.getRqTm().equals(ttc2.getRqTm());
        assert tt2.getMessage().getListOfAccount().getAccountInfo().get(0).getId().equals(ttc2.getMessage().getListOfAccount().getAccountInfo().get(0).getId());


        Map temp = (Map) TestUtils.getPrivateFieldValue("cache", jaxbCachingManager);
        assert temp.size() == 3;
    }
}