package ureca.muneobe.common.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class DeleteSlangResponse {

    private List<String> failedList;
}
