package tvs.vote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tvs.vote.repository.Choice;
import tvs.vote.repository.UserVoteEntity;
import tvs.vote.repository.UserVoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserVoteService {

    private final UserVoteRepository userVoteRepository;

    @Autowired
    public UserVoteService(UserVoteRepository userVoteRepository) {
        this.userVoteRepository = userVoteRepository;
    }

    public Flux<UserVoteEntity> findVotesById(Long userId){
        return this.userVoteRepository.findAllByUserId(userId);
    }

    public Mono<Map<String,Integer>> findUserVoteCount(Long userId, Long pollId){
        return Mono.just(1)
                .map(a -> {
                    Map<String, Integer> map = new HashMap<>();

                    for(String choice: List.of(Choice.A, Choice.B)){
                        Mono.just(choice).flatMap(c -> {
                            return this.userVoteRepository.countByUserIdAndPollIdAndChoiceId(userId, pollId, c);
                        }).map(i -> {
                            map.put(Choice.A, i);
                            return 1;
                        });
                    }

                    return map;
                });
    }
}
