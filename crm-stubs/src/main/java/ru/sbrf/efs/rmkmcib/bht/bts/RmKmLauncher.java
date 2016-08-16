package ru.sbrf.efs.rmkmcib.bht.bts;

import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.loader.jar.JarFile;

/**
 * Created by sbt-manayev-iye on 09.08.2016.
 */
public class RmKmLauncher extends JarLauncher {
    private static ClassLoader classLoader = null;
    private static RmKmLauncher bootstrap = null;

    protected void launch(String[] args, String mainClass, ClassLoader classLoader, boolean wait)
            throws Exception {
        // ToDo после обновления версии спринг бут до 1.4.0 стали проявляться проблемы несовместимости типов
        // закомментировано до тех пор, пока не будет исправлено
//        Runnable runner = createMainMethodRunner(mainClass, args, classLoader);
//        Thread runnerThread = new Thread(runner);
//        runnerThread.setContextClassLoader(classLoader);
//        runnerThread.setName(Thread.currentThread().getName());
//        runnerThread.start();
//        if (wait == true) {
//            runnerThread.join();
//        }
    }

    public static void start (String []args) {
        bootstrap = new RmKmLauncher ();
        try {
            JarFile.registerUrlProtocolHandler();
            classLoader = bootstrap.createClassLoader(bootstrap.getClassPathArchives());
            bootstrap.launch(args, bootstrap.getMainClass(), classLoader, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void stop (String []args) {
        try {
            if (bootstrap != null) {
                bootstrap.launch(args, bootstrap.getMainClass(), classLoader, true);
                bootstrap = null;
                classLoader = null;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        String mode = args != null && args.length > 0 ? args[0] : null;
        if ("start".equals(mode)) {
            RmKmLauncher.start(args);
        }
        else if ("stop".equals(mode)) {
            RmKmLauncher.stop(args);
        }
    }
}
