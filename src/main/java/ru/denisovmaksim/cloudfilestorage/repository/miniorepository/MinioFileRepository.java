package ru.denisovmaksim.cloudfilestorage.repository.miniorepository;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.denisovmaksim.cloudfilestorage.model.StorageObject;
import ru.denisovmaksim.cloudfilestorage.repository.FileRepository;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.denisovmaksim.cloudfilestorage.repository.miniorepository.MinioExceptionHandler.executeWithHandling;
import static ru.denisovmaksim.cloudfilestorage.repository.miniorepository.MinioExceptionHandler.getWithHandling;

@Component
@Slf4j
@Profile({"dev", "prod"})
public class MinioFileRepository implements FileRepository {
    // https://min.io/docs/minio/linux/developers/java/minio-java.html
    // Examples
    // https://github.com/minio/minio-java/tree/release/examples

    //https://min.io/docs/minio/linux/developers/java/API.html
    private final MinioClient minioClient;

    private final String bucket;

    public MinioFileRepository(MinioClient minioClient, @Value("${app.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    public void createFolder(Long userId, String path, String folderName) {
        MinioPath minioPath = new MinioPath(userId, path);
        executeWithHandling(() -> {
                    String newFolderName = minioPath.getUserFolder() + minioPath.getPath() + folderName + "/";
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(newFolderName)
                                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                                    .build());
                }
        );
    }


    public List<StorageObject> getStorageObjects(Long userId, String path) {
        MinioPath minioPath = new MinioPath(userId, path);
        Iterable<Result<Item>> minioItems = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(minioPath.getFullMinioPath())
                        .recursive(true)
                        .build());
        return toStorageObjects(minioPath, minioItems);
    }

    @Override
    public void deleteFolder(Long userId, String path) {
        MinioPath minioPath = new MinioPath(userId, path);
        executeWithHandling(() ->
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(bucket)
                                .object(minioPath.getFullMinioPath())
                                .build())
        );
    }

    private List<StorageObject> toStorageObjects(MinioPath minioPath, Iterable<Result<Item>> resultItems) {
        Map<String, StorageObject> stringStorageObjectMap = new ConcurrentHashMap<>();
        for (Result<Item> resultItem : resultItems) {
            MinioItemDescription itemDescription =
                    getWithHandling(() -> new MinioItemDescription(minioPath, resultItem.get()));
            if (itemDescription.isRootFolder()) {
                continue;
            }
            stringStorageObjectMap.computeIfPresent(itemDescription.getDirectElementName(),
                    (name, object) -> {
                        if (itemDescription.hasOnlyOneChild()) {
                            object.setSize(object.getSize() + 1);
                        }
                        return object;
                    });
            stringStorageObjectMap.putIfAbsent(itemDescription.getDirectElementName(),
                    StorageObject.builder().name(itemDescription.getDirectElementName())
                            .path(itemDescription.getDirectElementPath())
                            .type(itemDescription.getType())
                            .lastModified(itemDescription.getLastModified())
                            .size(itemDescription.hasOnlyOneChild() ? 1L : 0L)
                            .build()
            );
        }
        return stringStorageObjectMap.values()
                .stream()
                .toList();
    }
}
