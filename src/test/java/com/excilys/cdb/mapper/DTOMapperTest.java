package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.model.Computer;

public class DTOMapperTest {

    /**
     * Teste le cas normal de la fonction ConvertComputerToComputerDTO.
     */
    @Test
    public void testConvertComputerToComputerDTO() {
        ComputerDTO computerDTO = new ComputerDTO();
        computerDTO.setId(1);
        computerDTO.setName("test");
        ComputerDTO computerResult = DTOMapper.convertComputerToComputerDTO(new Computer.Builder("test").id(1L).build());
        assertEquals(computerDTO, computerResult);
    }

    /**
     * Teste la fonction ConvertComputerToComputerDTO quand l'objet est null.
     */
    @Test
    public void testConvertComputerToComputerDTONull() {
        assertNull(DTOMapper.convertComputerToComputerDTO(null));
    }
}
