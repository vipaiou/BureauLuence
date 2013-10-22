package bureau.lucence;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {



	private final String configurepath = "/config.properties";

	/* define the key of all the configuration */
	private final String KEY_DB_USERNAME = "dbusername";
	private final String KEY_DB_PASSWORD = "dbpassword";
	private final String KEY_DB_URL = "dburl";
	private final String KEY_DB_DRIVER = "dbdriver";
	private final String KEY_FILE_APTH="filepath";
    private final String KEY_LOCKFILE="lockfile";

	/* fill the value of the properties */
	private static String dbusername;
	private static String dbpassword;
	private static String dburl;
	private static String dbdriver;
	private static String filepath;
	private static String lockFile;
   
	static{
		new Config();
	}
	
	private Config(){
	    String path = System.getProperty("user.dir");
	    System.out.println("配置文件路径：" + path+configurepath);
		/* init the configuration file as input stream */
	    InputStream cfgfileis;
        try {
            cfgfileis = new BufferedInputStream(new FileInputStream(path+configurepath));
        } catch (FileNotFoundException e1) {
            //e1.printStackTrace();
            System.out.println("在jar包同级目录下没有找到配置文件，将使用jar包中的默认配置");
            cfgfileis = this.getClass().getResourceAsStream(configurepath);
        }
        /* init the input stream as properties*/
		Properties property = new Properties();
		try {
			property.load(cfgfileis);
			Config.dburl=property.getProperty(this.KEY_DB_URL);
			Config.dbusername=property.getProperty(this.KEY_DB_USERNAME);
			Config.dbpassword=property.getProperty(this.KEY_DB_PASSWORD);
			Config.filepath=property.getProperty(this.KEY_FILE_APTH);
			Config.lockFile = property.getProperty(this.KEY_LOCKFILE);
			Config.dbdriver = property.getProperty(this.KEY_DB_DRIVER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDbusername() {
		return dbusername;
	}

	public static void setDbusername(String dbusername) {
		Config.dbusername = dbusername;
	}

	public static String getDbpassword() {
		return dbpassword;
	}

	public static void setDbpassword(String dbpassword) {
		Config.dbpassword = dbpassword;
	}

	public static String getDburl() {
		return dburl;
	}

	public static void setDburl(String dburl) {
		Config.dburl = dburl;
	}

	public static String getDbdriver() {
		return dbdriver;
	}

	public static void setDbdriver(String dbdriver) {
		Config.dbdriver = dbdriver;
	}

	public static String getFilepath() {
		return filepath;
	}

	public static void setFilepath(String filepath) {
		Config.filepath = filepath;
	}

	public static String getLockFile() {
		return lockFile;
	}

	public static void setLockFile(String lockFile) {
		Config.lockFile = lockFile;
	}
}
