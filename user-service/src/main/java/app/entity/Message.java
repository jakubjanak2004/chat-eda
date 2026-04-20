package app.entity;

import config.props.ValidationConstraints;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
// todo remove the message entity as it is being handled by search-service
public class Message {
    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant created;

    @ManyToOne
    @JoinColumn
    private ChatUser chatUser;

    @ManyToOne
    @JoinColumn
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_to_id")
    private Message responseTo;

    @OneToMany(mappedBy = "responseTo")
    @Builder.Default
    private List<Message> responses = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 1, max = ValidationConstraints.MESSAGE_CONTENT_MAX)
    private String content;

    @Override
    public String toString() {
        String preview = Objects.toString(content);
        return String.format("Message{id=%s, created=%s, content=%s}", id, created, preview);
    }
}
