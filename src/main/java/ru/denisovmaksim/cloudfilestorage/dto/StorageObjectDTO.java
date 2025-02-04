package ru.denisovmaksim.cloudfilestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class StorageObjectDTO {
    //TODO Хранить родительскую папку
    private final String parentPath;

    private final String name;

    private final StorageObjectType type;

    private final Long size;
    public String getPath() {
        return parentPath + name;
    }
}
