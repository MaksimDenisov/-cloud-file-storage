package ru.denisovmaksim.cloudfilestorage.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;


@Getter
@ToString
@Slf4j
public class StorageObject {
    private String parentPath = "";

    private final String name;

    private final boolean folder;

    @Setter
    private long size;


    private StorageObject(Builder builder) {
        String path = builder.path;
        this.folder = path.endsWith("/");
        if (path.endsWith("/")) {
            this.name = path.substring(path.lastIndexOf('/', path.length() - 2) + 1, path.length() - 1);
            path = path.substring(0, path.length() - 1);
        } else {
            this.name = path.substring(path.lastIndexOf('/') + 1);
        }

        int lastSlashIndex = path.lastIndexOf('/');
        this.parentPath = (lastSlashIndex != -1) ? path.substring(0, lastSlashIndex + 1) : "";

        if (this.folder) {
            this.size = (builder.folderSizeSupplier != null) ? builder.folderSizeSupplier.get() : 0L;
        } else {
            this.size = builder.size;
        }
    }

    public String getPath() {
        return parentPath + name + (isFolder() ? "/" : "");
    }

    static class Builder {
        private final String path;
        private long size = 0;

        private Supplier<Long> folderSizeSupplier;

        Builder(String path) {
            this.path = path;
        }

        public Builder withFolderSizeSupplier(Supplier<Long> folderSizeSupplier) {
            this.folderSizeSupplier = folderSizeSupplier;
            return this;
        }

        public Builder objectSize(long size) {
            this.size = size;
            return this;
        }

        public StorageObject build() {
            return new StorageObject(this);
        }
    }

    StorageObject(String path) {
        this.folder = path.endsWith("/");
        if (path.endsWith("/")) {
            this.name = path.substring(path.lastIndexOf('/', path.length() - 2) + 1, path.length() - 1);
            path = path.substring(0, path.length() - 1);
        } else {
            this.name = path.substring(path.lastIndexOf('/') + 1);
        }
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex != -1) {
            this.parentPath = path.substring(0, lastSlashIndex + 1);
        }
    }


}
