package com.github.bablo_org.bablo_project.api.service.impl;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

import com.github.bablo_org.bablo_project.api.model.domain.Currency;
import com.github.bablo_org.bablo_project.api.model.domain.Settings;
import com.github.bablo_org.bablo_project.api.model.domain.StorageFile;
import com.github.bablo_org.bablo_project.api.model.domain.User;
import com.github.bablo_org.bablo_project.api.model.dto.UpdateUserProfileRequest;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.github.bablo_org.bablo_project.api.service.TelegramService;
import com.github.bablo_org.bablo_project.api.service.UserService;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DB_COLLECTION_NAME = "users";

    private static final String STORAGE_BUCKET_NAME = "bablo-project.appspot.com";

    private static final String STORAGE_AVATARS_DIRECTORY = "avatars";

    private final Firestore firestore;

    private final Storage cloudStorage;

    private final CurrencyService currencyService;

    private final TelegramService telegramService;

    @Override
    @Nullable
    @SneakyThrows
    public User getById(String id) {
        DocumentSnapshot doc = getRefById(id)
                .get()
                .get();

        return doc.exists() ? User.ofDoc(doc) : null;
    }

    @Override
    @SneakyThrows
    public Map<String, User> getByIds(String... ids) {
        return firestore.collection(DB_COLLECTION_NAME)
                .where(Filter.inArray(FieldPath.documentId(), ids))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(User::ofDoc)
                .collect(toMap(user -> user.getId(), user -> user));
    }

    @Override
    @SneakyThrows
    public List<User> getAll() {
        return firestore.collection(DB_COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(User::ofDoc)
                .collect(toList());
    }

    @Override
    @SneakyThrows
    public void addOrUpdate(String id, Map<String, Object> fields) {
        firestore.collection(DB_COLLECTION_NAME)
                .document(id)
                .set(fields)
                .get();
    }

    @Override
    @SneakyThrows
    public void updateCurrentProfile(UpdateUserProfileRequest profile, String userId) {
        getRefById(userId)
                .update(profile.toMap())
                .get();
    }

    @Override
    @SneakyThrows
    public void updateSettings(Settings settings, String userId) {
        validateSettings(settings);

        if (settings.getEnableTelegramNotifications() != null && settings.getFavoriteCurrencies() != null) {
            firestore.collection(DB_COLLECTION_NAME)
                    .document(userId)
                    .update(
                            FieldPath.of("privateData", "settings", "enableTelegramNotifications"),
                            settings.getEnableTelegramNotifications(),
                            FieldPath.of("privateData", "settings", "favoriteCurrencies"),
                            settings.getFavoriteCurrencies()
                    )
                    .get();
        }

        if (settings.getEnableTelegramNotifications() != null) {
            firestore.collection(DB_COLLECTION_NAME)
                    .document(userId)
                    .update(
                            FieldPath.of("privateData", "settings", "enableTelegramNotifications"),
                            settings.getEnableTelegramNotifications()
                    )
                    .get();
        }

        if (settings.getFavoriteCurrencies() != null) {
            firestore.collection(DB_COLLECTION_NAME)
                    .document(userId)
                    .update(
                            FieldPath.of("privateData", "settings", "favoriteCurrencies"), settings.getFavoriteCurrencies()
                    )
                    .get();
        }
    }

    @Override
    public StorageFile uploadAvatar(String fileName, byte[] content, String user) {
        String personalizedFileName = user + "-" + fileName; // 2+ users may upload avatar with the same name
        String filePath = STORAGE_AVATARS_DIRECTORY + "/" + personalizedFileName;
        BlobInfo blobInfo = BlobInfo.newBuilder(STORAGE_BUCKET_NAME, filePath).build();
        try (InputStream is = new ByteArrayInputStream(content)) {
            Blob blob = cloudStorage.createFrom(blobInfo, is);
            try {
                deletePreviousAvatars(user);
            } catch (Exception e) {
                log.error("failed to delete previous avatars", e);
            }
            return new StorageFile(blob.getBlobId().getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public void connectTelegram(String telegramUser, String userId) {
        String tgId = telegramService.resolveId(telegramUser);
        firestore.collection(DB_COLLECTION_NAME)
                .document(userId)
                .update(
                        FieldPath.of("privateData", "telegramUser"), telegramUser,
                        FieldPath.of("privateData", "telegramId"), tgId,
                        FieldPath.of("privateData", "settings", "enableTelegramNotifications"), true
                )
                .get();
    }

    @SneakyThrows
    private void validateSettings(Settings settings) {
        List<String> currencyIds = settings.getFavoriteCurrencies();

        if (currencyIds != null) {
            List<String> dbCurrencyIds = currencyService.getByIds(currencyIds)
                    .stream()
                    .map(Currency::getId)
                    .collect(toList());

            Set<String> unknownCurrencies = currencyIds
                    .stream()
                    .filter(id -> !dbCurrencyIds.contains(id))
                    .collect(toSet());

            if (!unknownCurrencies.isEmpty()) {
                throw new RuntimeException("unknown currencies: " + unknownCurrencies);
            }
        }
    }

    private void deletePreviousAvatars(String user) {
        Iterable<Blob> allAvatars = cloudStorage.get(STORAGE_BUCKET_NAME)
                .list(Storage.BlobListOption.prefix(STORAGE_AVATARS_DIRECTORY))
                .iterateAll();

        StreamSupport.stream(allAvatars.spliterator(), false)
                .filter(blob -> blob.getName().startsWith(STORAGE_AVATARS_DIRECTORY + "/" + user + "-"))
                .sorted(comparing(BlobInfo::getUpdateTimeOffsetDateTime).reversed())
                .skip(1)
                .forEach(Blob::delete);
    }

    @SneakyThrows
    private DocumentReference getRefById(String id) {
        return firestore
                .collection(DB_COLLECTION_NAME)
                .document(id);
    }
}
