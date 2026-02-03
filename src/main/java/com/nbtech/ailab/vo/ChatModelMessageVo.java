package com.nbtech.ailab.vo;

import java.util.List;

import com.nbtech.ailab.biz.dto.DialogRecordDto;

import lombok.Data;

@Data
public class ChatModelMessageVo {
    private String role;

    private List<DialogRecordDto> content;
}
