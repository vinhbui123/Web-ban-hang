package vn.edu.hcmuaf.fit.Web_ban_hang.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class DBProperties {
    private static final Properties prop = new Properties();
    private static final Logger log = LoggerFactory.getLogger(DBProperties.class);

    static {
        try {
            prop.load(DBProperties.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
    public static String host() {
        return prop.get("db.host").toString();
    }
    public static int port() {
        try{
            return Integer.parseInt(prop.get("db.port").toString());
        }catch (NumberFormatException e){
            return 3306;
        }
    }
    public static String username() {
        return prop.get("db.username").toString();
    }
    public static String password() {
        return prop.get("db.password").toString();
    }
    public static String dbname() {
        return prop.get("db.dbname").toString();
    }
    public static String option() {
        return prop.get("db.option").toString();
    }
}
