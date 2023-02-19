package utils;

import com.microsoft.playwright.*;
import com.microsoft.playwright.Browser.NewContextOptions;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static configuration.ConfigurationManager.configuration;


public class PlaywrightFactory {

    // Thread Local
    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    protected Page page = null;
    protected BrowserContext context;

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getBrowserContext() {
        return tlBrowserContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    private static Page initBrowser() {
        String browserName = configuration().browser();
        Path pathToVideos;

        System.out.println("<<======= USING BROWSER: " + browserName + " =======>>");
        tlPlaywright.set(Playwright.create());

        switch (browserName.toLowerCase()) {
            case "chromium":
                tlBrowser.set(getPlaywright().chromium()
                        .launch(new BrowserType.LaunchOptions().setHeadless(configuration().headless()).setArgs(Arrays.asList("--use-angle=default", "--disable-web-security"))));
                break;

            case "chrome":
                tlBrowser.set(getPlaywright().chromium()
                        .launch(new BrowserType.LaunchOptions().setChannel("chrome")
                                .setHeadless(configuration().headless()).setArgs(Arrays.asList("--disable-web-security", "--use-angle=default"))));
                break;

            case "msedge":
                tlBrowser.set(getPlaywright().chromium()
                        .launch(new BrowserType.LaunchOptions().setChannel("msedge")
                                .setHeadless(configuration().headless()).setArgs(Arrays.asList("--use-angle=default", "--disable-web-security"))));
                break;

            case "firefox":
                tlBrowser.set(getPlaywright().firefox()
                        .launch(new BrowserType.LaunchOptions().setHeadless(configuration().headless()).setArgs(Arrays.asList("--disable-web-security","--use-angle=default"))));
                break;

            case "safari":
                tlBrowser.set(getPlaywright().webkit()
                        .launch(new BrowserType.LaunchOptions().setHeadless(configuration().headless()).setArgs(Collections.singletonList("--use-angle=default"))));
                break;

            default:
                System.out.println("Chromium browser is selected by default");
                tlBrowser.set(getPlaywright().chromium()
                        .launch(new BrowserType.LaunchOptions().setHeadless(configuration().headless()).setArgs(Collections.singletonList("--use-angle=default"))));
                break;
        }

        PlaywrightFactory playwrightFactory = new PlaywrightFactory();
        tlBrowserContext.set(getBrowser().newContext());

        int width = getScreenSize()[0];
        int height = getScreenSize()[1];

        NewContextOptions options = new NewContextOptions()
                .setViewportSize(width, height);

        if (configuration().captureVideo()) {
            pathToVideos = Paths.get(System.getProperty("user.dir") + configuration().baseVideoPath());
            options.setRecordVideoDir(pathToVideos);
        }

        tlBrowserContext.set(getBrowser().newContext(options));

        if (configuration().enableTracing()) {
            tlBrowserContext.get().tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false));
        }

        tlPage.set(getBrowserContext().newPage());
        playwrightFactory.monitorNetwork();
        getPage().setDefaultNavigationTimeout(45000);
        getPage().setDefaultTimeout(45000);
        getPage().navigate(configuration().baseUrl().trim());
        return getPage();
    }

    public static void removeThreadLocal() {
        // Stop tracing and export it into a zip archive.
        if (configuration().enableTracing()) {
            tlBrowserContext.get()
                    .tracing()
                    .stop(new Tracing.StopOptions().setPath(Paths.get(System.getProperty("user.dir") + configuration().baseTraceViewerPath())));
        }
        getPage().context()
                .browser()
                .close();
        tlPage.remove();
        tlBrowserContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }

    public static int[] getScreenSize() {
        int[] size = new int[2];
        Dimension screensize = Toolkit.getDefaultToolkit()
                .getScreenSize();
        size[0] = (int) screensize.getWidth();
        size[1] = (int) screensize.getHeight();
        return size;
    }


    public Page initialize() {
        page = initBrowser();
        return page;
    }


    public void monitorNetwork() {
        /*
         * to get request from the network ,need to use onRequest()
         * to get response from the network ,need to use onResponse()
         */
        getPage().onRequest(handler -> {
            String method = handler.method();

        /*    if (method.equalsIgnoreCase("post") || method.equalsIgnoreCase("get")) {
                System.out.println(method);
                String postdata = handler.postData();
                System.out.println(postdata);
                System.out.println(handler.url() + "Navigation Request: " + handler.isNavigationRequest());
            }*/
        });

        getPage().onResponse(handler -> {
            int statuscode = handler.status();
          /*  System.out.println(handler.url());
            System.out.println(handler.request());
            System.out.println("Response :" + statuscode);*/
            if (statuscode >= 400) {
                System.out.println(handler.url());
                System.out.println(handler.request());
                System.out.println("Response :" + statuscode);
            }
        });
    }

}
