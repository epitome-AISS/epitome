package com.nbtech.ailab.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Data
public class SysRoleVo {

	private Long id;

	@ApiModelProperty(value = "角色名称")
	private String name;

    @ApiModelProperty(value = "角色英文名")
    private String englishName;

}