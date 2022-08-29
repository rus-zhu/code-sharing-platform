package platform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "snippet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Code {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private UUID id;
    private String code;
    private LocalDateTime date;
    private Long time;
    private Long views;
    @JsonIgnore
    private boolean isTimeLimit;
    @JsonIgnore
    private boolean isViewsLimit;

    public Code(String code) {
        this.code = code;
        this.date = LocalDateTime.now();
        this.time = 0L;
        this.views = 0L;
        this.isTimeLimit = false;
        this.isViewsLimit = false;
    }
    public Code(String code, Long time, Long views) {
        this.code = code;
        this.date = LocalDateTime.now();
        this.time = 0L;
        this.views = 0L;
    }

}
