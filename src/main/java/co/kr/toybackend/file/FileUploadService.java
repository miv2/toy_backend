package co.kr.toybackend.file;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    //public static final String PATH =   /home/git/withPet/images
    public static final String PATH = "C:\\Users\\cu_bo\\OneDrive\\바탕 화면\\media\\images";
    public static final String TEST = "C:/Users/cu_bo/OneDrive/바탕 화면";

    public String fileUpload(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("파일명은 필수입니다.");
        }

        String uuid = UUID.randomUUID().toString();
        String extension = getExtension(originalFilename);
        String newFileName = uuid + "." + extension;

        Path destinationDirectory = Paths.get(PATH);

        // Create the destination directory if it does not exist
        if (!Files.exists(destinationDirectory.getParent())) {
            try {
                Files.createDirectories(destinationDirectory);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not create directory: " + destinationDirectory);
            }
        }

        Path copyOfLocation = destinationDirectory.resolve(StringUtils.cleanPath(newFileName));

        try {
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
            return Path.of("", "/images", newFileName).toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not store file: " + originalFilename);
        }
    }

    private String getExtension(String fileName) {
        try {
            System.out.println("filename : " + fileName);
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("파일 확장자를 찾을 수 없습니다.");
        }
    }

    public List<String> uploadFile(MultipartFile... images) {
        List<String> imagePaths = new ArrayList<>();

        for (MultipartFile image : images) {
            if (image != null) {
                imagePaths.add(this.fileUpload(image));
            }
        }

        return imagePaths;
    }
}
