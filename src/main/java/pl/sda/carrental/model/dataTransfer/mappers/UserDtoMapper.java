package pl.sda.carrental.model.dataTransfer.mappers;

import pl.sda.carrental.model.entity.userEntities.User;

public interface UserDtoMapper<T extends User, W> {
    T getUserClass(W dto);
    W getDto(T userClass) ;
}
