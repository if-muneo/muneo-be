package ureca.muneobe.common.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.mplan.repository.MplanRepository;

@Component
@RequiredArgsConstructor
public class MetaDataFactory {
    private final MplanRepository mplanRepository;

    public MetaData generate(){
        return new MetaData();
    }

}
