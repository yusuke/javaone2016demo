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
import com.codeborne.selenide.impl.Navigator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.Assert.assertEquals;

public class UITestWithPhantomJS {
    @ClassRule
    public static PhantomJSResource PhantomJS = PhantomJSResource.getInstance();

    @BeforeClass
    public static void beforeClass() throws Exception {
        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(8080), 200);
            // the server is running
        } catch (IOException e) {
            // nothing is listening on the port
            startServer();
        }
    }

    private static void startServer() {
        App.main();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        try {
            // ensure phantomjs to quit
            WebDriverRunner.getWebDriver().quit();
        } catch (Exception ignored) {
        }

    }

    @Test
    public void helloWorldIsHelloWorld() throws Exception {
        Navigator navigator = new Navigator();
        navigator.open("http://localhost:8080");

        assertEquals("hello world", $("body").getText());
    }

}
