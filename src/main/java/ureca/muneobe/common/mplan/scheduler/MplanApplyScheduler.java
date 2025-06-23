package ureca.muneobe.common.mplan.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.mplan.entity.Mplan;
import ureca.muneobe.common.mplan.entity.UnapplyMplan;
import ureca.muneobe.common.mplan.repository.MplanRepository;
import ureca.muneobe.common.mplan.repository.UnapplyMplanRepository;
import ureca.muneobe.common.vector.entity.Fat;
import ureca.muneobe.common.vector.repository.FatRepository;
import ureca.muneobe.common.vector.service.FatService;
import ureca.muneobe.global.exception.GlobalException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MplanApplyScheduler {
    private final MplanRepository mplanRepository;
    private final UnapplyMplanRepository unapplyMplanRepository;
    private final FatRepository fatRepository;
    private final FatService fatService;
    private final DescriptionGenerator descriptionGenerator;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    @Retryable(value = GlobalException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void applyMplans(){
        List<Mplan> mplans = saveMplan(); //mplans GET API로 보여주기 위해, DB 저장
        saveFats(mplans);   //mplan을 fat테이블에 적용
        fatService.generateAndSaveAllNullEmbeddings();  //fat_embeddings 적용, fat true 적용, 내부 메서드 save까지 명시적으로 되어있음
    }

    private List<Fat> saveFats(List<Mplan> mplans) {
        List<FatPreVO> fatPreVOs =  //mplans를 Fat에 넣기 전 전처리
                mplans.stream().map(mplan -> FatPreVO.of(mplan, mplan.getMplanDetail(), mplan.getAddonGroup().getAddonGroupName(), descriptionGenerator.generate(mplan), false)).toList();
        return fatRepository.saveAll(fatPreVOs.stream().map(fatPreVO -> Fat.of(fatPreVO, false)).toList()); //fat에 모든 mplan 저장(fat으로 바뀐)
    }

    private List<Mplan> saveMplan() {
        List<UnapplyMplan> unapplyMplans = unapplyMplanRepository.findAll();
        List<Mplan> mplans = getMplans(unapplyMplans);
        mplanRepository.saveAll(mplans);
        return mplans;
    }

    private List<Mplan> getMplans(List<UnapplyMplan> unapplyMplans) {
        return unapplyMplans.stream().map(Mplan::from).toList();
    }
}
