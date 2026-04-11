package app.repository;

import app.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MessageRepository extends ElasticsearchRepository<Message, UUID> {

    Page<Message> findByChatId(UUID chatId, Pageable pageable);

    Page<Message> findByChatIdOrderByCreatedDesc(UUID chatId, Pageable pageable);

    /**
     * @param contentWildcardPattern full ES wildcard for {@code content}, e.g. {@code *escapedUserText*}
     */
    @Query("""
            {
              "bool": {
                "filter": [
                  { "term": { "chatId": "?1" } }
                ],
                "must": [
                  {
                    "wildcard": {
                      "content": {
                        "value": "?0",
                        "case_insensitive": true
                      }
                    }
                  }
                ]
              }
            }
            """)
    Page<Message> findAllByChatIdAndContentWildcard(String contentWildcardPattern, UUID chatId, Pageable pageable);
}
