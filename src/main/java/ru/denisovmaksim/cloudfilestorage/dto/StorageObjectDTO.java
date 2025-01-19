package ru.denisovmaksim.cloudfilestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StorageObjectDTO {
    private final String path;

    private final String name;

    private final StorageObjectType type;

    private final Long size;
}
