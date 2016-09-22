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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

public class RunJetty {

    public static void main(String... args) throws Exception {
        int port = 8080;
        WebAppContext context = new WebAppContext();
        File webapp = new File("src/main/webapp/");
        context.setWar(webapp.getAbsolutePath());
        context.setContextPath("/");
        Server server = new Server(port);
        server.setHandler(context);
        server.start();
    }
}
