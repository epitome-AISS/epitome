package com.nbtech.ailab.vo;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author nber
 */
@Data
public class QuestionDataVo implements Comparable<QuestionDataVo> {

    Integer questionSort;

    String questionName;

    String questionType;

    List<ChoiceVo> choices;

    List<ScaleVo> scales;

    Integer isMust;

    Long id;

    String conditions;

    @Override
    public int compareTo(@NotNull QuestionDataVo o) {
        return Integer.compare(this.questionSort, o.getQuestionSort());
    }
}
