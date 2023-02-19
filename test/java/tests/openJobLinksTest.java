package tests;

import org.junit.jupiter.api.Test;
import pages.HomePage;
import utils.BaseTest;

import java.io.IOException;

public class openJobLinksTest extends BaseTest {

    HomePage homePage = new HomePage();

    @Test
    public void scanJobLinks() throws IOException {
        homePage.waitForPageToOpen();
        homePage.checkJobs();
    }

}
