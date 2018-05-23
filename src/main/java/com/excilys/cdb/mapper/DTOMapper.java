package com.excilys.cdb.mapper;

import com.excilys.cdb.dtos.ComputerDTO;
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

}
