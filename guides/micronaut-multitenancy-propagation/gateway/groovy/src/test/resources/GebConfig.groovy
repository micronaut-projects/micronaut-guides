import org.openqa.selenium.htmlunit.HtmlUnitDriver

// default to use htmlunit
driver = {
    HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver()
    htmlUnitDriver.javascriptEnabled = true
    htmlUnitDriver
}

environments {


    htmlUnit {
        driver = {
            HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver()
            htmlUnitDriver.javascriptEnabled = true
            htmlUnitDriver
        }
    }
}