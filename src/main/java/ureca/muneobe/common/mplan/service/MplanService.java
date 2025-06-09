package ureca.muneobe.common.mplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.common.chat.entity.Mplan;
import ureca.muneobe.common.mplan.dto.MplanRequest;
import ureca.muneobe.common.mplan.dto.MplanResponse;
import ureca.muneobe.common.mplan.dto.MplansRequest;
import ureca.muneobe.common.mplan.dto.SimpMplansResponse;
import ureca.muneobe.common.mplan.dto.SimpMplanResponse;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import ureca.muneobe.global.exception.GlobalException;
import ureca.muneobe.global.response.ErrorCode;

@Service
@RequiredArgsConstructor
public class MplanService {
    private final MplanRepository mplanRepository;

    public SimpMplansResponse findAll(PageRequest pageRequest) {
        return getSimpMplansResponse(pageRequest);
    }

    public MplanResponse findById(Long mplanId) {
        return getMplanResponse(mplanId);
    }

    public void saveMplan(MplanRequest mplanRequest, Role role) {
        if(!isAdmin(role)) throw new GlobalException(ErrorCode.FORBIDDEN);
        mplanRepository.save();
    }

    public Void updateMplan(MplanRequest mplanRequest, Role role) {
        if(!isAdmin(role)) throw new GlobalException(ErrorCode.FORBIDDEN);
    }

    public Void deleteMplanById(Long mplanId, Role role) {
        if(!isAdmin(role)) throw new GlobalException(ErrorCode.FORBIDDEN);
    }

    private SimpMplansResponse getSimpMplansResponse(PageRequest pageRequest) {
        Page<Mplan> mplans = mplanRepository.findAll(pageRequest);
        Page<SimpMplanResponse> simpMplansResponse = mplans.map(SimpMplanResponse::from);
        return SimpMplansResponse.from(simpMplansResponse);
    }

    private MplanResponse getMplanResponse(Long mplanId) {
        Mplan mplan = mplanRepository.findById(mplanId)
                .orElseThrow(()-> new GlobalException(ErrorCode.NO_MPLAN));

    }

    private Boolean isAdmin(Role role) {
        return role.equals(Role.ADMIN);
    }
}
