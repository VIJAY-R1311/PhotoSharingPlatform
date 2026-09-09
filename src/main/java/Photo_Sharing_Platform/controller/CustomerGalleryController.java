package Photo_Sharing_Platform.controller;

import Photo_Sharing_Platform.model.Gallery;
import Photo_Sharing_Platform.model.Photo;
import Photo_Sharing_Platform.service.GalleryService;
import Photo_Sharing_Platform.service.PhotoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gallery")
public class CustomerGalleryController {

    private final GalleryService galleryService;
    private final PhotoService photoService;

    public CustomerGalleryController(
            GalleryService galleryService,
            PhotoService photoService) {

        this.galleryService = galleryService;
        this.photoService = photoService;
    }

    @GetMapping("/{token}")
    public String showPinPage(
            @PathVariable String token,
            Model model) {

        Gallery gallery =
                galleryService
                        .getGalleryByToken(token)
                        .orElse(null);

        if (gallery == null ||
                !gallery.isPublished()) {

            return "gallery-unavailable";
        }

        model.addAttribute(
                "gallery",
                gallery
        );

        return "gallery-pin";
    }

    @PostMapping("/{token}/verify")
    public String verifyPin(
            @PathVariable String token,
            @RequestParam String pin,
            HttpSession session,
            Model model) {

        Gallery gallery =
                galleryService
                        .getGalleryByToken(token)
                        .orElse(null);

        if (gallery == null ||
                !gallery.isPublished()) {

            return "gallery-unavailable";
        }

        if (!galleryService.verifyPin(
                gallery,
                pin)) {

            model.addAttribute(
                    "gallery",
                    gallery
            );

            model.addAttribute(
                    "error",
                    "Incorrect PIN. Please try again."
            );

            return "gallery-pin";
        }

        session.setAttribute(
                "authorizedGalleryToken",
                gallery.getGalleryToken()
        );

        return "redirect:/gallery/"
                + token
                + "/photos";
    }

    @GetMapping("/{token}/photos")
    public String showGallery(
            @PathVariable String token,
            HttpSession session,
            Model model) {

        Gallery gallery =
                galleryService
                        .getGalleryByToken(token)
                        .orElse(null);

        if (gallery == null ||
                !gallery.isPublished()) {

            return "gallery-unavailable";
        }

        Object authorizedToken =
                session.getAttribute(
                        "authorizedGalleryToken"
                );

        if (authorizedToken == null ||
                !token.equals(
                        authorizedToken.toString())) {

            return "redirect:/gallery/" + token;
        }

        List<Photo> selectedPhotos =
                photoService
                        .getPhotosForEvent(
                                gallery.getEvent()
                        )
                        .stream()
                        .filter(Photo::isSelected)
                        .toList();

        model.addAttribute(
                "gallery",
                gallery
        );

        model.addAttribute(
                "photos",
                selectedPhotos
        );

        return "customer-gallery";
    }
}