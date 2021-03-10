import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

ChromeOptions options = new ChromeOptions()
if (System.getProperty('download.folder')) {
    options.setExperimentalOption("prefs", [
        "profile.default_content_settings.popups": 0, // <1>
        "download.default_directory"             : System.getProperty('download.folder') // <2>
    ])
}

environments {
    chrome {
        driver = { new ChromeDriver(options) }
    }
    chromeHeadless {
        driver = {
            options.addArguments('headless')
            new ChromeDriver(options)
        }
    }
}
