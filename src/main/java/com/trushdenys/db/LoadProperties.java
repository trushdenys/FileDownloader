package com.trushdenys.db;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

public class LoadProperties {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    public static Properties loadProperties(){
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            File jarPath=new File(LoadProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath=jarPath.getParentFile().getAbsolutePath();
            inputStream = new FileInputStream(propertiesPath+"/MAIL.properties");
            /*inputStream = LoadProperties.class.getResourceAsStream("/MAIL.properties");*/
            prop.load(inputStream);
        } catch (IOException e) {
                logger.error("Can't open the properties file, exception: ", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                        logger.error("Can't close InputStream, exception: ", e);
                }
            }
        }
        return prop;
    }

}
