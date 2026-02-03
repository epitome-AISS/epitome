package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文件上传回显信息")
public class FileInfoDto {
    @ApiModelProperty(value = "素材类型")
    private String materialType;

    @ApiModelProperty(value = "文件url")
    private String url;

}
