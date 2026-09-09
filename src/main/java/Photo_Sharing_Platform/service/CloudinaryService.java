package Photo_Sharing_Platform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file, Long eventId) throws IOException {

        Map<String, Object> options = ObjectUtils.asMap(
                "folder", "photo-sharing/events/" + eventId
        );

        Map<?, ?> result = cloudinary.uploader()
                .upload(file.getBytes(), options);

        return result.get("secure_url").toString();
    }
}
