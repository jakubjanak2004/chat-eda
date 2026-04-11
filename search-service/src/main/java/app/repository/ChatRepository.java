package app.repository;

import app.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface ChatRepository extends ElasticsearchRepository<Chat, UUID> {

    @Query("""
            {
              "bool": {
                "filter": [
                  { "term": { "usernamesList": "?0" } }
                ]
              }
            }
            """)
    Page<Chat> findChatsForMember(String username, Pageable pageable);

    @Query("""
            {
              "bool": {
                "filter": [
                  { "term": { "usernamesList": "?1" } }
                ],
                "must": [
                  {
                    "wildcard": {
                      "nameNormalized": {
                        "value": "?0",
                        "case_insensitive": true
                      }
                    }
                  }
                ]
              }
            }
            """)
    Page<Chat> findChatsForMemberAndNameNormalizedWildcard(String nameWildcardPattern, String username, Pageable pageable);
}
