package Photo_Sharing_Platform.service;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event, User admin) {

        event.setCreatedBy(admin);

        return eventRepository.save(event);
    }

    public List<Event> getAdminEvents(User admin) {

        return eventRepository.findByCreatedBy(admin);
    }

    public List<Event> getTeamMemberEvents(User teamMember) {

        return eventRepository.findByTeamMembersContaining(teamMember);
    }

    public Event getEventById(Long id) {

        return eventRepository.findById(id)
                .orElse(null);
    }

    public void addTeamMember(Event event, User teamMember) {

        event.getTeamMembers().add(teamMember);

        eventRepository.save(event);
    }
}