package com.nbtech.ailab.vo;

import com.nbtech.common.model.PageDto;
import lombok.Data;

@Data
public class PageGroupVo {
    // 分页
    PageDto pageDto;

    // 实验组id集合
    String[] ids;
}
