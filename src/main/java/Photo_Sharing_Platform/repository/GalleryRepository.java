package Photo_Sharing_Platform.repository;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    Optional<Gallery> findByEvent(Event event);

    Optional<Gallery> findByGalleryToken(String galleryToken);
}