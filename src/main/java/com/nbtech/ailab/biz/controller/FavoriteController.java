package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.FavoriteDto;
import com.nbtech.ailab.biz.dto.FavoritePageDto;
import com.nbtech.ailab.biz.dto.MaterialFavoritePageDto;
import com.nbtech.ailab.biz.dto.ModelFavoritePageDto;
import com.nbtech.ailab.biz.dto.QuestionnaireFavoritePageDto;
import com.nbtech.ailab.biz.service.IFavoriteService;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@RestController
@RequestMapping("/favorite")
@Api(tags = "收藏表")
public class FavoriteController {

    @Autowired
    private IFavoriteService favoriteService;

    @GetMapping("page/experimentPlan")
    @ApiOperation("分页查询当前用户收藏的实验计划")
    @RequiresRoles("manager")
    public BizResponse<PageResult<FavoritePageDto>> page(
            PageDto pageDto,
            @ApiParam(value = "实验名称（可选，用于模糊查询）") @RequestParam(required = false) String experimentName,
            @ApiParam(value = "实验领域（可选，用于模糊查询）") @RequestParam(required = false) String experimentField) {
        PageResult<FavoritePageDto> page = favoriteService
                .pageFavorite(pageDto, experimentName, experimentField);
        return BizResponse.success(page);
    }

    @GetMapping("page/material")
    @ApiOperation("分页查询当前用户收藏的素材")
    @RequiresRoles("manager")
    public BizResponse<PageResult<MaterialFavoritePageDto>> pageMaterial(
            PageDto pageDto,
            @ApiParam(value = "素材类型（可选，用于筛选）") @RequestParam(required = false) String materialType,
            @ApiParam(value = "素材名称（可选，用于模糊查询）") @RequestParam(required = false) String materialName) {
        PageResult<MaterialFavoritePageDto> page = favoriteService
                .pageFavoriteMaterial(pageDto, materialType, materialName);
        return BizResponse.success(page);
    }

    @GetMapping("page/model")
    @ApiOperation("分页查询当前用户收藏的模型")
    @RequiresRoles("manager")
    public BizResponse<PageResult<ModelFavoritePageDto>> pageModel(
            PageDto pageDto,
            @ApiParam(value = "模型名称（可选，用于模糊查询）") @RequestParam(required = false) String modelName) {
        PageResult<ModelFavoritePageDto> page = favoriteService
                .pageFavoriteModel(pageDto, modelName);
        return BizResponse.success(page);
    }

    @GetMapping("page/questionnaire")
    @ApiOperation("分页查询当前用户收藏的问卷")
    @RequiresRoles("manager")
    public BizResponse<PageResult<QuestionnaireFavoritePageDto>> pageQuestionnaire(
            PageDto pageDto,
            @ApiParam(value = "问卷名称（可选，用于模糊查询）") @RequestParam(required = false) String questionnaireName) {
        PageResult<QuestionnaireFavoritePageDto> page = favoriteService
                .pageFavoriteQuestionnaire(pageDto, questionnaireName);
        return BizResponse.success(page);
    }

    @PostMapping
    @ApiOperation("新增")
    @RequiresRoles("manager")
    @LogOperation("新增收藏")
    public BizResponse<FavoriteDto> save(@RequestBody FavoriteDto favoriteDto) {
        FavoriteDto dto = favoriteService.saveFavorite(favoriteDto.getFavoriteType(),
                favoriteDto.getTargetId());
        return BizResponse.success(dto);
    }

    @DeleteMapping
    @ApiOperation("删除")
    @RequiresRoles("manager")
    @LogOperation("删除收藏")
    public BizResponse<?> delete(@RequestBody FavoriteDto favoriteDto) {
        favoriteService.deleteByTargetId(favoriteDto.getFavoriteType(), favoriteDto.getTargetId());
        return BizResponse.success();
    }

}
