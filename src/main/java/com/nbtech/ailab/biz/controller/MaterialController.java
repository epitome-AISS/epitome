package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.dto.FlowDto;
import com.nbtech.ailab.biz.dto.GroupsDto;
import com.nbtech.ailab.biz.dto.MaterialDto;
import com.nbtech.ailab.biz.dto.ReviewTestDto;
import com.nbtech.ailab.biz.service.IMaterialService;
import com.nbtech.ailab.facade.MaterialFacade;
import com.nbtech.ailab.vo.MaterialTypeListVo;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 素材管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-07
 */
@RestController
@RequestMapping("material")
@Api(tags = "素材管理")
public class MaterialController {
    @Autowired
    private IMaterialService materialService;

    @Autowired
    private MaterialFacade materialFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    @RequiresRoles("manager")
    public BizResponse<PageResult<MaterialDto>> page(PageDto pageDto, MaterialDto dto) {
        PageResult<MaterialDto> page = materialService.pageMaterial(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("pagePublic")
    @ApiOperation("分页查询开源的素材")
    @RequiresRoles("manager")
    public BizResponse<PageResult<MaterialDto>> pagePublic(PageDto pageDto, MaterialDto dto) {
        PageResult<MaterialDto> page = materialService.pagePublic(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("auditPage")
    @ApiOperation("审核者素材分页")
    @RequiresRoles("manager")
    public BizResponse<?> auditPage(PageDto pageDto, MaterialDto dto) {
        PageResult<MaterialDto> page = materialService.pageAudit(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiresRoles("manager")
    public BizResponse<MaterialTypeListVo> list(MaterialDto dto) {
        return BizResponse.success(materialService.getMaterialList(dto));
    }

    @GetMapping("listAll")
    @ApiOperation("全部素材")
    @RequiresRoles("manager")
    public BizResponse<?> listAll(MaterialDto dto) {
        List<MaterialDto> list = materialService.listAll(dto);
        return BizResponse.success(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresRoles("manager")
    public BizResponse<MaterialDto> get(@PathVariable("id") Long id) {
        MaterialDto data = materialService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping("save")
    @ApiOperation("新建素材 图片/音频/视频")
    @RequiresRoles("manager")
    public BizResponse<?> save(@RequestBody MaterialDto material) throws Exception {
        materialFacade.saveMaterial(material);
        return BizResponse.success();
    }

    @PostMapping("file")
    @ApiOperation("上传素材")
    @RequiresRoles("manager")
    public BizResponse<?> saveFile(MultipartFile file) throws Exception {
        return BizResponse.success(materialFacade.uploadFile(file));
    }

    @PostMapping("saveQuestionFile")
    @ApiOperation("上传问卷附件的文件")
    public BizResponse<?> saveQuestionFile(MultipartFile file) throws Exception {
        return BizResponse.success(materialFacade.uploadQuestionnaireFile(file));
    }

    @PostMapping("deleteByUrl")
    @ApiOperation("根据图片url删除图片")
    public BizResponse<?> deleteByUrl(@RequestBody List<String> urls) {
        materialFacade.deleteByUrl(urls);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @RequiresRoles("manager")
    public BizResponse<?> update(@RequestBody MaterialDto dto) {
        materialService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除素材")
    @RequiresRoles("manager")
    public BizResponse<?> delete(@PathVariable("id") Long id) {
        materialFacade.deleteMaterial(id);
        return BizResponse.success();
    }

    @PostMapping("/flow")
    @ApiOperation("素材状态流程操作")
    @RequiresRoles("manager")
    public BizResponse<?> flow(@RequestBody FlowDto dto) {
        materialFacade.flow(dto);
        return BizResponse.success();
    }

    @GetMapping("/copyMaterial")
    @ApiOperation("素材复制按钮")
    @RequiresRoles("manager")
    public BizResponse<?> flow(@RequestParam("id") Long id) {
        materialFacade.copyMaterial(id, null);
        return BizResponse.success();
    }

    @PostMapping("/saveText")
    @ApiOperation("操作文本 新建/修改")
    @RequiresRoles("manager")
    public BizResponse<?> operateText(@RequestBody MaterialDto dto) {
        materialFacade.operateText(dto);
        return BizResponse.success();
    }

    @PostMapping("/review")
    @ApiOperation("审核")
    @RequiresRoles("manager")
    public BizResponse<?> review(@RequestBody ReviewTestDto dto) {
        materialFacade.review(dto);
        return BizResponse.success();
    }

    @GetMapping("/openList")
    @ApiOperation("开源素材列表")
    public BizResponse<?> openList(String materialType) {
        List<MaterialDto> list = materialService.openList(materialType);
        return BizResponse.success(list);
    }

    @GetMapping("/getMaterialTags")
    @ApiOperation("查询这个类型的素材的全部标签")
    public BizResponse<?> getMaterialTags(@RequestParam("materialType") String materialType) {
        return BizResponse.success(materialService.getMaterialTags(materialType));
    }

    @GetMapping("/preview")
    @ApiOperation("minio预览接口")
    public BizResponse<?> preview(@RequestParam("id") Long id) throws Exception {
        return BizResponse.success(materialFacade.preview(id));
    }

    @GetMapping("/listMyPrivateMaterials")
    @ApiOperation("查询当前用户自己的未开源的素材列表")
    @RequiresRoles("manager")
    public BizResponse<?> listMyPrivateMaterials(
            @ApiParam(value = "素材名称（可选，用于模糊查询）") @RequestParam(required = false) String materialName,
            @ApiParam(value = "素材类型（可选，用于精确筛选）") String materialType) {
        List<MaterialDto> list = materialService.listMyPrivateMaterials(materialName, materialType);
        return BizResponse.success(list);
    }

}