package ureca.muneobe.common.chat.service.strategy.rdb.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Range {
    private Integer baseNumber;
    private Integer subNumber;
    private String operator;
}
