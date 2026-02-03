package com.nbtech.ailab.biz.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.nbtech.ailab.biz.dto.AddressTotalDto;
import com.nbtech.ailab.biz.dto.QuestionnaireDataParamDto;
import com.nbtech.ailab.facade.DataCollectionFacade;
import com.nbtech.ailab.facade.ExperimentDataFacade;
import com.nbtech.ailab.handler.ExperimentMessageTask;
import com.nbtech.ailab.vo.ExperimentDataVo;
import com.nbtech.ailab.vo.ExperimentTotalVo;
import com.nbtech.ailab.vo.ExperimentWordNumberVo;
import com.nbtech.ailab.vo.PlanCompletionVo;
import com.nbtech.common.model.BizResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

/**
 * @author nber
 */
@RestController
@RequestMapping("experiment/board")
@Api(tags = "实验组数据看板")
public class ExperimentBoardController {

    @Autowired
    private DataCollectionFacade dataCollectionFacade;

    @Autowired
    private ExperimentDataFacade experimentDataFacade;

    @Autowired
    private ExperimentMessageTask experimentMessageTask;

    @GetMapping("/total/{id}")
    @ApiOperation("实验统计(实验组级别)")
    @RequiresRoles("manager")
    public BizResponse<List<ExperimentTotalVo>> getExperimentTotal(@PathVariable("id") Long id) {
        List<ExperimentTotalVo> list = dataCollectionFacade.getExperimentTotal(id);
        return BizResponse.success(list);
    }

    @GetMapping("/total/plan/{id}")
    @ApiOperation("实验统计(实验计划级别)")
    @RequiresRoles("manager")
    public BizResponse<List<ExperimentTotalVo>> getExperimentTotalByPlanId(@PathVariable("id") Long id) {
        List<ExperimentTotalVo> list = dataCollectionFacade.getExperimentTotalByPlanId(id);
        return BizResponse.success(list);
    }

    @GetMapping("/address/{id}")
    @ApiOperation(("地域分布统计(实验组级别)"))
    @RequiresRoles("manager")
    public BizResponse<?> getAddressTotal(@PathVariable("id") Long id) {
        List<AddressTotalDto> list = dataCollectionFacade.getAddressTotal(id);
        return BizResponse.success(list);
    }

    @GetMapping("/address/plan/{id}")
    @ApiOperation(("地域分布统计(实验计划级别)"))
    @RequiresRoles("manager")
    public BizResponse<?> getAddressTotalByPlanId(@PathVariable("id") Long id) {
        List<AddressTotalDto> list = dataCollectionFacade.getAddressTotalByPlanId(id);
        return BizResponse.success(list);
    }

    @GetMapping("/experimentData")
    @ApiOperation("实验组数据包")
    @RequiresRoles("manager")
    public BizResponse<?> getData(QuestionnaireDataParamDto dto) {
        ExperimentDataVo vo = experimentDataFacade.getExperimentData(dto.getExperimentId(), dto.getGroupsId());
        return BizResponse.success(vo);
    }

    @GetMapping("test")
    @ApiOperation("测试定时任务")
    @RequiresRoles("manager")
    public BizResponse<?> testTask() throws InterruptedException, ParseException {
        experimentMessageTask.MessageTask();
        return BizResponse.success();
    }

    @GetMapping("getIEducation")
    @ApiOperation("智能教育首页数据统计")
    @RequiresRoles("manager")
    public BizResponse<?> getIEducation() {
        return BizResponse.success(dataCollectionFacade.getIEducation());
    }

    @GetMapping("/completion/plan/{id}")
    @ApiOperation("实验计划完成人数统计")
    @RequiresRoles("manager")
    public BizResponse<PlanCompletionVo> getPlanCompletion(@PathVariable("id") Long id) {
        PlanCompletionVo result = dataCollectionFacade.getPlanCompletion(id);
        return BizResponse.success(result);
    }

    @GetMapping("/wordNumber")
    @ApiOperation("导出实验计划模型字数统计Excel")
    public void exportExperimentWordNumber(HttpServletResponse response) throws IOException {
        List<ExperimentWordNumberVo> list = dataCollectionFacade.getExperimentWordNumber();

        // 设置响应头
        String excelName = "实验计划模型字数统计";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 导出Excel
        EasyExcel.write(response.getOutputStream(), ExperimentWordNumberVo.class)
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(30))
                .sheet("实验字数统计")
                .doWrite(list);
    }

}
