package Photo_Sharing_Platform.controller;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Photo;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.UserRepository;
import Photo_Sharing_Platform.service.EventService;
import Photo_Sharing_Platform.service.PhotoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPhotoController {

    private final EventService eventService;
    private final PhotoService photoService;
    private final UserRepository userRepository;

    public AdminPhotoController(
            EventService eventService,
            PhotoService photoService,
            UserRepository userRepository) {

        this.eventService = eventService;
        this.photoService = photoService;
        this.userRepository = userRepository;
    }

    @GetMapping("/event/{eventId}/photos")
    public String viewEventPhotos(
            @PathVariable Long eventId,
            Authentication authentication,
            Model model) {

        User admin = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return "redirect:/admin/dashboard";
        }

        List<Photo> photos =
                photoService.getPhotosForEvent(event);

        long selectedCount = photos.stream()
                .filter(Photo::isSelected)
                .count();

        model.addAttribute("event", event);
        model.addAttribute("admin", admin);
        model.addAttribute("photos", photos);
        model.addAttribute("selectedCount", selectedCount);

        return "admin-event-photos";
    }

    @PostMapping("/photo/{photoId}/toggle")
    public String togglePhotoSelection(
            @PathVariable Long photoId,
            @RequestParam Long eventId,
            Authentication authentication) {

        userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return "redirect:/admin/dashboard";
        }

        Photo photo = photoService.getPhotoById(photoId);

        if (photo == null) {
            return "redirect:/admin/event/"
                    + eventId
                    + "/photos";
        }

        if (photo.getEvent() == null ||
                !photo.getEvent().getId().equals(event.getId())) {

            return "redirect:/admin/event/"
                    + eventId
                    + "/photos";
        }

        photoService.togglePhotoSelection(photoId);

        return "redirect:/admin/event/"
                + eventId
                + "/photos";
    }
}
