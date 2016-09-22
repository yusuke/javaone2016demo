package ui;
/*
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
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

public class PhantomJSExample {
    public static void main(String... args) throws InterruptedException, IOException {
        PhantomJSInstaller.ensureInstalled();

        Navigator navigator = new Navigator();
        navigator.open("https://www.google.com/");
        Thread.sleep(1000);
        $("[name='q']").click();
        $("[name='q']").sendKeys("JavaOne 2016");
        $("[name='q']").pressEnter();
        System.out.println($("h3").getText());
    }
}
