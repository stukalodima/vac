package com.company.vac.web.controllers;


import com.company.vac.entity.Vacation;
import com.haulmont.addon.restapi.api.exception.RestAPIException;
import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.remoting.discovery.ServerSelector;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v3/files")
public class FileDownloadController {

    private static final Logger log = LoggerFactory.getLogger(FileDownloadController.class);

    // Using injection by name here, because an application project can define several instances
    // of ServerSelector type to work with different middleware blocks
    @Resource(name = ServerSelector.NAME)
    protected ServerSelector serverSelector;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected DataService dataService;

    @Inject
    protected FileLoader fileLoader;

    @Inject
    protected Security security;

    @Inject
    protected DataManager dataManager;

    @GetMapping("/{fileDescriptorId}")
    public void downloadFile(@PathVariable String fileDescriptorId,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) Boolean attachment,
                             HttpServletResponse response) {

        checkCanReadFileDescriptor();

        UUID uuid;
        try {
            uuid = UUID.fromString(fileDescriptorId);
        } catch (IllegalArgumentException e) {
            throw new RestAPIException("Invalid entity ID",
                    String.format("Cannot convert %s into valid entity ID", fileDescriptorId),
                    HttpStatus.BAD_REQUEST,
                    e);
        }
//        LoadContext<Vacation> vacationLoadContext = LoadContext.create(Vacation.class).setId(uuid);
        Vacation vacation = loadVacById(uuid);

        if (vacation == null) {
            throw new RestAPIException("Vacation not found", "Vacation not found. Id: " + fileDescriptorId, HttpStatus.NOT_FOUND);
        }

        FileDescriptor fd = null;

        if (name.equalsIgnoreCase("application")) {
            fd = vacation.getApplication();
        } else {
            fd = vacation.getDecree();
        }

        if (vacation.getApplication() == null) {

        }

//        LoadContext<FileDescriptor> ctx = LoadContext.create(FileDescriptor.class).setId(uuid);
//        FileDescriptor fd = dataService.load(ctx);
        if (fd == null) {
            throw new RestAPIException("File not found", "File not found. Id: " + fileDescriptorId, HttpStatus.NOT_FOUND);
        }

        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-Type", getContentType(fd));
            response.setHeader("Content-Disposition", (BooleanUtils.isTrue(attachment) ? "attachment" : "inline")
                    + "; filename=\"" + URLEncodeUtils.encodeUtf8(fd.getName()) + "\"");

            downloadFromMiddlewareAndWriteResponse(fd, response);
        } catch (Exception e) {
            log.error("Error on downloading the file {}", fileDescriptorId, e);
            throw new RestAPIException("Error on downloading the file", "", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private Vacation loadVacById(UUID vacId) {
        return dataManager.load(Vacation.class).id(vacId).view("vacation-full-view").one();
    }

    private List<Vacation> loadBookPublications(UUID vacId) {
        return dataManager.load(Vacation.class)
                .query("select v from vac_Vacation v where v.id = :vacId")
                .parameter("vacId", vacId)
                .view("vacation-full-view")
                .list();
    }

    protected void downloadFromMiddlewareAndWriteResponse(FileDescriptor fd, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        try (InputStream is = fileLoader.openStream(fd)) {
            IOUtils.copy(is, os);
            os.flush();
        } catch (FileStorageException e) {
            throw new RestAPIException("Unable to download file from FileStorage",
                    "Unable to download file from FileStorage: " + fd.getId(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e);
        }
    }

    protected String getContentType(FileDescriptor fd) {
        if (StringUtils.isEmpty(fd.getExtension())) {
            return FileTypesHelper.DEFAULT_MIME_TYPE;
        }

        return FileTypesHelper.getMIMEType("." + fd.getExtension().toLowerCase());
    }

    protected void checkCanReadFileDescriptor() {
        if (!security.isEntityOpPermitted(FileDescriptor.class, EntityOp.READ)) {
            throw new RestAPIException("Reading forbidden",
                    "Reading of the sys$FileDescriptor is forbidden",
                    HttpStatus.FORBIDDEN);
        }
    }

}
