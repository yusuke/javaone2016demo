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

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class PhantomJSInstaller {
    private static final String PHANTOM_JS_VERSION = "2.1.1";

    /**
     * ensure PhantomJS is installed on the specified directory
     *
     * @param installRoot directory to be installed
     * @return path to the PhantomJS binary
     */
    static String ensureInstalled(String installRoot) throws IOException {
        Path installRootPath = Paths.get(installRoot);
        String fileName;
        String dirName = "phantomjs-" + PHANTOM_JS_VERSION;
        String os = System.getProperty("os.name").toLowerCase();
        String binName = "bin/phantomjs";
        if (os.contains("nux")) {
            dirName += "-linux-" + ("32".equals(System.getProperty("sun.arch.data.model")) ? "i686" : "x86_64");
            fileName = dirName + ".tar.bz2";
        } else{
            if (os.startsWith("windows")) {
                dirName += "-windows";
                binName += ".exe";
            } else if (os.contains("mac") || os.contains("darwin")) {
                dirName += "-macosx";
            } else {
                throw new IllegalStateException("Unexpected os:" + os);
            }
            fileName = dirName + ".zip";
        }

        Path filePath = installRootPath.resolve(fileName);
        Path destDir = installRootPath.resolve(dirName);
        Path bin = destDir.resolve(binName);
        // download PhantomJS
        if (!Files.exists(bin)) {
            Files.createDirectories(installRootPath);
            URL url = new URL("https://bitbucket.org/ariya/phantomjs/downloads/" + fileName);
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                int code = conn.getResponseCode();
                if (code == 200) {
                    Files.copy(conn.getInputStream(), filePath);
                } else {
                    throw new IOException("URL[" + url + "] returns code [" + code + "].");
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            if (filePath.getFileName().toString().endsWith("tar.bz2")) {
                unTar(filePath.toFile().getAbsolutePath(), installRootPath);
            } else {
                unZip(installRootPath, filePath);
            }
            //noinspection ResultOfMethodCallIgnored
            bin.toFile().setExecutable(true);
        }
        String phantomjs = bin.toAbsolutePath().toFile().getAbsolutePath();
        System.setProperty("phantomjs.binary.path", phantomjs);
        System.setProperty("java.util.logging.config.class", JulConfig.class.getCanonicalName());
        System.setProperty("browser", "phantomjs");

        return phantomjs;
    }

    private static void unZip(Path root, Path archiveFile) throws IOException {
        ZipFile zip = new ZipFile(archiveFile.toFile());
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                Files.createDirectories(root.resolve(entry.getName()));
            } else {
                try (InputStream is = new BufferedInputStream(zip.getInputStream(entry))) {
                    Files.copy(is, root.resolve(entry.getName()));
                }
            }

        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void unTar(String s, Path root) throws IOException {
        File tarFile = File.createTempFile("phantomjs", "tar");
        try (BZip2CompressorInputStream in = new BZip2CompressorInputStream(new FileInputStream(s));
             FileOutputStream out = new FileOutputStream(tarFile)) {
            IOUtils.copy(in, out);
        }

        File outputDir = root.toFile();
        outputDir.mkdirs();
        try (ArchiveInputStream is = new ArchiveStreamFactory()
                .createArchiveInputStream("tar", new FileInputStream(tarFile))) {
            ArchiveEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                File out = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    out.mkdirs();
                } else {
                    try (OutputStream fos = new FileOutputStream(out)) {
                        IOUtils.copy(is, fos);
                    }
                }
            }
        } catch (ArchiveException e) {
            throw new IOException(e);
        }
        tarFile.delete();
    }
}
