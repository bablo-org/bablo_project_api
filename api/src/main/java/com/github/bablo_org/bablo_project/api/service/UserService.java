package com.github.bablo_org.bablo_project.api.service;

import static com.github.bablo_org.bablo_project.api.model.User.ofDoc;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.model.Settings;
import com.github.bablo_org.bablo_project.api.model.StorageFile;
import com.github.bablo_org.bablo_project.api.model.User;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
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
public class UserService {

    private static final String DB_COLLECTION_NAME = "users";

    private static final String STORAGE_BUCKET_NAME = "bablo-project.appspot.com";

    private static final String STORAGE_AVATARS_DIRECTORY = "avatars";

    private final Firestore firestore;

    private final Storage cloudStorage;

    private CurrencyService currencyService;

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

    @SneakyThrows
    public User getById(String id) {
        DocumentReference ref = getRefById(id);
        return ofDoc(ref.get().get());
    }

    @SneakyThrows
    public User updateCurrentProfile(String name, String avatar, String userId) {
        DocumentReference ref = getRefById(userId);
        DocumentSnapshot doc = ref.get().get();
        if (!doc.exists()) {
            throw new RuntimeException("User with such id does note exist");
        }

        User user = ofDoc(doc);
        validateUpdateProfile(user, userId);

        Map<String, Object> fields = new HashMap<>();
        if (name != null) {
            user.setName(name);
            fields.put("name", name);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
            fields.put("avatar", avatar);
        }

        if (!fields.isEmpty()) {
            ref.update(fields).get();
        }

        return user;
    }

    @SneakyThrows
    public void updateSettings(Settings settings, String userId) {
        validateSettings(settings);

        firestore.collection(DB_COLLECTION_NAME)
                .document(userId)
                .update(settings.toMap())
                .get();
    }

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

    @SneakyThrows
    private void validateSettings(Settings settings) {
        List<String> currencyIds = settings.getFavoriteCurrencies();

        if (currencyIds != null) {
            List<String> dbCurrencyIds = currencyService.getById(currencyIds)
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

    private void validateUpdateProfile(User user, String callerId) {
        if (user == null) {
            throw new RuntimeException("can't update non-existent user");
        }

        if (!user.getId().equals(callerId)) {
            throw new RuntimeException("can't update user - current user is not a user to be updated");
        }
    }
}
