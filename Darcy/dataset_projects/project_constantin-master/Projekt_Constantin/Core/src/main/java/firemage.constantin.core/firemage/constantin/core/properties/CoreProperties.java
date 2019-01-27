package firemage.constantin.core.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class CoreProperties {

	private static CoreProperties INSTANCE;
	private static final String PROPERTY_FILE_PATH = new String("config/core.con");
	private static Properties properties = new Properties();
	
	//private static final Logger LOG = LogManager.
	
	public static synchronized CoreProperties getInstance() throws IOException {
		if(INSTANCE == null)
			INSTANCE = new CoreProperties();
		
		return INSTANCE;
	}
	
	private CoreProperties() throws IOException {
		InputStream in = null;
		
		try {
			in = new FileInputStream(PROPERTY_FILE_PATH);
			properties.load(in);
		} finally {
			try {
				if(in != null)
					in.close();
			} catch(IOException ex) {/*ignore*/}
		}
	}
	
	public synchronized String getProperty(final CorePropertyName key) {
		return properties.getProperty(key.propertyKey);
	}

	public static String getPropertyFilePath() {
		return new File(PROPERTY_FILE_PATH).getAbsolutePath();
	}
}
