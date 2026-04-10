package app.repository;

import app.entity.ChatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ChatUserRepository extends ElasticsearchRepository<ChatUser, String> {
    Page<ChatUser> findByUsernameNot(String excludeUsername, Pageable pageable);

    @Query("""
            {
              "bool": {
                "must": [
                  { "wildcard": { "nameNormalized": "?0" } }
                ],
                "must_not": [
                  { "term": { "username": "?1" } }
                ]
              }
            }
            """)
    Page<ChatUser> findByNameNormWildcardNotUsername(String nameNormalizedPattern, String excludeUsername, Pageable pageable);
}
