package Photo_Sharing_Platform.repository;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCreatedBy(User user);

    List<Event> findByTeamMembersContaining(User user);
}