package ru.denisovmaksim.cloudfilestorage.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.nio.file.Path;
import java.nio.file.Paths;


@RequiredArgsConstructor
@Getter
@ToString
public class StorageObject {
    //TODO Привести в путный вид

    @Deprecated
    private  String path;

    private boolean folder;

    @Setter
    private long size;


    public StorageObject(String path) {
        this.path = path;
    }


    public StorageObject(String parentPath, String name) {

    }


    @Deprecated
    public boolean isFolder() {
        return path.endsWith("/");
    }

    @Deprecated
    public String getParentPath() {
        String[] elements = path.split("/");
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < elements.length - 1; i++) {
            builder.append(elements[i])
                    .append("/");
        }
        return builder.toString();
    }
    public String getName() {
        Path path = Paths.get(this.path);
        return path.getFileName().toString();
    }
}
