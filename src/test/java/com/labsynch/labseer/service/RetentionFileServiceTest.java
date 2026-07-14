package com.labsynch.labseer.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RetentionFileServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private HttpServer server;

    @After
    public void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    public void deleteFoldersDoesNotThrowRuntimeExceptionWhenFileDeleteFails() throws Exception {
        File root = temporaryFolder.newFolder("data-files");
        File experimentFolder = new File(root, "experiments/EXPT-1");
        assertTrue(experimentFolder.mkdirs());
        Files.writeString(new File(experimentFolder, "result.txt").toPath(), "retention-test");

        boolean permissionsChanged = experimentFolder.setWritable(false, false);
        assertTrue("Test setup could not make the experiment folder non-writable", permissionsChanged);

        RetentionFileService service = serviceWithFolderResponse(root, "{\"EXPT-1\":\"experiments/EXPT-1\"}");
        try {
            service.deleteFolders(List.of("EXPT-1"));
        } catch (RuntimeException e) {
            fail("Folder deletion failures should be caught by deleteFolders, not thrown as RuntimeException: " + e);
        } finally {
            experimentFolder.setWritable(true, false);
        }
    }

    private RetentionFileService serviceWithFolderResponse(File root, String responseJson) throws Exception {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/api/experiments/folders-for-codes", exchange -> {
            byte[] response = responseJson.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream responseBody = exchange.getResponseBody()) {
                responseBody.write(response);
            }
        });
        server.start();

        RetentionFileService service = new RetentionFileService();
        setField(service, "dataFilesRoot", root.getAbsolutePath());
        setField(service, "acasBaseUrl", "http://localhost:" + server.getAddress().getPort());
        return service;
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
