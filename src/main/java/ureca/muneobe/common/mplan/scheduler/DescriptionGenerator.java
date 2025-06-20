package ureca.muneobe.common.mplan.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ureca.muneobe.common.addon.entity.Addon;
import ureca.muneobe.common.mplan.entity.Mplan;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DescriptionGenerator {
    public String generate(Mplan mplan){
        String mplanName = mplan.getName();
        List<Addon> addons = mplan.getAddonGroup().getAddons();
        return getTotalDescription(mplanName, addons);
    }

    private String getTotalDescription(String mplanName, List<Addon> addons){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < addons.size(); i++){
            stringBuilder.append(getOnceDescription(mplanName, addons.get(i)));
            if(i != addons.size()) stringBuilder.append("$");
        }
        return stringBuilder.toString();
    }

    public String getOnceDescription(String mplanName, Addon addon){
        return mplanName + addon.getDescription();
    }
}