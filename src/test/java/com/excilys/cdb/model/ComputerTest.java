package com.excilys.cdb.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComputerTest {

    /**
     * Test la création d'un computer.
     */
    @Test
    public void testCreateComputer() {
        Computer computer = new Computer.Builder("test").build();
        assertTrue("test".equals(computer.getName()));
    }

}
