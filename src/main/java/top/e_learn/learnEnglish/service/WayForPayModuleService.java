package top.e_learn.learnEnglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.WayForPayModule;
import top.e_learn.learnEnglish.repository.WayForPayModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Буде змінюватись
@Service
@RequiredArgsConstructor
public class WayForPayModuleService {

    private final WayForPayModuleRepository wayForPayModuleRepository;

    public void saveWayForPayModule(WayForPayModule wayForPayModule) {
        if(wayForPayModule.getId() != null) {
            Optional<WayForPayModule> wayForPayModuleOptional = wayForPayModuleRepository.findById(wayForPayModule.getId());
            if(wayForPayModuleOptional.isPresent()) {
            WayForPayModule wayForPayModuleFromDataBase = wayForPayModuleOptional.get();
            wayForPayModuleFromDataBase.setActive(wayForPayModule.isActive());
            wayForPayModuleFromDataBase.setMerchantAccount(wayForPayModule.getMerchantAccount());
            wayForPayModuleFromDataBase.setMerchantSecretKey(wayForPayModule.getMerchantSecretKey());
            wayForPayModuleRepository.save(wayForPayModuleFromDataBase);
            } else throw new RuntimeException("method 'saveWayForPayModule' exception");
        } else {
            wayForPayModuleRepository.save(wayForPayModule);
        }
    }

    public WayForPayModule getWayForPayModule() {
        Optional<WayForPayModule> wayForPayModuleOptional = wayForPayModuleRepository.findById(1l);
        if (wayForPayModuleOptional.isPresent()) {
            return wayForPayModuleOptional.get();
        } else return new WayForPayModule();
    }

}
