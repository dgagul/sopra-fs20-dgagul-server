package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserTokenDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void test_convertUserPostDTOtoEntity() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("password");
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setPassword("password123");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void convertUserTokenDTOtoEntity_success() {
        // create User
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken("password123");

        // MAP -> Create UserGetDTO
        User user = DTOMapper.INSTANCE.convertUserTokenDTOtoEntity(userTokenDTO);

        // check content
        assertEquals(userTokenDTO.getToken(), user.getToken());
    }

    @Test
    public void convertUserEditDTOtoEntity_success() {
        // create UserEditDTO
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setId(1L);
        userEditDTO.setBirthday("2020-03-06");
        userEditDTO.setUsername("newUsername");

        // MAP -> Create User
        User user = DTOMapper.INSTANCE.convertUserEditDTOtoEntity(userEditDTO);

        // check content
        assertEquals(user.getId(), userEditDTO.getId());
        assertEquals(user.getUsername(), userEditDTO.getUsername());
        assertEquals(user.getBirthday(), userEditDTO.getBirthday());
    }
}
