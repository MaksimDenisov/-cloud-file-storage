package ru.denisovmaksim.cloudfilestorage.repository.miniorepository;

import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.denisovmaksim.cloudfilestorage.model.StorageObject;

@Slf4j
@Component
public class MinioItemToStorageObjectMapper {

     StorageObject toStorageObject(MinioPath minioPath, Item item) {
        String minioName = item.objectName();
        String path = minioName.replace(minioPath.getUserFolder(), "");
        StorageObject object = new StorageObject(path);
        if (!object.isFolder()) {
            object.setSize(item.size());
        }
        log.info("{} to {}", minioPath, object);
        return object;
    }
}
