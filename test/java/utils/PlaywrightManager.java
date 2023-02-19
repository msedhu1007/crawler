package utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightManager {

    public static ThreadLocal<Page> pg = new ThreadLocal<Page>();
    public static ThreadLocal<Playwright> playwright = new ThreadLocal<Playwright>();
    public static ThreadLocal<Browser> browser = new ThreadLocal<Browser>();
    public static ThreadLocal<BrowserContext> browserContext = new ThreadLocal<BrowserContext>();

    public synchronized static Page getPage() {
        return pg.get();
    }

    public synchronized static void setPage(Page dr) {
        pg.set(dr);
    }

    public synchronized static void setPlaywright(Playwright dr) {
        playwright.set(dr);
    }

    public synchronized static Playwright getPlaywright() {
        return playwright.get();
    }

    public synchronized static Browser getBrowser() {
        return browser.get();
    }

    public synchronized static void setBrowser(Browser dr) {
        browser.set(dr);
    }

    public synchronized static BrowserContext getBrowserContext() {
        return browserContext.get();
    }

    public synchronized static void setBrowserContext(BrowserContext dr) {
        browserContext.set(dr);
    }

}

