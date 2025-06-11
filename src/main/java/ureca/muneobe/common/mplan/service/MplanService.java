package ureca.muneobe.common.mplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.chat.entity.AddonGroup;
import ureca.muneobe.common.chat.entity.Mplan;
import ureca.muneobe.common.chat.entity.MplanDetail;
import ureca.muneobe.common.mplan.dto.*;
import ureca.muneobe.common.mplan.repository.MplanDetailRepository;
import ureca.muneobe.common.mplan.repository.MplanRepository;

@Service
@RequiredArgsConstructor
public class MplanService {
    private final MplanRepository mplanRepository;
    private final MplanDetailRepository mplanDetailRepository;
    private final AddonGroupRepository addonGroupRepository;

    public MplansResponse findAll(PageRequest pageRequest) {
        return getMplanResponse(pageRequest);
    }

    private MplansResponse getMplanResponse(PageRequest pageRequest) {
        Page<Mplan> mplans = mplanRepository.findAll(pageRequest);
        return MplansResponse.from(mplans);
    }

    @Transactional
    public MplanCreateResponse save(MplanCreateRequest mplanCreateRequest) {
        Mplan mplan = mplanRepository.save(getMplan(mplanCreateRequest));
        return getMplanCreateResponse(mplan);
    }

    private Mplan getMplan(MplanCreateRequest mplanCreateRequest) {
        MplanDetail mplanDetailProxy = mplanDetailRepository.getReferenceById(mplanCreateRequest.getMplanDetailId());
        AddonGroup addonGroupProxy = addonGroupRepository.getReferenceById(mplanCreateRequest.getAddonGroupId());
        return Mplan.of(mplanCreateRequest, addonGroupProxy, mplanDetailProxy);
    }

    private MplanCreateResponse getMplanCreateResponse(Mplan mplan) {
        return MplanCreateResponse.from(mplan);
    }
}
