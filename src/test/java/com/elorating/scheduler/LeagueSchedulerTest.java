package com.elorating.scheduler;

import com.elorating.CoreApplication;
import com.elorating.model.League;
import com.elorating.model.Match;
import com.elorating.model.Player;
import com.elorating.model.User;
import com.elorating.repository.LeagueRepository;
import com.elorating.repository.MatchRepository;
import com.elorating.repository.PlayerRepository;
import com.elorating.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CoreApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
public class LeagueSchedulerTest {

    private static final int RETRIES = 3;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeagueScheduler leagueScheduler;

    @BeforeEach
    public void setUp() throws Exception {
        League leagueToRemove = leagueRepository.save(new League("League to delete"));
        League assignedLeague = addAssignedLeague();
        for (int i = 0; i < RETRIES; i++) {
            Player playerOne = playerRepository.save(new Player("Player 1" + i, leagueToRemove));
            Player playerTwo = playerRepository.save(new Player("Player 2" + i, leagueToRemove));
            matchRepository.save(new Match(playerOne, playerTwo, 2, 1, leagueToRemove));
            playerOne = playerRepository.save(new Player("Player 1" + i, assignedLeague));
            playerTwo = playerRepository.save(new Player("Player 2" + i, assignedLeague));
            matchRepository.save(new Match(playerOne, playerTwo, 2, 1, assignedLeague));
        }
    }

    private League addAssignedLeague() {
        League otherLeague = new League("Other league");
        User user = new User("Test user");
        user.addLeague(otherLeague);
        user = userRepository.save(user);
        otherLeague.addUser(user);
        return leagueRepository.save(otherLeague);
    }

    @AfterEach
    public void tearDown() throws Exception {
        matchRepository.deleteAll();
        playerRepository.deleteAll();
        leagueRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testRemoveUnassignedLeagues() throws Exception {
        leagueScheduler.removeUnassignedLeagues();
        List matches = matchRepository.findAll();
        List players = playerRepository.findAll();
        List leagues = leagueRepository.findAll();
        Assertions.assertEquals(matches.size(), RETRIES);
        Assertions.assertEquals(players.size(), RETRIES * 2);
        Assertions.assertEquals(leagues.size(), 1);
    }

}