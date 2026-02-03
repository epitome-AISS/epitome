package com.nbtech.ailab.util;

import com.nbtech.ailab.vo.PyParamVo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nber
 */
@Slf4j
public class UsePyUtil {

    /**
     * 生成词云图片并获取路径
     *
     * @param pyParamVo v
     * @throws Exception
     */
    public static void createImage(PyParamVo pyParamVo) throws Exception {
        log.info("生成词云开始, 实验计划id={}, 实验组id={}, 问卷id={}, 题目序号={}, 目标文本长度={}",
                pyParamVo.getExperimentId(), pyParamVo.getGroupId(), pyParamVo.getQuestionnaireId(),
                pyParamVo.getQuestionSort(), pyParamVo.getTarget() != null ? pyParamVo.getTarget().length() : 0);
        String fixedStr = "/home/ecs-user/miniconda3/envs/epitome/bin/python gen_word_cloud.py -t ";
        String fullCmd = fixedStr + "\"" + pyParamVo.getTarget() + "\"" + " -e " + pyParamVo.getExperimentId() + " -g "
                + pyParamVo.getGroupId() + " -l " + pyParamVo.getQuestionnaireId() + " -u "
                + pyParamVo.getQuestionSort();
        String result = CommandInvoker.cmd(fullCmd);
        log.info("生成词云完成, 命令执行结果: {}", result);
    }

    /**
     * 获取词云的图片路径
     * 
     * @param vo 参数
     * @return
     */

    // 获取生成的词云图片路径
    public static String getUrl(PyParamVo vo) {
        // http://61.169.23.150:9000/experiment7/10/2/210.jpg
        // http://61.169.23.150:9000 + experiment + 实验id + 实验组id + 算子id + 用户字段id + .jpg
        return vo.getMinioPath() + "/experiment" + vo.getExperimentId() + "/" + vo.getGroupId() + "/"
                + vo.getQuestionnaireId() + "/" + vo.getQuestionSort() + ".jpg";
    }
}
