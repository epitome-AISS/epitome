package com.nbtech.ailab.vo;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author nber
 */
@Data
public class AnswerVo implements Comparable<AnswerVo>{
    //答案顺序
    public Integer answerSort;

    //答案内容
    public String answerContext;

    //选项信息
    public List<ChoiceVo> choices;

    //量表信息
    public ScaleVo scale;

    //开始时间
    public LocalDateTime startTime;

    // 结束时间
    public LocalDateTime endTime;

    // id
    public Long id;


    @Override
    public int compareTo(@NotNull AnswerVo o) {
        return Integer.compare(this.answerSort, o.getAnswerSort());
    }
}
