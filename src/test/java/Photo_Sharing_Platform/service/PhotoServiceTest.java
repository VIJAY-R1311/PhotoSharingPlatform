package Photo_Sharing_Platform.service;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Photo;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.PhotoRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoServiceTest {

    private final PhotoRepository photoRepository =
            mock(PhotoRepository.class);

    private final PhotoService photoService =
            new PhotoService(photoRepository);

    @Test
    void getPhotosForEventShouldReturnEventPhotos() {

        Event event = new Event();
        List<Photo> photos = List.of(new Photo(), new Photo());

        when(photoRepository.findByEvent(event))
                .thenReturn(photos);

        List<Photo> result = photoService.getPhotosForEvent(event);

        assertEquals(2, result.size());
        assertEquals(photos, result);

        verify(photoRepository).findByEvent(event);
    }

    @Test
    void getMyPhotosForEventShouldReturnOnlyUserPhotos() {

        Event event = new Event();
        User user = new User();

        List<Photo> photos = List.of(new Photo());

        when(photoRepository.findByEventAndUploadedBy(event, user))
                .thenReturn(photos);

        List<Photo> result =
                photoService.getMyPhotosForEvent(event, user);

        assertEquals(1, result.size());
        assertEquals(photos, result);

        verify(photoRepository)
                .findByEventAndUploadedBy(event, user);
    }

    @Test
    void getPhotosUploadedByShouldReturnUserPhotos() {

        User user = new User();
        List<Photo> photos = List.of(new Photo(), new Photo());

        when(photoRepository.findByUploadedBy(user))
                .thenReturn(photos);

        List<Photo> result =
                photoService.getPhotosUploadedBy(user);

        assertEquals(2, result.size());

        verify(photoRepository).findByUploadedBy(user);
    }

    @Test
    void getPhotoByIdShouldReturnPhotoWhenFound() {

        Photo photo = new Photo();

        when(photoRepository.findById(1L))
                .thenReturn(Optional.of(photo));

        Photo result = photoService.getPhotoById(1L);

        assertNotNull(result);
        assertEquals(photo, result);

        verify(photoRepository).findById(1L);
    }

    @Test
    void getPhotoByIdShouldReturnNullWhenNotFound() {

        when(photoRepository.findById(999L))
                .thenReturn(Optional.empty());

        Photo result = photoService.getPhotoById(999L);

        assertNull(result);

        verify(photoRepository).findById(999L);
    }

    @Test
    void togglePhotoSelectionShouldSelectUnselectedPhoto() {

        Photo photo = new Photo();
        photo.setSelected(false);

        when(photoRepository.findById(1L))
                .thenReturn(Optional.of(photo));

        photoService.togglePhotoSelection(1L);

        assertTrue(photo.isSelected());

        verify(photoRepository).save(photo);
    }

    @Test
    void togglePhotoSelectionShouldUnselectSelectedPhoto() {

        Photo photo = new Photo();
        photo.setSelected(true);

        when(photoRepository.findById(1L))
                .thenReturn(Optional.of(photo));

        photoService.togglePhotoSelection(1L);

        assertFalse(photo.isSelected());

        verify(photoRepository).save(photo);
    }

    @Test
    void togglePhotoSelectionShouldDoNothingWhenPhotoNotFound() {

        when(photoRepository.findById(999L))
                .thenReturn(Optional.empty());

        photoService.togglePhotoSelection(999L);

        verify(photoRepository, never()).save(any(Photo.class));
    }
}
