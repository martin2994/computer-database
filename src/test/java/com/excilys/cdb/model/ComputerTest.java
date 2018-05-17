package com.excilys.cdb.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class ComputerTest {

    /**
     * Test la création d'un computer.
     */
    @Test
    public void testCreateComputer() {
        Computer computer = new Computer.Builder("test").build();
        assertTrue("test".equals(computer.getName()));
    }

    /**
     * Test la création d'un computer avec un nom null.
     */
    @Test
    public void testCreateComputerNull() {
        Computer computer = new Computer.Builder(null).build();
        assertTrue(computer == null);
    }

}
