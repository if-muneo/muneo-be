package ureca.muneobe.slang.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoveSlangRequest {

    private List<String> words;
}
