package ureca.muneobe.common.mplan.scheduler;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

import jakarta.transaction.Transactional;
import java.util.List;
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

@Component
@RequiredArgsConstructor
public class MplanApplyScheduler {
    private final MplanRepository mplanRepository;
    private final UnapplyMplanRepository unapplyMplanRepository;
    private final FatRepository fatRepository;
    private final FatService fatService;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    @Retryable(value = GlobalException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void applyMplans(){
        List<Mplan> mplans = saveMplan(); //mplans GET API로 보여주기 위해, DB 저장
        saveFats(mplans);   //mplan을 fat테이블에 적용
        fatService.generateAndSaveAllNullEmbeddings();  //fat_embeddings 적용, fat true 적용, 내부 메서드 save까지 명시적으로 되어있음
    }

    private List<Fat> saveFats(List<Mplan> mplans) {
        //mplans를 Fat에 넣기 전 전처리
        List<FatPreVO> fatPreVOs =
                mplans.stream().map(mplan -> FatPreVO.from(mplan, mplan.getMplanDetail(), mplan.getAddonGroup().getAddonGroupName(), false)).toList();
        //fat에 모든 mplan 저장(fat으로 바뀐)
        return fatRepository.saveAll(fatPreVOs.stream().map(fatPreVO -> Fat.of(fatPreVO, false)).toList());
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
