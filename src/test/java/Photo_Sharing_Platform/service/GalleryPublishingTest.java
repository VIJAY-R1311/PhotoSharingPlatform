package Photo_Sharing_Platform.service;

import Photo_Sharing_Platform.model.Gallery;
import Photo_Sharing_Platform.repository.GalleryRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GalleryPublishingTest {

    private final GalleryRepository galleryRepository =
            mock(GalleryRepository.class);

    private final GalleryService galleryService =
            new GalleryService(galleryRepository);

    @Test
    void publishGalleryShouldSetPublishedToTrue() {

        Gallery gallery = new Gallery();

        when(galleryRepository.save(gallery))
                .thenReturn(gallery);

        Gallery result = galleryService.publishGallery(gallery);

        assertTrue(result.isPublished());

        verify(galleryRepository).save(gallery);
    }

    @Test
    void publishGalleryShouldSetPublishedAtTime() {

        Gallery gallery = new Gallery();

        when(galleryRepository.save(gallery))
                .thenReturn(gallery);

        Gallery result = galleryService.publishGallery(gallery);

        assertNotNull(result.getPublishedAt());
        assertTrue(
                result.getPublishedAt()
                        .isBefore(LocalDateTime.now().plusSeconds(1))
        );

        verify(galleryRepository).save(gallery);
    }

    @Test
    void publishGalleryShouldSaveGallery() {

        Gallery gallery = new Gallery();

        when(galleryRepository.save(gallery))
                .thenReturn(gallery);

        galleryService.publishGallery(gallery);

        verify(galleryRepository, times(1))
                .save(gallery);
    }
}
