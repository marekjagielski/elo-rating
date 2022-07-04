package com.elorating.service;

import com.elorating.model.Match;
import com.elorating.model.Player;
import com.elorating.repository.MatchRepository;
import com.elorating.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService implements RepositoryService<Player> {

    @Resource
    private PlayerRepository playerRepository;

    @Resource
    private MatchRepository matchRepository;

    @Override
    public Optional<Player> getById(String id) {
        return playerRepository.findById(id);
    }

    @Override
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public List<Player> save(Iterable<Player> players) {
        return playerRepository.saveAll(players);
    }

    @Override
    public void deleteById(String id) {
        playerRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        playerRepository.deleteAll();
    }

    public List<Player> findByLeagueId(String id) {
        return playerRepository.findByLeagueId(id);
    }

    public Long getActivePlayersCountByLeague(String leaugeId) {
        return playerRepository.countByLeagueIdAndActiveIsTrue(leaugeId);
    }

    public List<Player> getRanking(String id, Sort sort) {
        return playerRepository.getRanking(id, sort);
    }

    public List<Player> findByLeagueIdAndUsername(String leagueId, String username) {
        if (username.length() == 2) {
            String regex = buildInitialsRegex(username);
            System.out.println(regex);
            return playerRepository.findByLeagueIdAndUsernameRegex(leagueId, regex);
        } else if (username.length() > 2) {
            return playerRepository.findByLeagueIdAndUsernameLikeIgnoreCase(leagueId, username);
        }
        return new ArrayList<>();
    }

    public List<Player> findActiveByLeagueIdAndUsername(String leagueId, String username) {
        if (username.length() == 2) {
            String regex = buildInitialsRegex(username);
            return playerRepository.findByLeagueIdAndActiveIsTrueAndUsernameRegex(leagueId, regex);
        } else if (username.length() > 2) {
            return playerRepository.findByLeagueIdAndActiveIsTrueAndUsernameLikeIgnoreCase(leagueId, username);
        }
        return new ArrayList<>();
    }

    private String buildInitialsRegex(String username) {
        StringBuilder regex = new StringBuilder("(?i)^");
        String[] split = username.split("");
        regex.append(split[0]).append(".*\\s");
        regex.append(split[1]).append(".*");
        return regex.toString();
    }

    public void restorePlayers(Match match) {
        getById(match.getPlayerOne().getId()).ifPresent(playerOne -> {
            playerOne.restoreRating(match.getRatingDelta(), match.isDraw());
            Date playerLastMatchDate = getPlayerLastMatchDate(playerOne.getId());
            playerOne.getStatistics().setLastMatchDate(playerLastMatchDate);
            playerRepository.save(playerOne);
        });
        getById(match.getPlayerTwo().getId()).ifPresent(playerTwo -> {
            playerTwo.restoreRating(-match.getRatingDelta(), match.isDraw());
            Date playerLastMatchDate = getPlayerLastMatchDate(playerTwo.getId());
            playerTwo.getStatistics().setLastMatchDate(playerLastMatchDate);
            playerRepository.save(playerTwo);
        });
    }

    private Date getPlayerLastMatchDate(String playerId) {
        String dateFieldToSort = "date";
        Sort sort = Sort.by(Sort.Direction.DESC, dateFieldToSort);
        PageRequest pageRequest = PageRequest.of(0, 1, sort);
        Page<Match> page = matchRepository.findCompletedByPlayerId(playerId, pageRequest);
        Optional<Date> date = page.stream().findFirst().map(Match::getDate);
        return date.orElse(null);
    }
}
