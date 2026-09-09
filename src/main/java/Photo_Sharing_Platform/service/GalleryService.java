package Photo_Sharing_Platform.service;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Gallery;
import Photo_Sharing_Platform.repository.GalleryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;

    public GalleryService(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    public Gallery saveGallery(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    public Optional<Gallery> getGalleryByEvent(Event event) {
        return galleryRepository.findByEvent(event);
    }

    public Optional<Gallery> getGalleryByToken(String galleryToken) {
        return galleryRepository.findByGalleryToken(galleryToken);
    }

    public Gallery getGalleryById(Long id) {
        return galleryRepository.findById(id).orElse(null);
    }

    public Gallery publishGallery(Gallery gallery) {

        gallery.setPublished(true);
        gallery.setPublishedAt(LocalDateTime.now());

        return galleryRepository.save(gallery);
    }

    public boolean isValidPin(String pin) {

        if (pin == null) {
            return false;
        }

        return pin.matches("\\d{4,6}");
    }

    public boolean verifyPin(
            Gallery gallery,
            String pin) {

        if (gallery == null ||
                gallery.getPin() == null ||
                pin == null) {

            return false;
        }

        return gallery.getPin().equals(pin);
    }
}
