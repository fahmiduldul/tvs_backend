package tvs.vote.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserVoteRepository extends R2dbcRepository<UserVoteEntity, Long> {
    Flux<UserVoteEntity> findAllByUserId(Long userId);
    Mono<Integer> countByUserIdAndPollIdAndChoiceId(Long userId, Long pollId, String choiceId);
}
