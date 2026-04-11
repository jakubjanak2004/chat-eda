package app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document(indexName = "chat")
public class Chat {
    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private String nameNormalized;

    @Field(type = FieldType.Keyword)
    private List<String> usernamesList;
}
