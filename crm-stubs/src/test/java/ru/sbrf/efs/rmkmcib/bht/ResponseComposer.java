package ru.sbrf.efs.rmkmcib.bht;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientList.dto.rs.*;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.GetLegalClientProfileRsType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.ListOfAccountType;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbCachingManager;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;

import javax.xml.bind.JAXBException;

/**
 * Created by sbt-manayev-iye on 08.08.2016.
 */
public class ResponseComposer {

    private static final Logger log = LoggerFactory.getLogger(ResponseComposer.class);

    JaxbManager man = new JaxbCachingManager();

    public AccountInfoType getType(String val) {
        AccountInfoType type1 = new AccountInfoType();
        type1.setId(val);
        type1.setMDMId(val);
        type1.setOGRN(val);
        type1.setOPF(val);
        type1.setSBRFKPP(val);
        type1.setSBRFINN(val);
        type1.setName(val);
        return type1;
    }

    public  ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType getType2(String val){
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType type1 = new ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType();
        type1.setId(val);
        type1.setMDMId(val);
        type1.setOGRN(val);
        type1.setVKO(val);
        type1.setVKOPhone(val);
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
    public void composeGetLegalClientProfileRs() throws JAXBException, ClassNotFoundException {
        GetLegalClientProfileRsType resp = new GetLegalClientProfileRsType();
        resp.setMethod("IANUAMRMGetClientInfoResponse");
        resp.setRqTm("2016-12-25T23:23:23Z");
        resp.setRqUID("00000000000000000000000000000012");
        resp.setSPName("urn:sbrfsystems:99-crmorg");
        resp.setSystemId("urn:sbrfsystems:99-ufs");
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.StatusType status = new ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.StatusType();
        status.setServerStatusCode("ServerStatusCode");
        status.setSeverity("WARN");
        status.setStatusCode("0");
        status.setStatusDesc("StatusDesc");
        resp.setStatus(status);

        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.MessageType message = new ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.MessageType();
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.ListOfAccountType list = new ListOfAccountType();

        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType type1 = getType2("test");
        ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType type2 = getType2("test2");

        list.withAccountInfo(new ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rs.AccountInfoType[]{type1,type2});
        message.setListOfAccount(list);
        resp.setMessage(message);
        String data = man.marshall(resp);
        log.debug(data);
    }

    private StatusType getStatus() {
        StatusType status = new StatusType();
        status.setServerStatusCode("ServerStatusCode");
        status.setSeverity("WARN");
        status.setStatusCode("0");
        status.setStatusDesc("StatusDesc");
        return status;
    }

    @Test
    public void composeGetLegalClientListRsType() throws JAXBException, ClassNotFoundException {
        GetLegalClientListRsType resp = new GetLegalClientListRsType();
        resp.setMethod("IANUAGetClientsResponse");
        resp.setRqTm("2016-12-25T23:23:23Z");
        resp.setRqUID("00000000000000000000000000000012");
        resp.setSPName("urn:sbrfsystems:99-crmorg");
        resp.setSystemId("urn:sbrfsystems:99-ufs");


        resp.setStatus(getStatus());

        MessageType messageType = new MessageType();

        ListOfAccountInfoType list = new ListOfAccountInfoType();

        AccountInfoType type1 = getType("test1");
        AccountInfoType type2 = getType("test2");


        list.withAccountInfo(new AccountInfoType[]{type1, type2});

        messageType.setListOfAccountInfo(list);

        resp.setMessage(messageType);

        String data = man.marshall(resp);
        log.debug(data);
    }


}
