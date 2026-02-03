package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

@Data
public class PyConfigVo {

    private String pythonCode;

    private String input;

    private String name;

    private List<PyOutPutVo> output;

}
