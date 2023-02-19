package utils;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import org.apache.logging.log4j.Level;


import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static configuration.ConfigurationManager.configuration;
import static org.awaitility.Awaitility.await;
import static utils.PlaywrightFactory.getBrowser;
import static utils.PlaywrightFactory.getBrowserContext;

public class PageFactory {
    static Logger logger = Logger.getLogger(String.valueOf(PageFactory.class));
    public String alert = "div[role='alert'] div";


    public Response waitForResponse(Page page, String url, Predicate<Response> condition, Runnable trigger) throws Exception {
        Response response = null;
        while (!condition.test(response)) {
            response = page.waitForResponse(url, trigger);
        }
        return response;
    }

    public Page getPage() {
        return PlaywrightManager.getPage();
    }

    public void click(Page page, String locator) {
        try {
            page.click(locator);
        } catch (Exception e) {
            reportInfo("Click operation failed on the locator: " + locator);
            throw new RuntimeException("Click operation failed on the locator: " + locator);
        }
    }

    public void forceClick(Page page, String locator) {
        try {
            getLocator(page, locator).click(new Locator.ClickOptions().setForce(true));
            reportInfo("Force clicked on the element: " + locator);
        } catch (Exception e) {
            reportInfo("Click operation failed on the locator: " + locator);
            throw new RuntimeException("Click operation failed on the locator: " + locator);
        }
    }

    public void dispatchClickEvent(Page page, String locator) {
        try {
            getLocator(page, locator).dispatchEvent("click");
            reportInfo("Disaptched Click event on the element: " + locator);

        } catch (Exception e) {
            reportInfo("Click operation failed on the locator: " + locator);
            throw new RuntimeException("Click operation failed on the locator: " + locator);
        }
    }

    public void softClick(Page page, String locator) {
        try {
            page.evaluate("l => document.querySelector(l).click()", locator);
            reportInfo("Click performed on locator: " + locator);
        } catch (Exception e) {
            reportInfo("Soft click operation failed on: " + locator);
        }
    }


    public void selectList(Page page, String locator) {
        click(page, locator);

    }

    public void setText(Page page, String locator, String value) {
        try {
            getLocator(page, locator).clear();
            page.locator(locator)
                    .fill(value);
            reportInfo("Entered text on the element: " + locator);
        } catch (Exception e) {
            reportInfo("Enter Text failed on the locator: " + locator);
            throw new RuntimeException("Enter Text failed on the locator: " + locator);
        }
    }

    public void press(Page page, String locator, String value) {
        try {
            getLocator(page, locator).press(value);
            reportInfo("Send Keys on the element: " + locator);
        } catch (Exception e) {
            reportInfo("Send keys failed on the locator: " + locator);
            throw new RuntimeException("Send keys failed on the locator: " + locator);
        }
    }

    public void loadUrl(Page page, String url) {
        try {
            page.navigate(url);
            reportInfo("Page loaded with the : " + url);
        } catch (Exception e) {
            reportInfo("Load URL failed: " + url);
            throw new RuntimeException("Load URL failed: " + url);
        }
    }

    public void waitFor(Page page, String locator) {
        try {
            page.locator(locator)
                    .waitFor();
        } catch (Exception e) {
            reportInfo("Wait for element failed: " + locator);
            throw new RuntimeException("Wait for element failed: " + locator);
        }
    }

    public void waitForSelector(Page page, String locator) {
        try {
            page.waitForSelector(locator);
        } catch (Exception e) {
            reportInfo("Wait for element failed: " + locator);
            throw new RuntimeException("Wait for element failed: " + locator);
        }
    }

    public String getText(Page page, String locator) {
        try {
            return page.locator(locator)
                    .textContent();
        } catch (Exception e) {
            reportInfo("Get text on the element failed :" + locator);
            return null;
        }
    }

    public String getAttribute(Page page, String locator, String attribute) {
        try {
            return page.locator(locator)
                    .getAttribute(attribute);
        } catch (Exception e) {
            reportInfo("Get attribute on the element failed :" + locator);
            return null;
        }
    }

    public String getInputValue(Page page, String locator) {
        return page.locator(locator)
                .inputValue();
    }

    public boolean isEnabled(Page page, String locator) {
        return page.locator(locator)
                .isEnabled();
    }

    public boolean isDisabled(Page page, String locator) {
        return page.locator(locator)
                .isDisabled();
    }

    public boolean isEditable(Page page, String locator) {
        return page.locator(locator)
                .isEditable();
    }

    public boolean isVisible(Page page, String locator) {
        return page.locator(locator)
                .isVisible();
    }

    public boolean isHidden(Page page, String locator) {
        return page.locator(locator)
                .isHidden();
    }

    public boolean isChecked(Page page, String locator) {
        return page.locator(locator)
                .getAttribute("aria-checked")
                .contains("true");
    }

    public void scrollIntoView(Page page, String locator) {
        page.locator(locator)
                .scrollIntoViewIfNeeded();
    }

    public FrameLocator getFrame(Page page, String frame) {
        return page.frameLocator(frame);
    }

    public Locator getLocator(Page page, String locator) {
        return page.locator(locator);
    }

    public int getCount(Page page, String locator) {
        return page.locator(locator)
                .count();
    }

    public void waitUntilElementIsVisible(Page page, String locator, int time) {
        try {
            await().atMost(Duration.ofSeconds(time))
                    .ignoreExceptions()
                    .until(getLocator(page, locator)::isVisible);
        } catch (Exception e) {
            reportInfo("Timed out waiting for the element to be visible: " + locator);
        }
    }

    public void waitUntilElementPresent(Page page, String locator, int time) {
        try {
            await().atMost(Duration.ofSeconds(time))
                    .ignoreExceptions()
                    .until(() -> getCount(page, locator) > 0);
        } catch (Exception e) {
            reportInfo("Timed out waiting for the element to be present: " + locator);

        }
    }

    public void waitUntilNotDisplayed(Page page, String locator, int time) {
        try {
            await().atMost(Duration.ofSeconds(time))
                    .ignoreExceptions()
                    .until(getLocator(page, locator)::isHidden);
        } catch (Exception e) {
            reportInfo("Timed out waiting for the element to be hidden: " + locator);
        }
    }

    public void waitUntil(String locator, Callable<Boolean> condition, Duration duration) {
        await().atMost(Duration.ofSeconds(30))
                .ignoreExceptions()
                .until(condition);
    }

    // can be used like this
    // Response response = WaitHelper.waitForResponse(page, "https://example.com/resource",
    //    () -> page.getByText("trigger response").click());
    public Response waitForResponse(Page page, String url, Runnable trigger) throws Exception {
        Response response = null;
        while (response == null) {
            //    trigger.run();
            response = page.waitForResponse(url, trigger);
        }
        return response;
    }

    public void waitForElement(Page page, AriaRole role) {
        try {
            page.getByRole(role).waitFor();
        } catch (Exception e) {
            reportInfo("Timed out waiting for the element with the AriaRole: " + role);
        }
    }


    public boolean isElementVisible(Page page, String locator, int time) {
        try {
            await().atMost(Duration.ofSeconds(time))
                    .until(getLocator(page, locator)::isVisible);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void back(Page page) {
        page.goBack();
    }

    public void forward(Page page) {
        page.goForward();
    }

    public void refresh(Page page) {
        page.reload();
    }

    public List<Page> getAllBrowserContext() {
        return PlaywrightManager.getBrowserContext()
                .pages();
    }

    public Page openNewWindow() {
        BrowserContext browserContext = getBrowser().newContext();
        Page pageTwo = browserContext.newPage();
        return pageTwo;
    }

    public Page openNewTab() {
        Page newPage = PlaywrightManager.getBrowserContext()
                .waitForPage(() -> {
                    getPage().click("a[target='_blank']"); // Opens a new blank tab
                });
        newPage.waitForLoadState();
        return newPage;
    }

    public void switchTabs() {
        PlaywrightManager.getBrowserContext()
                .onPage(page -> {
                    page.waitForLoadState();
                    System.out.println(page.title());
                });
    }

    public void waitForNetworkIdle(Page page) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void waitForLoadState(Page page) {
        page.waitForLoadState(LoadState.LOAD);
    }

    public void waitForDOMToLoad(Page page) {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public String getUrl(Page page) {
        return page.url();
    }


    public String takeScreenshot(String testName) {
        String path = System.getProperty("user.dir") + configuration().baseScreenshotPath() + testName + '_' + System.currentTimeMillis() + ".png";
        getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path))
                .setFullPage(true));
        reportInfo("Screenshot captured for the Test: " + testName);
        return path;
    }

    // Logger to report for each step
    public void reportInfo(String message) {
        logger.info(message);
    }

    /*Page newPage = getBrowserContext().waitForPage(() -> {
        click(getPage(), fullScreen);
    });*/

    public Page openNewTab(Page page, String locator) {
        return getBrowserContext().waitForPage(() -> {
            click(page, locator);
        });
    }

    public List<Page> getAllTabs() {
        return getBrowserContext().pages();
    }

    public void loadURL(Page page, String url) {
        try {
            page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        } catch (Exception e) {
        }

    }

}
