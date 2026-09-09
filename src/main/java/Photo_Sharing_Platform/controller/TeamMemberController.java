package Photo_Sharing_Platform.controller;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Photo;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.UserRepository;
import Photo_Sharing_Platform.service.CloudinaryService;
import Photo_Sharing_Platform.service.EventService;
import Photo_Sharing_Platform.service.PhotoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/team")
public class TeamMemberController {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final PhotoService photoService;
    private final CloudinaryService cloudinaryService;

    public TeamMemberController(
            EventService eventService,
            UserRepository userRepository,
            PhotoService photoService,
            CloudinaryService cloudinaryService) {

        this.eventService = eventService;
        this.userRepository = userRepository;
        this.photoService = photoService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        User teamMember = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        List<Event> events =
                eventService.getTeamMemberEvents(teamMember);

        model.addAttribute("events", events);
        model.addAttribute("teamMember", teamMember);

        return "team-dashboard";
    }

    @GetMapping("/event/{eventId}")
    public String viewEvent(
            @PathVariable Long eventId,
            Authentication authentication,
            Model model) {

        User teamMember = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Event event = eventService.getEventById(eventId);

        if (!isAssignedToEvent(event, teamMember)) {
            return "redirect:/team/dashboard";
        }

        model.addAttribute("event", event);
        model.addAttribute("teamMember", teamMember);

        model.addAttribute(
                "photos",
                photoService.getMyPhotosForEvent(
                        event,
                        teamMember
                )
        );

        return "team-event";
    }

    @PostMapping("/event/{eventId}/upload")
    public String uploadPhotos(
            @PathVariable Long eventId,
            @RequestParam("files") MultipartFile[] files,
            Authentication authentication,
            Model model) {

        User teamMember = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Event event = eventService.getEventById(eventId);

        if (!isAssignedToEvent(event, teamMember)) {
            return "redirect:/team/dashboard";
        }

        int uploadedCount = 0;
        int skippedCount = 0;

        try {

            for (MultipartFile file : files) {

                if (file == null || file.isEmpty()) {
                    skippedCount++;
                    continue;
                }

                String contentType = file.getContentType();

                if (contentType == null ||
                        !contentType.startsWith("image/")) {

                    skippedCount++;
                    continue;
                }

                if (file.getSize() > 10 * 1024 * 1024) {
                    skippedCount++;
                    continue;
                }

                String originalFilename =
                        file.getOriginalFilename();

                if (originalFilename == null ||
                        originalFilename.isBlank()) {

                    skippedCount++;
                    continue;
                }

                String imageUrl =
                        cloudinaryService.uploadImage(
                                file,
                                eventId
                        );

                Photo photo = new Photo();

                photo.setEvent(event);
                photo.setUploadedBy(teamMember);
                photo.setFileName(originalFilename);
                photo.setStorageLocation(imageUrl);
                photo.setFileSize(file.getSize());
                photo.setCreatedAt(LocalDateTime.now());
                photo.setSelected(false);

                photoService.savePhoto(photo);

                uploadedCount++;
            }

        } catch (IOException e) {

            model.addAttribute(
                    "uploadError",
                    "Photo upload failed. Please try again."
            );

            model.addAttribute("event", event);
            model.addAttribute("teamMember", teamMember);

            model.addAttribute(
                    "photos",
                    photoService.getMyPhotosForEvent(
                            event,
                            teamMember
                    )
            );

            return "team-event";
        }

        if (uploadedCount > 0) {

            model.addAttribute(
                    "uploadSuccess",
                    uploadedCount +
                            " photo(s) uploaded successfully!"
            );
        }

        if (skippedCount > 0) {

            model.addAttribute(
                    "uploadError",
                    skippedCount +
                            " file(s) were skipped. " +
                            "Only valid image files up to 10MB are accepted."
            );
        }

        model.addAttribute("event", event);
        model.addAttribute("teamMember", teamMember);

        model.addAttribute(
                "photos",
                photoService.getMyPhotosForEvent(
                        event,
                        teamMember
                )
        );

        return "team-event";
    }

    private boolean isAssignedToEvent(
            Event event,
            User teamMember) {

        return event != null &&
                event.getTeamMembers().contains(teamMember);
    }
}