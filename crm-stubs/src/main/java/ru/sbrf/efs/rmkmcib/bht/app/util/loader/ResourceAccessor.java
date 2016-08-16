package ru.sbrf.efs.rmkmcib.bht.app.util.loader;

import org.springframework.context.ResourceLoaderAware;

/**
 * Created by sbt-manaev-ie on 28.07.2016.
 *
 */

/**
 * имплетация должна подгружать ресурсы доступные в ClassLoader
 */
public interface ResourceAccessor extends ResourceLoaderAware {

    String readResourceAsString(String resource);

}
