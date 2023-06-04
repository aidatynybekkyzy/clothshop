package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "username", target = "username")
    UserDto toDto(User user);

    @Mapping(source = "username", target = "username")
    User toEntity(UserDto userDto);
    List<UserDto> toDtoList(List<User> users);

    void updateEntity(UserDto userDto, @MappingTarget User user);
}
