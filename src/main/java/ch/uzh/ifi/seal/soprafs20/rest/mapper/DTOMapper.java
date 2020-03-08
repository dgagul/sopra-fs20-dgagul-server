package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserTokenDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    // POST
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    // GET
    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "creationDate", target = "creationDate")
    UserGetDTO convertEntityToUserGetDTO(User user);

    //Token
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "token", target = "token")
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    User convertUserTokenDTOtoEntity(UserTokenDTO userTokenDTO);

    //Edit
    @Mapping(source = "id", target = "id")
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "username", target = "username")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(target = "creationDate", ignore = true)
    User convertUserEditDTOtoEntity(UserEditDTO userEditDTO);
}