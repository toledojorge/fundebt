/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.user.rest;

import java.util.List;

import org.mapstruct.Mapper;

import dev.carlosmolero.fundebt.domain.user.UserEntity;
import dev.carlosmolero.fundebt.domain.user.rest.data.response.UserResponse;
import dev.carlosmolero.fundebt.domain.user.rest.data.response.UserResponseMin;

@Mapper
public interface UserRestMapper {
    List<UserResponse> fromUserEntityListToUserResponseList(List<UserEntity> users);

    List<UserResponseMin> fromUserEntityListToUserResponseMinList(List<UserEntity> users);

    UserResponse fromUserEntityToUserResponse(UserEntity user);

    UserResponseMin fromUserEntityToUserResponseMin(UserEntity user);
}
