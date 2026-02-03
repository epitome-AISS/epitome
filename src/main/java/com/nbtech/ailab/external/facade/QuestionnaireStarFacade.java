package com.nbtech.ailab.external.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nbtech.ailab.biz.dao.QuestionStarDao;
import com.nbtech.ailab.biz.dao.QuestionStarDataDao;
import com.nbtech.ailab.biz.entity.QuestionStarDataEntity;
import com.nbtech.ailab.biz.entity.QuestionStarEntity;
import com.nbtech.ailab.common.QuStartEnum;
import com.nbtech.ailab.common.RedisHeadEnum;
import com.nbtech.ailab.external.vo.QnDataParamVo;
import com.nbtech.ailab.external.vo.QnListParamVo;
import com.nbtech.ailab.external.vo.QnStartLoginParamVo;
import com.nbtech.ailab.external.vo.QuestionStartVo.QuestionnaireRequestVo;
import com.nbtech.ailab.external.vo.UserQnListParamVo;
import com.nbtech.ailab.external.vo.QuestionStartVo.StarQuestionData;
import com.nbtech.ailab.util.HttpRequestUtil;
import com.nbtech.ailab.util.RedisService;
import com.nbtech.ailab.util.SHA1Encryptor;
import com.nbtech.ailab.util.TimestampGenerator;

import com.nbtech.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.SortedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
@Slf4j
public class QuestionnaireStarFacade {

    @Value("${questionnaire.appid}")
    private String questionnaireAppid;

    @Value("${questionnaire.appkey}")
    private String questionnaireAppkey;

    @Value("${questionnaire.address}")
    private String questionnaireAddress;

    @Autowired
    private QuestionStarDataDao questionStarDataDao;

    @Autowired
    private QuestionStarDao questionStarDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisQueueFacade redisQueueFacade;

    /**
     * 问卷星创建用户/登录接口
     */
    public void createLogin(QnStartLoginParamVo paramVo) {
        String url = String.format("%s%s", questionnaireAddress, QuStartEnum.CREATE_LOGIN.getUrl());
        String param = String.format("appid=%s&subuser=%s&sign=%s&roleId=%s&ts=%s", questionnaireAppid,
                paramVo.getSubUser(), paramVo.getSign(), paramVo.getTs());
        ResponseEntity<String> createLoginResponse = HttpRequestUtil.getParamRequest(url, param);
        String body = createLoginResponse.getBody();
    }

    /**
     * 问卷星的快速登录
     *
     * @param subuser
     * @return
     */
    public String getLoginUrl(String subuser) {
        if (subuser == null) {
            subuser = "wangjingren";
        }
        TimestampGenerator tsUtil = new TimestampGenerator();
        tsUtil.generateTimestamp();
        long timestamp = tsUtil.getTimestamp();
        String result = questionnaireAppid + questionnaireAppkey + subuser + timestamp;
        String encrypted = SHA1Encryptor.encryptSHA1(result);

        String url = "https://www.wjx.cn/zunxiang/login.aspx";
        url = String.format("%s?appid=%s&subuser=%s&ts=%d&sign=%s", url, questionnaireAppid, subuser, timestamp,
                encrypted);
        return url;
    }

    /**
     * 调用数据
     */
    public Map getSign(String subuser) {
        TimestampGenerator tsUtil = new TimestampGenerator();
        tsUtil.generateTimestamp();
        long timestamp = tsUtil.getTimestamp();
        String result = questionnaireAppid + questionnaireAppkey + subuser + timestamp;
        String encrypted = SHA1Encryptor.encryptSHA1(result);
        Map map = new HashMap();
        map.put("encrypted", encrypted);
        map.put("timestamp", timestamp);
        return map;
    }

    /**
     * sha1字符串加密
     */
    public String getSha1String(String target) {
        return SHA1Encryptor.encryptSHA1(target);
    }


    /**
     * 获取问卷列表数据
     */
    public void getQnList(QnListParamVo paramVo) {
        String url = String.format("%s%s", questionnaireAddress, QuStartEnum.GET_QN_LIST.getUrl());
        String param = String.format("appid=%s&username=%s&ts=%s&folder=%s&sign=%s", questionnaireAppid,
                paramVo.getUsername(), paramVo.getTs(), paramVo.getFolder(), paramVo.getSign());
        ResponseEntity<String> getQnListRequest = HttpRequestUtil.getParamRequest(url, param);
        String body = getQnListRequest.getBody();
    }

    /**
     * 获取问卷答卷数据
     */
    public void getQnData(QnDataParamVo paramVo) {
        String url = String.format("%s%s", questionnaireAddress, QuStartEnum.GET_QN_DATA.getUrl());
        String param = String.format("appid=%s&activity=%s&ts=%s&sign=%s&pageindex=%s&pagesize=%s", questionnaireAppid,
                paramVo.getActivity(), paramVo.getTs(), paramVo.getSign(), paramVo.getPageIndex(),
                paramVo.getPageSize());
        ResponseEntity<String> getQnDataRequest = HttpRequestUtil.getParamRequest(url, param);
        String body = getQnDataRequest.getBody();
    }

    /**
     * 参与者数据查询
     */
    public void getUserQnList(UserQnListParamVo paramVo) {
        String url = String.format("%s%s", questionnaireAddress, QuStartEnum.GET_USER_QN_LIST.getUrl());
        String param = String.format("appid=%s&username=%s&joiner=%s&realname=%s&dept=%s&extf=%s&ts=%s&sign=%s",
                questionnaireAppid, paramVo.getUsername(), paramVo.getJoiner(), paramVo.getRealName(),
                paramVo.getDept(), paramVo.getExtf(), paramVo.getTs(), paramVo.getSign());
        ResponseEntity<String> getUserQnListRequest = HttpRequestUtil.getParamRequest(url, param);
        String body = getUserQnListRequest.getBody();

    }

    /**
     * 获取问卷列表
     */
    public Object getQuestionList(Integer status, Integer current, Integer size) {
        TimestampGenerator tsUtil = new TimestampGenerator();
        tsUtil.generateTimestamp();
        long timestamp = tsUtil.getTimestamp();

        String url = "https://www.wjx.cn/openapi/default.aspx";

        SortedMap<String, Object> dic = new TreeMap<>();
        dic.put("encode", "sha1");
        dic.put("creater", "wangjingren");
        dic.put("appid", questionnaireAppid);
        dic.put("ts", timestamp);
        dic.put("action", "1000002");
        dic.put("status", status);
        dic.put("page_index", current);
        dic.put("page_size", size);

        StringBuilder toSign = new StringBuilder();
        for (Map.Entry<String, Object> kv : dic.entrySet()) {
            if (kv.getValue() != null) {
                toSign.append(kv.getValue());
            }
        }

        toSign.append(questionnaireAppkey);

        String wjxSign = SHA1Encryptor.encryptSHA1(toSign.toString()).toLowerCase();

        dic.put("sign", wjxSign);
        String content = JSON.toJSONString(dic);
        String data = HttpRequestUtil.postRequest(url, content);

        return JSON.parse(data);
    }

    /**
     * 获取问卷数据
     */
    public Object getQuestionData(String vid) {
        TimestampGenerator tsUtil = new TimestampGenerator();
        tsUtil.generateTimestamp();
        long timestamp = tsUtil.getTimestamp();

        String url = "https://www.wjx.cn/openapi/default.aspx";

        SortedMap<String, Object> dic = new TreeMap<>();
        dic.put("encode", "sha1");
        dic.put("vid", vid);
        dic.put("appid", questionnaireAppid);
        dic.put("ts", timestamp);
        dic.put("action", "1000001");

        StringBuilder toSign = new StringBuilder();
        for (Map.Entry<String, Object> kv : dic.entrySet()) {
            if (kv.getValue() != null) {
                toSign.append(kv.getValue());
            }
        }

        toSign.append(questionnaireAppkey);
        String wjxSign = SHA1Encryptor.encryptSHA1(toSign.toString()).toLowerCase();
        dic.put("sign", wjxSign);
        String content = JSON.toJSONString(dic);
        String data = HttpRequestUtil.postRequest(url, content);

        return JSON.parse(data);

    }

    /**
     * 创建问卷
     */
    public Object createQuestion(String title, String desc) {

        String url = "https://www.wjx.cn/openapi/default.aspx";

        SortedMap<String, Object> dic = new TreeMap<>();
        dic.put("creater", "wangjingren");
        dic.put("atype", 1);
        dic.put("title", title);
        dic.put("desc", desc);

        StringBuilder toSign = new StringBuilder();
        for (Map.Entry<String, Object> kv : dic.entrySet()) {
            if (kv.getValue() != null) {
                toSign.append(kv.getValue());
            }
        }

        toSign.append(questionnaireAppkey);
        String wjxSign = SHA1Encryptor.encryptSHA1(toSign.toString()).toLowerCase();
        dic.put("questions", wjxSign);
        String content = JSON.toJSONString(dic);
        String data = HttpRequestUtil.postRequest(url, content);

        return JSON.parse(data);

    }

    /**
     * 获取问卷星推送的答卷数据
     */
    public void getStarData(HttpServletRequest request) throws IOException {
        String jsonString = IOUtils.toString(request.getInputStream());
        JSONObject dataJson = JSON.parseObject(jsonString);
        log.info("获取问卷星推送的答卷获取到的json字符串:{}", jsonString);
        StarQuestionData starQuestionData = JSON.parseObject(jsonString, StarQuestionData.class);

        // 获取所有q开头的问题
        Map<String, String> questions = new HashMap<>();
        for (String key : dataJson.keySet()) {
            if (key.startsWith("q")) {
                questions.put(key, dataJson.getString(key));
            }
        }
        QuestionStarDataEntity dataEntity = ConvertUtils.sourceToTarget(starQuestionData, QuestionStarDataEntity.class);
        // 保存所有的答题结果
        dataEntity.setQuestionData(JSON.toJSONString(questions));
        String questionTitle;
        String questionStartHead = RedisHeadEnum.QUESTION_START_HEAD.getDesc() + ":" + starQuestionData.getActivity();
        if (redisService.hasKey(questionStartHead)) {
            questionTitle = (String) redisService.get(questionStartHead);
        } else {
            // 调用外部http请求获取标题信息
            String url = "https://www.wjx.cn/handler/IllustrateApi.ashx";
            String param = String.format("activityID=%s", starQuestionData.getActivity());
            ResponseEntity<String> paramRequest = HttpRequestUtil.getParamRequest(url, param);
            questionTitle = paramRequest.getBody();
            redisService.set(questionStartHead, questionTitle, 60 * 5L);
        }
        dataEntity.setQuestionTitle(questionTitle);
        dataEntity.setIndexDesc(starQuestionData.getIndex());
        log.info("保存问卷答题结果:{}", dataEntity);
        // 不直接插入 先放redis缓存里面
        redisQueueFacade.push(dataEntity, RedisHeadEnum.QUESTION_START_RECEPTION.getDesc());
    }


    /**
     * 获取问卷数据
     */
    public void getQuestionnaireData(QuestionnaireRequestVo requestVo) {
        String jsonString = JSON.toJSONString(requestVo);
        log.info("获取问卷修改的数据:{}", jsonString);

        // AES 加密字符串连密钥都不给 无语 居然叫我自己再去调查询试卷详情的接口
        Object questionData = getQuestionData(requestVo.getActivityId());
        log.info("获取到的问卷信息: {}", JSON.toJSONString(questionData));

        QuestionStarEntity questionStar = ConvertUtils.sourceToTarget(requestVo, QuestionStarEntity.class);
        questionStar.setActivityPcUrl(requestVo.getActivityPCUrl());
        questionStar.setData(JSON.toJSONString(questionData));

        log.info("可以保存的问卷信息: {}", JSON.toJSONString(questionStar));
        questionStarDao.insert(questionStar);
    }
}
