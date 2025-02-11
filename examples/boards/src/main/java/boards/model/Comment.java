package boards.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment extends OpenkodaEntity {
    public Comment(Long organizationId) {
        super(organizationId);
    }

    public Comment() {
        super(null);
    }

    @ManyToOne
    @JoinColumn(name = "board_task_id", insertable = false, updatable = false)
    private BoardTask boardTask;
    @Column(name = "board_task_id")
    private Long boardTaskId;

    @Column(length = 1000)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BoardTask getBoardTask() {
        return boardTask;
    }

    public void setBoardTask(BoardTask boardTask) {
        this.boardTask = boardTask;
    }

    public Long getBoardTaskId() {
        return boardTaskId;
    }

    public void setBoardTaskId(Long boardTaskId) {
        this.boardTaskId = boardTaskId;
    }
}
