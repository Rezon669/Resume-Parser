package com.hire.resumeparser.controller;

import com.hire.resumeparser.service.FileService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/resume")
public class FileController {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> resumeUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                log.warn("No file provided");
                return new ResponseEntity<>("No file provided", HttpStatus.BAD_REQUEST);
            }

            // Get file extension
            String filename = file.getOriginalFilename();
            if (filename == null || !isValidResumeFile(filename)) {
                log.warn("Invalid file type: {}", filename);
                return new ResponseEntity<>("Only PDF, DOC, and DOCX files are allowed", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            log.info("Uploading file: {}", filename);
            fileService.uploadResume(file);
            log.info("File uploaded successfully");

            return new ResponseEntity<>("Resume uploaded successfully", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error while uploading file", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidResumeFile(String filename) {
        String lowercase = filename.toLowerCase();
        return lowercase.endsWith(".pdf") || lowercase.endsWith(".doc") || lowercase.endsWith(".docx");
    }
}
