package ureca.muneobe.common.mplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.entity.MplanDetail;
import ureca.muneobe.common.mplan.dto.request.MplanCreateRequest;
import ureca.muneobe.common.mplan.dto.response.MplanCreateResponse;
import ureca.muneobe.common.mplan.dto.response.MplansResponse;
import ureca.muneobe.common.mplan.repository.MplanDetailRepository;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

@Service
@RequiredArgsConstructor
public class MplanService {
    private final MplanRepository mplanRepository;
    private final MplanDetailRepository mplanDetailRepository;
    private final AddonGroupRepository addonGroupRepository;

    public MplansResponse findAll(PageRequest pageRequest) {
        return getMplanResponse(pageRequest);
    }

    @Transactional
    public MplanCreateResponse save(MplanCreateRequest mplanCreateRequest, Role role) {
        if(role == Role.USER) throw new GlobalException(ErrorCode.FORBIDDEN);
        Mplan mplan = mplanRepository.save(getMplan(mplanCreateRequest));
        return getMplanCreateResponse(mplan);
    }

    private MplansResponse getMplanResponse(PageRequest pageRequest) {
        Page<Mplan> mplans = mplanRepository.findAll(pageRequest.of(pageRequest.getPageNumber(), 4, Sort.by("id").ascending()));
        return MplansResponse.from(mplans);
    }

    private Mplan getMplan(MplanCreateRequest mplanCreateRequest) {
        MplanDetail mplanDetail = mplanDetailRepository.save(MplanDetail.from(mplanCreateRequest));
        AddonGroup addonGroup = addonGroupRepository.getReferenceById(mplanCreateRequest.getAddonGroupRequest().getId());
        return Mplan.of(mplanCreateRequest, addonGroup, mplanDetail);
    }

    private MplanCreateResponse getMplanCreateResponse(Mplan mplan) {
        return MplanCreateResponse.from(mplan);
    }
}
