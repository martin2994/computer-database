package com.excilys.cdb.mapper;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.dtos.UserDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Role;
import com.excilys.cdb.model.User;

public class DTOMapper {

    /**
     * Convertit un computer en computerDTO.
     * @param computer
     *            le computer à convertir
     * @return le computerDTO résultat
     */
    public static ComputerDTO fromComputer(Computer computer) {
        if (computer != null) {
            return new ComputerDTO(computer);
        }
        return null;
    }

    /**
     * Convertit un computerDTo vers un computer.
     * @param computerDTO
     *            le dto à convertir
     * @return le computer
     * @throws DateTimeException
     *             Exception lancée si la date n'a pas le bon format
     */
    public static Computer toComputer(ComputerDTO computerDTO) throws DateTimeException {
        long id = 0;
        if (computerDTO.getId() != 0) {
            id = computerDTO.getId();
        }
        LocalDate introducedDate = null;
        if (computerDTO.getIntroduced() != null && !computerDTO.getIntroduced().isEmpty()) {
            introducedDate = LocalDate.parse(computerDTO.getIntroduced());
        }
        LocalDate discontinuedDate = null;
        if (computerDTO.getDiscontinued() != null && !computerDTO.getDiscontinued().isEmpty()) {
            discontinuedDate = LocalDate.parse(computerDTO.getDiscontinued());
        }
        Company company = null;
        if (computerDTO.getManufacturerId() != 0) {
            company = new Company();
            company.setId(computerDTO.getManufacturerId());
        }
        return new Computer.Builder(computerDTO.getName()).id(id).introduced(introducedDate)
                .discontinued(discontinuedDate).manufacturer(company).build();
    }
    
    public static User toUser(UserDTO userDTO) {
    	User user = new User();
    	user.setUsername(userDTO.getUsername());
    	user.setPassword(userDTO.getPassword());
    	user.setEnabled(true);
    	Role role = new Role("ROLE_USER", user);
    	List<Role> roles = new ArrayList<>();
    	roles.add(role);
    	user.setAuthorities(roles);
    	return user;
    }
    
    public static UserDTO fromUser(User user) {
    	UserDTO userDTO = new UserDTO();
    	userDTO.setUsername(user.getUsername());
    	return userDTO;
    }

}
