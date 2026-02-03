package com.nbtech.ailab.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IEducation {

    private Integer teacherNumber;

    private Integer studentNumber;

    private Integer publicClassNumber;

    private Integer OpenSourceAids;
}
