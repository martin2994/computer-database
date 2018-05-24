package com.excilys.cdb.mapper;

import java.time.LocalDate;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

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
     * @param computerDTO le dto à convertir
     * @return le computer
     */
    public static Computer toComputer(ComputerDTO computerDTO) {
        long id = 0;
        if (computerDTO.getId() != 0) {
            id = computerDTO.getId();
        }
        LocalDate introducedDate = null;
        if (!computerDTO.getIntroduced().isEmpty()) {
            introducedDate = LocalDate.parse(computerDTO.getIntroduced());
        }
        LocalDate discontinuedDate = null;
        if (!computerDTO.getDiscontinued().isEmpty()) {
            discontinuedDate = LocalDate.parse(computerDTO.getDiscontinued());
        }
        Company company = null;
        if (computerDTO.getManufacturerId() != 0) {
            company = new Company();
            company.setId(computerDTO.getManufacturerId());
        }
        return new Computer.Builder(computerDTO.getName()).id(id).introduced(introducedDate).discontinued(discontinuedDate)
                .manufacturer(company).build();
    }

}
