package ureca.muneobe.common.mplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.common.chat.entity.MplanDetail;
import ureca.muneobe.common.mplan.dto.request.MplanDetailCreateRequest;
import ureca.muneobe.common.mplan.dto.response.MplanDetailCreateResponse;
import ureca.muneobe.common.mplan.dto.response.MplanDetailResponse;
import ureca.muneobe.common.mplan.dto.response.MplanDetailsResponse;
import ureca.muneobe.common.mplan.repository.MplanDetailRepository;

@Service
@RequiredArgsConstructor
public class MplanDetailService {
    private final MplanDetailRepository mplanDetailRepository;

    public MplanDetailsResponse findAll(PageRequest pageRequest) {
        return getMplanDetailsResponse(pageRequest);
    }

    @Transactional
    public MplanDetailCreateResponse save(MplanDetailCreateRequest mplanDetailCreateRequest, Role role) {
        MplanDetail mplanDetail = mplanDetailRepository.save(getMplanDetail(mplanDetailCreateRequest));
        return getMplanDetailCreateResponse(mplanDetail);
    }

    private MplanDetailsResponse getMplanDetailsResponse(PageRequest pageRequest) {
        Page<MplanDetail> mplanDetails = mplanDetailRepository.findAll(pageRequest);
        return MplanDetailsResponse.builder()
                .mplanDetailsResponse(mplanDetails.map(MplanDetailResponse::from))
                .build();
    }

    private MplanDetail getMplanDetail(MplanDetailCreateRequest mplanDetailCreateRequest) {
        return MplanDetail.from(mplanDetailCreateRequest);
    }

    private MplanDetailCreateResponse getMplanDetailCreateResponse(MplanDetail mplanDetail) {
        return MplanDetailCreateResponse.from(mplanDetail);
    }
}
