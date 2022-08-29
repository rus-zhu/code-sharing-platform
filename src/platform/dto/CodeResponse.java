package platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeResponse {
    private String code;
    private LocalDateTime date;
    private Long time;
    private Long views;
}
