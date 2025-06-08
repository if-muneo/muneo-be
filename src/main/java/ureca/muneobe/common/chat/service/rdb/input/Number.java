package ureca.muneobe.common.chat.service.rdb.input;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Number {
    private Integer baseNumber;
    private Integer subNumber;
    private String operator;
}
