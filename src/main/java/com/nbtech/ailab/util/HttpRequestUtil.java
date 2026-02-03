package com.nbtech.ailab.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.vo.PyConfigVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageResult;
import org.apache.poi.ss.formula.functions.T;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

public class HttpRequestUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtil.class);

    /**
     * 分页请求结果转换为分页格式数据
     *
     * @param page        当前页
     * @param pageSize    每页展示条数
     * @param pageRequest 请求结果
     * @param arrName     集合名称
     * @return 返回分页结果
     */
    public static PageResult<Object> changePage(Integer page, Integer pageSize, ResponseEntity<String> pageRequest, String arrName) {
        Page<T> pageEntity = new Page<>();
        JSONObject body = JSON.parseObject(pageRequest.getBody());
        if (body.containsKey("retcode") && body.getInteger("retcode") == 102) {
            throw new BizException(body.getInteger("retcode"), body.getString("retmsg"));
        }
        PageResult<Object> files = PageResult.build(pageEntity, body.getJSONObject("data").getJSONArray(arrName));
        files.setTotal(body.getJSONObject("data").getInteger("total"));
        files.setCurrent(page);
        files.setSize(pageSize);
        return files;
    }


    /**
     * get请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> getRequest(String url, String param, String token) {
        String requestAddress = String.format("%s?%s", url, param);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", CodeUtil.packingToken(token));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.GET, requestEntity, String.class);
    }

    /**
     * get 获取app的api-key
     *
     * @param url
     * @return
     */
    public static ResponseEntity<String> getApiKey(String url, String token) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", CodeUtil.packingToken(token));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.GET, requestEntity, String.class);
    }


    /**
     * get 无参 仅token请求
     *
     * @param url
     * @return
     */
    public static String getPathParam(String url, String param) throws UnsupportedEncodingException {
        // 链接是特殊的请求参数 所以需要转义后使用
        String changeUrl = url + URLEncoder.encode(param, "UTF-8");
        log.info("发送dify服务的请求 {}", changeUrl);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(changeUrl, HttpMethod.GET, requestEntity, String.class);
        return exchange.getBody();
    }

    /**
     * get 无参 仅token请求
     *
     * @param url
     * @return
     */
    public static String getNonParam(String url, String token) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("Authorization", CodeUtil.packingToken(token));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestAddress, HttpMethod.GET, requestEntity, String.class);
        return exchange.getBody();
    }

    /**
     * get分页请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> getParamRequest(String url, String param) {
        String requestAddress = String.format("%s?%s", url, param);
        log.info("发送外部的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.GET, requestEntity, String.class);
    }

    /**
     * post分页请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> postPageRequest(String url, String param) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
    }


    /**
     * getPath请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<?> getPathRequest(String url, String param) {
        String requestAddress = String.format("%s/%s", url, param);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.GET, requestEntity, String.class);
    }

    /**
     * getStreamFile 获取文件
     *
     * @param url
     * @param param
     * @return
     */
    public static void getStreamFile(HttpServletResponse response, String url, String param) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        // 发送请求并接收响应
        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(String.format("%s/%s", url, param), byte[].class);
        // 检查响应状态码
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // 获取响应体
            byte[] imageBytes = responseEntity.getBody();
            // 设置内容类型
            MediaType contentType = responseEntity.getHeaders().getContentType();
            response.setContentType(contentType.toString());
            // 获取输出流
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(imageBytes);
            outputStream.flush();
            outputStream.close();
        } else {
            // 处理错误情况
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to get image");
        }
    }

    /**
     * getPath请求
     *
     * @param url
     * @param param
     * @return
     */
    public static void getFileRequest(HttpServletResponse response, String url, String param, String name) throws IOException {
        String requestAddress = String.format("%s/%s", url, param);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        response.reset();
        String fileName = URLEncoder.encode(name, "utf-8");
        try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            ResponseEntity<byte[]> exchange = restTemplate.exchange(requestAddress, HttpMethod.GET, requestEntity, byte[].class);
            byte[] body = exchange.getBody();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            toClient.write(body);
            toClient.flush();
        }
    }

    /**
     * postPath请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> postPathRequest(String url, String param) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(new ArrayList<MediaType>() {{
            add(TEXT_EVENT_STREAM);
        }});
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
    }

    /**
     * postPath请求
     *
     * @param param
     * @return
     */
    public static String apiRunWorkFlow(String urlString, String param, String token) {
        String requestAddress = String.format("%s", urlString);
        log.info("发送dify服务的请求 {}", requestAddress);
        HttpURLConnection connection = null;
        String result = null;
        try {
            URL url = new URL(requestAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", String.format("Bearer %s", token));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(param.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = param.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            // 获取响应状态码
            int responseCode = connection.getResponseCode();
            // 获取请求的结果 持续刷新结果
            if (responseCode >= 200 && responseCode < 300) {
                try (InputStream is = connection.getInputStream();
                     BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    // 遍历返回的流
                    while ((line = rd.readLine()) != null) {
                        // 把最新的结果写入到字符串 直至最后一次结果
                        if (!line.isEmpty()) {
                            result = line;
                        }
                    }
                }
            } else {// 请求失败，读取错误流
                try (InputStream es = connection.getErrorStream();
                     BufferedReader er = new BufferedReader(new InputStreamReader(es))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = er.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    // 记录错误日志
                    log.error("DIFY运行工作流失败，状态码: {}, 错误信息: {}", responseCode, errorResponse);
                    throw new BizException(BizResponseCodeEnum.WORK_FLOW_ERROR_MESSAGE, errorResponse);
                    // 可以选择抛出异常或者返回错误信息
                }
            }
        } catch (BizException exception){
            throw exception;
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> difyLogin(String url, String param) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
        return exchange;
    }

    /**
     * python工具使用
     *
     * @return
     */
    public static ResponseEntity<String> userPython(PyConfigVo pyConfigVo) {
        String requestAddress = String.format("http://61.169.23.150:8085/py_workflow_process/runPyCode");
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(pyConfigVo), headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
        return exchange;
    }

    /**
     * 不带Token的post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String postRequest(String url, String param) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
        return exchange.getBody();
    }


    /**
     * 带Token的 post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> postRequest(String url, String param, String token) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("Authorization", CodeUtil.packingToken(token));
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
    }


    /**
     * 带Token的 deleted 请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> deleteModelRequest(String url, String param, String token) {
        String requestAddress = String.format("%s", url, param);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("Authorization", CodeUtil.packingToken(token));
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.DELETE, requestEntity, String.class);
    }

    /**
     * 带Token的 deleted 请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<String> deleteRequest(String url, String param, String token) {
        String requestAddress = String.format("%s/%s", url, param);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("Authorization", CodeUtil.packingToken(token));
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        return restTemplate.exchange(requestAddress, HttpMethod.DELETE, requestEntity, String.class);
    }


    /**
     * 文件上传post请求
     *
     * @return
     */
    public static String putRequest(String url, String param, String token) {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("Authorization", CodeUtil.packingToken(token));
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestAddress, HttpMethod.PUT, requestEntity, String.class);
        return exchange.getBody();
    }

    /**
     * 文件上传post请求
     *
     * @return
     */
    public static String postFileRequest(String url, MultipartFile multipartFile, String token) throws IOException {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.put("Authorization", CodeUtil.packingToken(token));
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        File file = getFile(multipartFile);
        body.add("file", new FileSystemResource(file));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
        if (file.exists()) {
            file.delete();
        }
        return exchange.getBody();
    }

    /**
     * 文件上传post请求
     *
     * @return
     */
    public static BizResponse<?> postDocumentRequest(String url,
                                                     List<MultipartFile> multipartFiles,
                                                     Long user_id,
                                                     Integer tenant_id,
                                                     String parent_id,
                                                     String kb_id,
                                                     String user_role,
                                                     List<List<String>> user_org) throws IOException {
        String requestAddress = String.format("%s", url);
        log.info("发送dify服务的请求 {}", requestAddress);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile multipartFile : multipartFiles) {
            File file = getFile(multipartFile);
            body.add("file", new FileSystemResource(file));
        }
        ResponseEntity<String> exchange = null;
        try {
            body.add("parent_id", parent_id);
            body.add("tenant_id", tenant_id);
            body.add("user_id", user_id);
            body.add("user_role", user_role);
            body.add("user_org", user_org);
            body.add("kb_id", kb_id);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            exchange = restTemplate.exchange(requestAddress, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // 删除所有上传的临时文件
            for (MultipartFile multipartFile : multipartFiles) {
                File file = getFile(multipartFile);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        return getResponse(exchange);
    }

    static BizResponse<?> getResponse(ResponseEntity<String> responseEntity) {
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        Integer retcode = jsonObject.getInteger("retcode");
        String retmsg = jsonObject.getString("retmsg");
        Object data = jsonObject.get("data");
        return new BizResponse(retcode, retmsg, data);
    }


    /**
     * 把 MultipartFile 转变为 file文件
     */
    public static @NotNull File getFile(MultipartFile file) throws IOException {
        File newFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(newFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return newFile;
    }


}
