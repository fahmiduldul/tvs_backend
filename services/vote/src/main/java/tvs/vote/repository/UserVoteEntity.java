package tvs.vote.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("user_votes")
public class UserVoteEntity {
    @Id
    @Column("id")
    private long id;

    @Column("user_id")
    private long userId;

    @Column("poll_id")
    private long pollId;

    @Column("choice_id")
    private String choiceId;
}
