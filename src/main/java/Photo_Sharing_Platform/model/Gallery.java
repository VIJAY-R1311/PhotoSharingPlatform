package Photo_Sharing_Platform.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "galleries")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false, unique = true)
    private Event event;

    @Column(nullable = false, unique = true)
    private String galleryToken;

    @Column(nullable = false)
    private String pin;

    @Column(nullable = false)
    private boolean published = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime publishedAt;

    public Gallery() {
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public String getGalleryToken() {
        return galleryToken;
    }

    public String getPin() {
        return pin;
    }

    public boolean isPublished() {
        return published;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setGalleryToken(String galleryToken) {
        this.galleryToken = galleryToken;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}