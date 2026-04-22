package app.repository;

import app.entity.ActiveMembership;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface ActiveMembershipRepository extends ElasticsearchRepository<ActiveMembership, UUID> {
    boolean existsByChatIdAndUsername(UUID chatId, String username);
}
