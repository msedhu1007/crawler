package pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.PageFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static utils.PlaywrightFactory.getBrowserContext;

public class HomePage extends PageFactory {

    private String jobTable = "div#table";
    private String jobLink = "(//span[text()='Open'])/..";
    private String companyName = "div.break-word";
    private String totalCount = "div.selectionCount";
    private String rowNumber = "div.rowNumber div.numberText";
    private String openLink = "span.truncate.noevents:has-text('Open')";
    private String tabJobsPage = "//div[text()='Jobs Page']";
    private String tabOpenRoles = "//div[text()='Open Roles Found by Function']";
    String lastRow = "//div[@class='numberText'][text()='%s']";

    private String closeLinkedInPopup = "(//button[@aria-label='Dismiss'])[2]";
    private String fullScreen = "a:has-text('full screen')";
    private String btnSeeMoreJobs = "a:has-text('See all jobs')";

    private String keywordOne = "SDET";
    private String keywordTwo = "Engineer in Test";
    private String keywordThree = "QA";
    private String keywordFour = "Test Automation";
    private String keywordFive = "Quality Assurance";
    private String keywordSix = "Software Development Engineer in Test";
    private String keywordSeven = "Automation Test";
    private String keywordEight = "Software Developer in Test";


    public void waitForPageToOpen() {
        waitForNetworkIdle(getPage());
    }


    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

    public void checkJobs() throws IOException {
        Page newPage;
        getFrame(getPage(), "iframe").locator(jobTable).waitFor();

        Page secondPage = openNewTab(getPage(), fullScreen);
        secondPage.waitForLoadState();
        List<String> keywords = Arrays.asList(keywordOne, keywordTwo, keywordThree, keywordFour, keywordFive, keywordSix, keywordSeven, keywordEight);
        print(" * <%s> ", "Crawling the web to search jobs for keywords :");
        keywords.forEach(i -> System.out.print(i + " , "));

        List<ElementHandle> locators = secondPage.querySelectorAll(jobLink);
        LinkedHashSet<String> links = locators.stream().map(i -> i.getAttribute("href")).collect(Collectors.toCollection(LinkedHashSet::new));

        String value = String.format(lastRow, "1203");
        while (!secondPage.locator(value).isVisible()) {
            links.addAll(addLinks(secondPage));
        }
        System.out.println("Total number of sites searched: " + links.size());


        for (String link : links) {


            if (link != null && !link.isEmpty() && !link.trim().equals("") && !link.contains("linkedin")) {
                newPage = getBrowserContext().newPage();
                loadURL(newPage, link);

                try {
                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordOne).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));

                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordTwo).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));

                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordThree).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));

                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordFour).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));

                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordFive).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));

                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordSix).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));

                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordSeven).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));

                    newPage.frames().stream()
                            .filter(i -> i.getByText(keywordEight).count() > 0)
                            .forEach(j -> print(" * %s <%s> (%s)", "Job found :", j.url(), j.title()));
                } catch (Exception e) {
                }
                newPage.close();
            }
        }

    }

    private LinkedHashSet<String> addLinks(Page page) {
        click(page, tabJobsPage);
        page.keyboard().press("PageDown");
        List<ElementHandle> locators = page.querySelectorAll(jobLink);
        return locators.stream().map(i -> i.getAttribute("href")).collect(Collectors.toCollection(LinkedHashSet::new));
    }


    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }


    public void getJobsWithKeywords(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .header("Accept", "text/html")
                    .header("Accept-Encoding", "gzip,deflate")
                    .header("Accept-Language", "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
                    .header("Connection", "keep-alive")
                    .ignoreContentType(true)
                    .timeout(5000).get();
            Elements allElements =
                    document.getAllElements();
            for (Element element : allElements) {
                String automationJobs = element.select("h3:contains('automation')").text();
                String autoJobs = element.select("h5:contains('automation')").text();
                String SDETJobs = element.select("h5:contains('SDET')").text();
                String SETJobs = element.select("h5:contains('SDET')").text();
                System.out.println(autoJobs);
                System.out.println(automationJobs);
                System.out.println(SDETJobs);
                System.out.println(SETJobs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isFound(Page page, String keyword) {
        if (page.locator(keyword).count() > 0) {
            System.out.println("Opening found :");
            return true;
        }
        return false;
    }


}
