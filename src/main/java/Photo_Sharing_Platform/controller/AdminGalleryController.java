package Photo_Sharing_Platform.controller;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Gallery;
import Photo_Sharing_Platform.model.Photo;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.UserRepository;
import Photo_Sharing_Platform.service.EventService;
import Photo_Sharing_Platform.service.GalleryService;
import Photo_Sharing_Platform.service.PhotoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminGalleryController {

    private final EventService eventService;
    private final GalleryService galleryService;
    private final PhotoService photoService;
    private final UserRepository userRepository;

    public AdminGalleryController(
            EventService eventService,
            GalleryService galleryService,
            PhotoService photoService,
            UserRepository userRepository) {

        this.eventService = eventService;
        this.galleryService = galleryService;
        this.photoService = photoService;
        this.userRepository = userRepository;
    }

    @GetMapping("/event/{eventId}/gallery")
    public String manageGallery(
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

        List<Photo> selectedPhotos =
                photos.stream()
                        .filter(Photo::isSelected)
                        .toList();

        Gallery gallery =
                galleryService
                        .getGalleryByEvent(event)
                        .orElse(null);

        model.addAttribute("admin", admin);
        model.addAttribute("event", event);
        model.addAttribute("photos", photos);
        model.addAttribute("selectedPhotos", selectedPhotos);
        model.addAttribute("gallery", gallery);

        return "admin-gallery";
    }

    @PostMapping("/event/{eventId}/gallery/create")
    public String createGallery(
            @PathVariable Long eventId,
            @RequestParam String pin,
            Authentication authentication,
            Model model) {

        userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return "redirect:/admin/dashboard";
        }

        if (!galleryService.isValidPin(pin)) {

            model.addAttribute(
                    "pinError",
                    "PIN must contain 4 to 6 digits."
            );

            return loadGalleryPage(
                    event,
                    authentication,
                    model
            );
        }

        Gallery gallery =
                galleryService
                        .getGalleryByEvent(event)
                        .orElse(new Gallery());

        if (gallery.getId() == null) {

            gallery.setEvent(event);

            gallery.setGalleryToken(
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
            );

            gallery.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        gallery.setPin(pin);

        galleryService.saveGallery(gallery);

        return "redirect:/admin/event/"
                + eventId
                + "/gallery";
    }

    @PostMapping("/event/{eventId}/gallery/publish")
    public String publishGallery(
            @PathVariable Long eventId,
            Authentication authentication) {

        userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Event event =
                eventService.getEventById(eventId);

        if (event == null) {
            return "redirect:/admin/dashboard";
        }

        Gallery gallery =
                galleryService
                        .getGalleryByEvent(event)
                        .orElse(null);

        if (gallery == null) {
            return "redirect:/admin/event/"
                    + eventId
                    + "/gallery";
        }

        if (!galleryService.isValidPin(
                gallery.getPin())) {

            return "redirect:/admin/event/"
                    + eventId
                    + "/gallery";
        }

        List<Photo> selectedPhotos =
                photoService
                        .getPhotosForEvent(event)
                        .stream()
                        .filter(Photo::isSelected)
                        .toList();

        if (selectedPhotos.isEmpty()) {

            return "redirect:/admin/event/"
                    + eventId
                    + "/gallery";
        }

        galleryService.publishGallery(gallery);

        return "redirect:/admin/event/"
                + eventId
                + "/gallery";
    }

    private String loadGalleryPage(
            Event event,
            Authentication authentication,
            Model model) {

        User admin = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        List<Photo> photos =
                photoService.getPhotosForEvent(event);

        List<Photo> selectedPhotos =
                photos.stream()
                        .filter(Photo::isSelected)
                        .toList();

        Gallery gallery =
                galleryService
                        .getGalleryByEvent(event)
                        .orElse(null);

        model.addAttribute("admin", admin);
        model.addAttribute("event", event);
        model.addAttribute("photos", photos);
        model.addAttribute("selectedPhotos", selectedPhotos);
        model.addAttribute("gallery", gallery);

        return "admin-gallery";
    }
}