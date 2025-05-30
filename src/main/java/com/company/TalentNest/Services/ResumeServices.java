package com.company.TalentNest.Services;

import com.company.TalentNest.Model.Resume;
import com.company.TalentNest.Repo.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResumeServices {

    @Autowired
    private ResumeRepository resumeRepository;

    private final String FOLDER_PATH = "C:\\Personal\\Study\\myFiles\\";

    public String uploadResume(MultipartFile file, Integer userId) throws IOException {

        Optional<Resume> checkIfExisits =  resumeRepository.findByUserId(userId);
        if(checkIfExisits.isPresent()){return "You already have uploaded a resume!";}
        String originalFilename = file.getOriginalFilename();
        String filePath = FOLDER_PATH + originalFilename;

        file.transferTo(new File(filePath));

        Resume resume = Resume.builder()
                .userId(userId)
                .fileName(originalFilename)
                .filePath(filePath)
                .uploadedAt(LocalDateTime.now())
                .build();

        resumeRepository.save(resume);

        return "File uploaded successfully: " + originalFilename;
    }

    public byte[] downloadResume(Integer userId) throws IOException {
        Optional<Resume> resumeOptional = resumeRepository.findByUserId(userId);

        if (resumeOptional.isPresent()) {
            Resume resume = resumeOptional.get();
            File file = new File(resume.getFilePath());

            if (file.exists()) {
                return Files.readAllBytes(file.toPath());
            } else {
                throw new IOException("File not found on disk");
            }
        } else {
            throw new IOException("Resume not found for user ID: " + userId);
        }
    }
}
