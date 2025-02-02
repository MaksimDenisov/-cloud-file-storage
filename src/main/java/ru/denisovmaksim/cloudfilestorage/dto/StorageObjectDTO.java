package ru.denisovmaksim.cloudfilestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class StorageObjectDTO {
    //TODO Хранить родительскую папку
    private final String path;

    private final String name;

    private final StorageObjectType type;

    private final Long size;
}
