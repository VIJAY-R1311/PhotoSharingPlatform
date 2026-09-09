package Photo_Sharing_Platform.repository;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository
        extends JpaRepository<Photo, Long> {

    List<Photo> findByEvent(Event event);

    List<Photo> findByUploadedBy(User user);

    List<Photo> findByEventAndUploadedBy(
            Event event,
            User user
    );
}
