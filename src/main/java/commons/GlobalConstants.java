package commons;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public class GlobalConstants {
	private static GlobalConstants globalInstance;

	private GlobalConstants() {
	}

	public static synchronized GlobalConstants getGlobalConstants() {
		if (globalInstance == null) {
			globalInstance = new GlobalConstants();
		}
		return globalInstance;
	}

	private final long shortTimeout = 5;
	private final long longTimeout = 30;
	private final String projectPath = System.getProperty("user.dir");
	private final String uploadFolderPath = projectPath + File.separator + "uploadFiles" + File.separator;
	private final String downloadFolderPath = projectPath + File.separator + "downloadFiles";
	private final String browserLogPath = projectPath + File.separator + "browserLogs" + File.separator;
	private final String browserStackUsername = "automationfc1";
	private final String browserStackKey = "HzcRC4Q1fzuQhRJYSkhz";
	private final String browserStackUrl = "https://" + browserStackUsername + ":" + browserStackKey + "@hub-cloud.browserstack.com/wd/hub";
	private final String saucelabUsername = "oauth-automationfc.com-628f3";
	private final String saucelabKey = "7b9a65b3-3521-48cc-9938-4c71e9e68404";
	private final String saucelabUrl = "https://" + saucelabUsername + ":" + saucelabKey + "@ondemand.us-west-1.saucelabs.com:443/wd/hub";
	private final String crossBrowserUsername = "dam@automationfc.com".replaceAll("@", "%40");
	private final String crossBrowserKey = "u87f334283d50903";
	private final String crossBrowserUrl = "http://" + crossBrowserUsername + ":" + crossBrowserKey + "@hub.crossbrowsertesting.com:80/wd/hub";
	private final String lambdaUsername = "automationfc.net";
	private final String lambdaKey = "yhFwU3pQ3bTOa0omWrr0w3DxHLju2XLEHjDWJwcDbxeIBjrg5N";
	private final String lambdaUrl = "https://" + lambdaUsername + ":" + lambdaKey + "@hub.lambdatest.com/wd/hub";
	private final String DATA_JSON_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "testdata" + File.separator + "data.json";
	private final List<String> ADMIN_CLOUNM =  Stream.of("","Username","User Role", "Employee Name", "Status").collect(Collectors.toList());
}
