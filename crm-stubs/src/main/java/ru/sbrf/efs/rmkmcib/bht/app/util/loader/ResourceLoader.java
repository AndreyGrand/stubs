package ru.sbrf.efs.rmkmcib.bht.app.util.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by sbt-manaev-ie on 28.07.2016.
 *
 */
@Service
public class ResourceLoader implements ResourceAccessor {

    private static final Logger log = LoggerFactory.getLogger(ResourceLoader.class);

    private org.springframework.core.io.ResourceLoader loader;

    @Override
    public String readResourceAsString(String resource) {
        Resource temp = loader.getResource(resource);
        ByteArrayOutputStream res = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        try(InputStream is= temp.getInputStream()) {
            while ((length = is.read(buffer))!=-1){
                res.write(buffer,0,length);
            }
        } catch (IOException e) {
           log.error(e.getMessage(),e);
        }
        try {
            return res.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return res.toString();
        }
    }

    @Override
    public void setResourceLoader(org.springframework.core.io.ResourceLoader resourceLoader) {
        this.loader = resourceLoader;
    }

}
