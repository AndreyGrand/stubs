package ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbrf.efs.integration.esb.fs.SrvGetLegalClientList;
import ru.sbrf.efs.integration.esb.fs.SrvGetLegalClientManagerTaskDetails;
import ru.sbrf.efs.integration.esb.fs.SrvGetLegalClientManagerTaskList;
import ru.sbrf.efs.integration.esb.fs.SrvPutLegalClientManagerTask;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientList.dto.rs.GetLegalClientListRsType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rs.GetLegalClientManagerTaskDetailsRsType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.GetlegalClientManagerTaskListRsType;
import ru.sbrf.efs.integration.esb.fs.srvPutLegalClientManagerTask.dto.rs.PutLegalClientManagerTaskRsType;
import ru.sbrf.efs.rmkmcib.bht.TestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CRMClassBoundsImplTest {

    private static final Logger log = LoggerFactory.getLogger(CRMClassBoundsImplTest.class);

    CRMClassBoundsImpl crmClassBoundsImpl = null;

    static final Map<String, String> methodMap = new HashMap<>();

    static {
        methodMap.put(GetlegalClientManagerTaskListRsType.class.getName(), SrvGetLegalClientManagerTaskList.class.getName());
        methodMap.put(GetLegalClientListRsType.class.getName(), SrvGetLegalClientList.class.getName());
        methodMap.put(GetLegalClientManagerTaskDetailsRsType.class.getName(), SrvGetLegalClientManagerTaskDetails.class.getName());
        methodMap.put(PutLegalClientManagerTaskRsType.class.getName(),SrvPutLegalClientManagerTask.class.getName());
    }

    @Before
    public void init() {
        crmClassBoundsImpl = new CRMClassBoundsImpl();
    }


    @Test
    public void testEmptyHeadersLoad() {
//
    }


    public void tryLoad(String dtoClass, String srvClass, CRMClassBoundsImpl instance) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class clzzDto = Class.forName(dtoClass);
        Class clzzSrv = Class.forName(srvClass);
        Method getVisibleNames = null;
        getVisibleNames = TestUtils.getPrivateMethod("getVisibleNames", instance);
        if (getVisibleNames != null) {
            Map<String, Map<String, String>> r = null;

            r = (Map<String, Map<String, String>>) getVisibleNames.invoke(instance);

            assert r.containsKey(srvClass.split("\\.")[srvClass.split("\\.").length - 1].toLowerCase());
        }
    }

    @Test
    public void loadClasses() throws ClassNotFoundException {
        try {
            Method loadVisibleNames = TestUtils.getPrivateMethod("loadVisibleNames", crmClassBoundsImpl);
            if (loadVisibleNames != null) {
                loadVisibleNames.invoke(crmClassBoundsImpl);
            }
            assert crmClassBoundsImpl.getKnownClasses() != null;
            assert crmClassBoundsImpl.getKnownClasses().size() != 0;
            int cc = 0;
            for (Map.Entry<String, String> ee : methodMap.entrySet()) {

                Class clzzDto = Class.forName(ee.getKey());
                Class clzzSrv = Class.forName(ee.getValue());
                cc++;
                assert crmClassBoundsImpl.getKnownClasses().size() >= cc;
            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            //ignore
        }
    }

    @Test
    public void testLoadVisibleNames() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        try {
            Method loadVisibleNames = TestUtils.getPrivateMethod("loadVisibleNames", crmClassBoundsImpl);
            if (loadVisibleNames != null)
                loadVisibleNames.invoke(crmClassBoundsImpl);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            assert false;
        }
        for (Map.Entry<String, String> ee : methodMap.entrySet()) {

            tryLoad(ee.getKey(), ee.getValue(), crmClassBoundsImpl);

        }
    }


}