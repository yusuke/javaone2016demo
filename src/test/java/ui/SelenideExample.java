package ui;/*
 * Copyright 2016 Yusuke Yamamoto
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.codeborne.selenide.impl.Navigator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.Assert.assertEquals;

public class SelenideExample {
    public static void main(String... args) throws InterruptedException {
        Navigator navigator = new Navigator();
        navigator.open("https://www.google.com/");
        $(".gsfi").click();
        $(".gsfi").sendKeys("JavaOne 2016");
        $("[name='btnK']").click();
        System.out.println($("h3").getText());
    }
}
