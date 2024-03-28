package top.e_learn.learnEnglish.utils.mapper;

import top.e_learn.learnEnglish.utils.dto.PhraseUserDto;
import top.e_learn.learnEnglish.model.users.PhraseUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PhraseUserMapper extends Mappable<PhraseUser, PhraseUserDto> {

//    PhraseUser toModel();

}
