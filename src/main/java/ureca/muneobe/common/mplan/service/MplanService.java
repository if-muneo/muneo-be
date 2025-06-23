package ureca.muneobe.common.mplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.addongroup.entity.AddonGroup;
import ureca.muneobe.common.addongroup.repository.AddonGroupRepository;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.common.mplan.dto.request.MplanCreateRequest;
import ureca.muneobe.common.mplan.dto.response.MplanCreateResponse;
import ureca.muneobe.common.mplan.dto.response.MplansResponse;
import ureca.muneobe.common.mplan.dto.response.UnapplyMplanCreateResponse;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.entity.MplanDetail;
import ureca.muneobe.common.mplan.entity.UnapplyMplan;
import ureca.muneobe.common.mplan.repository.MplanDetailRepository;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import ureca.muneobe.common.mplan.repository.UnapplyMplanRepository;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

@Service
@RequiredArgsConstructor
public class MplanService {
    private final UnapplyMplanRepository unapplyMplanRepository;
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

    /**
     * UnapplyMplanRepository에 일단 들어간다. 그 후 MplanApplyScheduler 클래스에서 배치 처리할 것이다.
     */
    @Transactional
    public UnapplyMplanCreateResponse save(MplanCreateRequest mplanCreateRequest, Role role) {
        if(role == Role.USER) throw new GlobalException(ErrorCode.FORBIDDEN);
        UnapplyMplan unapplyMplan = unapplyMplanRepository.save(getUnapplyMplan(mplanCreateRequest));
        return getUnapplyMplanCreateResponse(unapplyMplan);
        //Mplan mplan = mplanRepository.save(getMplan(mplanCreateRequest));
        //return getMplanCreateResponse(mplan);
    }

    private UnapplyMplan getUnapplyMplan(MplanCreateRequest mplanCreateRequest) {
        MplanDetail mplanDetail = mplanDetailRepository.save(MplanDetail.from(mplanCreateRequest));
        AddonGroup addonGroup = addonGroupRepository.getReferenceById(mplanCreateRequest.getAddonGroupRequest().getId());
        return UnapplyMplan.of(mplanCreateRequest, addonGroup, mplanDetail);
    }

    private UnapplyMplanCreateResponse getUnapplyMplanCreateResponse(UnapplyMplan unapplyMplan) {
        return UnapplyMplanCreateResponse.from(unapplyMplan);
    }

    @Deprecated //스케쥴러 배치처리로 인해, deprecated
    private Mplan getMplan(MplanCreateRequest mplanCreateRequest) {
        MplanDetail mplanDetail = mplanDetailRepository.save(MplanDetail.from(mplanCreateRequest));
        AddonGroup addonGroup = addonGroupRepository.getReferenceById(mplanCreateRequest.getAddonGroupRequest().getId());
        return Mplan.of(mplanCreateRequest, addonGroup, mplanDetail);
    }

    @Deprecated //스케쥴러 배치처리로 인해, deprecated
    private MplanCreateResponse getMplanCreateResponse(Mplan mplan) {
        return MplanCreateResponse.from(mplan);
    }
}
