package utils;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static utils.PlaywrightFactory.removeThreadLocal;

public class BaseTest {

    protected Page page;
    PlaywrightFactory playwrightFactory = new PlaywrightFactory();

    @BeforeEach
    public void setup() {
        Page page = playwrightFactory.initialize();
        PlaywrightManager.setPage(page);
    }

    @AfterEach
    public void tearDown() {
        removeThreadLocal();
    }

}
