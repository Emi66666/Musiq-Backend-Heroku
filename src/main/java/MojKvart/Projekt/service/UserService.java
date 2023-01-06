package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.rest.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

	User createUser(UserDTO user);
	
	Optional<User> findByEmail(String email);
	
	User loginUser(String email, String password);

	User changeIntoUser(UserDTO user);
 
	Optional<User> findByUserId(Long userId);
	
	User changeUserAddress(UserDTO user);
	
	UserDTO changeIntoUserDTO(User user);
	
	List<User> getAllUsers();
	
	User changeRole(Long userId, int newRole);
	
	User banUser(Long userId);

	User unbanUser(Long userId);
	
	void deleteUser(Long userId);

	int getRoleFromUserId(Long userId);
	
	List<UserDTO> sortUsers(List<UserDTO> list);
}
