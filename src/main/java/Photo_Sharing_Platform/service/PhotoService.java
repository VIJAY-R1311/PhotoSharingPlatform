package Photo_Sharing_Platform.service;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Photo;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public List<Photo> getPhotosForEvent(Event event) {
        return photoRepository.findByEvent(event);
    }

    public List<Photo> getPhotosUploadedBy(User user) {
        return photoRepository.findByUploadedBy(user);
    }

    public List<Photo> getMyPhotosForEvent(
            Event event,
            User user) {

        return photoRepository.findByEventAndUploadedBy(
                event,
                user
        );
    }

    public Photo getPhotoById(Long photoId) {
        return photoRepository.findById(photoId)
                .orElse(null);
    }

    public void togglePhotoSelection(Long photoId) {

        Photo photo = getPhotoById(photoId);

        if (photo == null) {
            return;
        }

        photo.setSelected(!photo.isSelected());

        photoRepository.save(photo);
    }
}
