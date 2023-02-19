package configuration;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config/playwright.properties"})
public interface Configuration extends Config{

    /**
     * @return a string containing the browser name
     */
    @DefaultValue("chromium")
    String browser();

    /**
     * @return a string containing the base url of the Application
     */
    @Key("url")
    @DefaultValue("https://stillhiring.today/")
    String baseUrl();

    /**
     * @return a boolean containing the choice whether the browser will run in headless mode
     */
    @DefaultValue("true")
    @Key("headless")
    Boolean headless();

    /**
     * @return an integer containing the slow motion value
     */
    @Key("slow.motion")
    int slowMotion();


    /**
     * @return a boolean containing the choice whether the video has to be captured or not
     */
    @DefaultValue("false")
    Boolean captureVideo();


    /**
     * @return a boolean containing the choice whether the tracing is enabled or not
     */
    @DefaultValue("true")
    @Key("enableTracing")
    Boolean enableTracing();

    /**
     * @return a string containing the base path to store all the test reports
     */
    @Key("base.report.path")
    String baseReportPath();

    /**
     * @return a string containing the base path to store all the failed test screenshots
     */
    @Key("base.screenshot.path")
    String baseScreenshotPath();

    /**
     * @return a string containing the base path to store all the Vidoes captured
     */
    @Key("base.video.path")
    String baseVideoPath();

    /**
     * @return a string containing the base path to store the Traces captured
     */
    @Key("base.trace.path")
    String baseTraceViewerPath();
}
