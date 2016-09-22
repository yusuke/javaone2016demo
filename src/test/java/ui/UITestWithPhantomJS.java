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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.assertEquals;

public class UITestWithPhantomJS {
    private static Server server = null;
    @ClassRule
    public static PhantomJSResource PhantomJS = PhantomJSResource.getInstance();

    @BeforeClass
    public static void beforeClass() throws Exception {
        int port = 8080;
        if (!isPortInUse(port)) {
            // nothing is listening on the port
            WebAppContext context = new WebAppContext();
            File webapp = new File("src/main/webapp/");
            context.setWar(webapp.getAbsolutePath());
            context.setContextPath("/");
            server = new Server(port);
            server.setHandler(context);
            server.start();
        }
    }

    @AfterClass
    public static void afterClass() throws Exception {
        if (server != null) {
            server.stop();
        }
        try {
            // ensure phantomjs to quit
            WebDriverRunner.getWebDriver().quit();
        } catch (IllegalStateException ignored) {
        }

    }

    private static boolean isPortInUse(int port) {
        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(port), 200);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Test
    public void helloWorldIsHelloWorld() throws Exception {
        Navigator navigator = new Navigator();
        navigator.open("http://localhost:8080");
        String value = $("body").getText();

        assertEquals("hello world", value);
    }

}
