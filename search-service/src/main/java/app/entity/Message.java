package app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document(indexName = "message")
public class Message {
    @Id
    private UUID id;

    @Field(type = FieldType.Text)
    private UUID responseToId;

    @Field(type = FieldType.Text)
    private String responseToSenderUsername;

    @Field(type = FieldType.Text)
    private String responseToContent;

    @Field(type = FieldType.Keyword)
    private UUID chatId;

    @Field(type = FieldType.Text)
    private String senderUsername;

    @Field(type = FieldType.Date)
    private Instant created;

    @Field(type = FieldType.Text)
    private String content;
}
