package com.elorating.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MatchTest {

    private Player playerOne;
    private Player playerTwo;
    private Match match;

    @BeforeEach
    public void setUp() throws Exception {
        playerOne = new Player("Player one");
        playerOne.setId("111");
        playerTwo = new Player("Player two");
        playerTwo.setId("222");
        match = new Match(playerOne, playerTwo);
    }

    @Test
    public void testIsCompletedIsFalse() throws Exception {
        Assertions.assertFalse(match.isCompleted());
    }

    @Test
    public void testIsCompletedIsTrueFirst() throws Exception {
        match.setScore(playerOne, 0);
        match.setScore(playerTwo, 2);
        Assertions.assertTrue(match.isCompleted());
    }

    @Test
    public void testIsCompletedIsTrueSecond() throws Exception {
        match.setScore(playerOne, 2);
        match.setScore(playerTwo, 0);
        Assertions.assertTrue(match.isCompleted());
    }

    @Test
    public void testIsCompleteInvalidScore() throws Exception {
        match.setScore(playerOne, 3);
        Assertions.assertFalse(match.isCompleted());
    }

    @Test
    public void testGetWinnerIdPlayerOne() {
        match.setScore(playerOne, 2);
        match.setScore(playerTwo, 1);
        Assertions.assertEquals(playerOne.getId(), match.getWinnerId());
    }

    @Test
    public void testGetWinnerIdPlayerTwo() {
        match.setScore(playerOne, 0);
        match.setScore(playerTwo, 2);
        Assertions.assertEquals(playerTwo.getId(), match.getWinnerId());
    }

    @Test
    public void testGetWinnerIdNull() {
        Assertions.assertNull(match.getWinnerId());
    }

    @Test
    public void testGetLooserIdPlayerOne() {
        match.setScore(playerOne, 1);
        match.setScore(playerTwo, 2);
        Assertions.assertEquals(playerOne.getId(), match.getLooserId());
    }

    @Test
    public void testGetLooserIdPlayerTwo() {
        match.setScore(playerOne, 2);
        match.setScore(playerTwo, 0);
        Assertions.assertEquals(playerTwo.getId(), match.getLooserId());
    }

    @Test
    public void testIsDrawIsTrue() {
        match.setScore(playerOne, 3);
        match.setScore(playerTwo, 3);
        Assertions.assertTrue(match.isDraw());
    }

    @Test
    public void testIsDrawIsFalse() {
        match.setScore(playerOne, 2);
        match.setScore(playerTwo, 4);
        Assertions.assertFalse(match.isDraw());
    }

    @Test
    public void testGetLooserIdNull() {
        Assertions.assertNull(match.getLooserId());
    }
}