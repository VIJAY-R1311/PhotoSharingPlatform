package Photo_Sharing_Platform.service;

import Photo_Sharing_Platform.model.Gallery;
import Photo_Sharing_Platform.repository.GalleryRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GalleryServiceTest {

    private final GalleryRepository galleryRepository =
            mock(GalleryRepository.class);

    private final GalleryService galleryService =
            new GalleryService(galleryRepository);

    @Test
    void validPinShouldReturnTrue() {
        assertTrue(galleryService.isValidPin("1234"));
        assertTrue(galleryService.isValidPin("123456"));
    }

    @Test
    void invalidPinShouldReturnFalse() {
        assertFalse(galleryService.isValidPin("123"));
        assertFalse(galleryService.isValidPin("1234567"));
        assertFalse(galleryService.isValidPin("abcd"));
    }

    @Test
    void correctPinShouldReturnTrue() {
        Gallery gallery = new Gallery();
        gallery.setPin("1234");

        assertTrue(galleryService.verifyPin(gallery, "1234"));
    }

    @Test
    void wrongPinShouldReturnFalse() {
        Gallery gallery = new Gallery();
        gallery.setPin("1234");

        assertFalse(galleryService.verifyPin(gallery, "9999"));
    }

    @Test
    void nullGalleryShouldReturnFalse() {
        assertFalse(galleryService.verifyPin(null, "1234"));
    }

    @Test
    void nullPinShouldReturnFalse() {
        Gallery gallery = new Gallery();
        gallery.setPin("1234");

        assertFalse(galleryService.verifyPin(gallery, null));
    }
}
