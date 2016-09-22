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
package ui;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.rules.ExternalResource;

import java.nio.file.Path;
import java.nio.file.Paths;


public class PhantomJSResource extends ExternalResource {

    private PhantomJSResource() {}

    private static PhantomJSResource singleton = new PhantomJSResource();
    public static PhantomJSResource getInstance(){
        return singleton;
    }
    
    @Override
    protected void before() throws Throwable {
        Path phantomJS = Paths.get(System.getProperty("user.home"), "phantomJS");
        PhantomJSInstaller.ensureInstalled(phantomJS.toString());
    }

    @Override
    protected void after() {
        WebDriverRunner.getWebDriver().quit();
    }
}
