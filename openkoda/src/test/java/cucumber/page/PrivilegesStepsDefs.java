/*
MIT License

Copyright (c) 2016-2024, Openkoda CDX Sp. z o.o. Sp. K. <openkoda.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package cucumber.page;

import com.openkoda.TestDataLoader;
import com.openkoda.core.flow.LoggingComponent;
import com.openkoda.model.Organization;
import com.openkoda.model.Privilege;
import com.openkoda.model.PrivilegeBase;
import com.openkoda.model.component.FrontendResource;
import cucumber.common.StepsBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrivilegesStepsDefs extends StepsBase implements LoggingComponent {

    AdminPage page = new AdminPage(driver);

    @Autowired
    protected TestDataLoader testDataLoader;

    @Given("Frontend resources {string}")
    public void frontendResource(String name) {
        testDataLoader.createFrontendResource(name, "test.js", " ", FrontendResource.Type.JS);
    }

    @When("Create user {string} {string} with {string} privilege {string} and organization {string} {string}")
    public void createUserWithPrivilege(String userName, String password, String area, String privilege, String organization, String privilegeName) {
        Set<PrivilegeBase> privileges = new HashSet<>();
        for (String p : privilege.split(";")) {
            privileges.add(Privilege.valueOf(p));
        }
        Organization org = testDataLoader.findOrganizationByName(organization);
        if (org == null) {
            org = testDataLoader.createOrganization(organization);
        }

        Tuple2[] tuple2 = new Tuple2[]{Tuples.of(privilegeName, org.getId())};
        if ("Organization".equals(area)) {
            testDataLoader.createOrganizationRole(privilegeName, privileges, true);
            testDataLoader.createUser(userName, password, userName + "@openkodatest.com", true, new String[]{"ROLE_USER"}, tuple2);

        } else {
            testDataLoader.createGlobalRole(privilegeName, privileges, true);
            testDataLoader.createUser(userName, password, userName + "@openkodatest.com", true, new String[]{"ROLE_USER"}, new Tuple2[]{});

        }

    }

    @And("I click {string} on the side menu")
    public void i_click_on_the_side_menu(String menuItemLabel) {
        page.waitFor(driver.findElement(By.partialLinkText(menuItemLabel))).click();
    }

    @And("I click {string} link")
    public void i_click(String linkName) {
        page.waitFor(driver.findElement(By.linkText(linkName))).click();
    }

    @Then("I should see test user email {string}")
    public void iShouldSeeTestUserEmail(String condition) {
        List<WebElement> we = driver.findElements(By.xpath("//div[@class=\"card-body\"]//div//div[contains(string(), \"E-mail\")]"));
        Assert.assertEquals(condition, we.size() > 0 ? "1" : "0");
    }

    @Then("I should see {string} forms")
    public void iShouldSeeForms(String forms) {
        List<WebElement> we;
        for (String form : forms.split(";")) {
            we = driver.findElements(By.xpath("//form[@class='" + form + "']"));
            Assert.assertTrue(we.size() > 0);
        }
    }

    @Then("I should see {string} pages {string}")
    public void iShouldSeePages(String pagePaths, String condition) {
        List<WebElement> we = driver.findElements(By.linkText(pagePaths));
        Assert.assertEquals(condition, we.size() > 0 ? "1" : "0");
    }

    @Then("I should see {string} button {string}")
    public void iShouldSeeLink(String text, String condition) {
        List<WebElement> we = driver.findElements(By.xpath("//button[contains(text(),'" + text + "')]"));
        Assert.assertEquals(condition, we.size() > 0 ? "1" : "0");
    }

    @And("I should find {string} in the side menu {string}")
    public void iShouldFindInTheSideMenu(String rowValues, String name) {
        List<String> values = List.of(StringUtils.split(rowValues + ";" + name, ';'));
        boolean condition = true;
        List<WebElement> menus = page.waitFor(driver.findElements(By.cssSelector("[class='collapse show']")));
        for (WebElement webElement : menus) {

            List<WebElement> rows = webElement.findElements(By.tagName("a"));
            for (WebElement we : rows) {
                if (!values.contains(we.getText())) {
                    condition = false;
                    break;
                }
            }
        }
        Assert.assertTrue("Elements found", condition);
    }

}
