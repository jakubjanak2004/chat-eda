package app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Getter
@Setter
@Document(indexName="activeMembership")
public class ActiveMembership {
    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private UUID chatId;

    @Field(type = FieldType.Keyword)
    private String username;
}
