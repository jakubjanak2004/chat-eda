package app.util;

import app.entity.Chat;
import app.entity.ChatMembership;
import app.entity.ChatUser;
import app.entity.Message;
import app.mapper.ChatMapper;
import app.mapper.ChatUserMapper;
import app.mapper.MessageMapper;
import app.repository.ChatRepository;
import app.repository.ChatUserRepository;
import app.repository.MessageRepository;
import app.service.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
// TODO: when saving emit the created objects to kafka
public class Generator {
    private static final Faker faker = new Faker(Locale.forLanguageTag("sk"));
    private static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final KafkaPublisher kafkaPublisher;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final ChatUserMapper chatUserMapper;
    private final ChatMapper chatMapper;

    public List<ChatUser> generateChatUsers(int count, ParallelEntitySeedFactory<ChatUser> parallelEntitySeedFactory) {
        List<ChatUser> chatUserList = parallelEntitySeedFactory.createEntities(count);
        LOGGER.info("Saving ChatUser instances...");
        List<ChatUser> chatUsersSavedList = chatUserRepository.saveAll(chatUserList);
        // sending the created users to kafka
        LOGGER.info("ChatUser instances saved");
        chatUsersSavedList.stream()
                .map(chatUserMapper::toUserCreatedEvent)
                .forEach(kafkaPublisher::publishUserCreatedEvent);
        LOGGER.info("ChatUser instances emitted to kafka");
        return chatUsersSavedList;
    }

    public Message generateMessagesForChat(Chat chat, int wordCountFrom, int wordCountTo) {
        int wordCount = ThreadLocalRandom.current().nextInt(wordCountFrom, wordCountTo + 1);
        List<ChatUser> chatUsers = chat.getChatMemberships().stream()
                .map(ChatMembership::getChatUser)
                .toList();
        ChatUser chatUser = chatUsers.get(ThreadLocalRandom.current().nextInt(chatUsers.size()));
        Message responseTo = null;
        if (ThreadLocalRandom.current().nextBoolean()) {
            Page<Message> previousMessages =
                    messageRepository.findAllByChat(chat, PageRequest.of(0, 10));

            if (previousMessages.hasContent()) {
                List<Message> content = previousMessages.getContent();
                int idx = ThreadLocalRandom.current().nextInt(content.size());
                responseTo = content.get(idx);
            }
        }

        Message savedMessage = messageRepository.save(
                Message.builder()
                        .content(faker.lorem().sentence(wordCount))
                        .responseTo(responseTo)
                        .chat(chat)
                        .chatUser(chatUser)
                        .build()
        );
        // flushing to ensure that the created timestamp is not null
        messageRepository.flush();
        kafkaPublisher.publishMessageCreatedEvent(messageMapper.toMessageCreatedEvent(savedMessage));
        return savedMessage;
    }

    public boolean userExistsByUsername(String username) {
        return chatUserRepository.existsByUsername(username);
    }

    public ChatUser saveChatUser(ChatUser chatUser) {
        ChatUser savedChatUser = chatUserRepository.save(chatUser);
        kafkaPublisher.publishUserCreatedEvent(chatUserMapper.toUserCreatedEvent(savedChatUser));
        return savedChatUser;
    }

    public List<Chat> saveAllChats(List<Chat> chats) {
        List<Chat> savedChats = chatRepository.saveAll(chats);
        savedChats.stream()
                .map(chatMapper::toChatCreatedEvent)
                .forEach(kafkaPublisher::publishChatCreatedEvent);
        return savedChats;
    }

    public List<Message> saveAllMessages(List<Message> messages) {
        List<Message> savedMessages = messageRepository.saveAll(messages);
        // flushing to ensure that the created timestamp is not null
        messageRepository.flush();
        savedMessages.stream()
                .map(messageMapper::toMessageCreatedEvent)
                .forEach(kafkaPublisher::publishMessageCreatedEvent);
        return savedMessages;
    }

    public long getNumberOfUsers() {
        return chatUserRepository.count();
    }
}
