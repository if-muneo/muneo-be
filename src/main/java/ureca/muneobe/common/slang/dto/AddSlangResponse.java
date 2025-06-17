package ureca.muneobe.common.slang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class AddSlangResponse {

    private List<String> failedList;
}
