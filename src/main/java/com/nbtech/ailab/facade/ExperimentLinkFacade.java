package com.nbtech.ailab.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dto.GroupsPersonDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.service.IGroupsPersonService;
import com.nbtech.ailab.biz.service.ISysUserService;
import com.nbtech.ailab.config.AesSecret;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.AesKeyVo;
import com.nbtech.ailab.vo.SysUserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExperimentLinkFacade {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IGroupsPersonService groupsPersonService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private GroupsDao groupsDao;

    private static final String keySecret = "{\"keyArr\":\"NfMiYYvOXdzRWgLJGTbJeA==\"}";


    /**
     * 根据链接信息登录
     *
     * @param link
     * @return
     */
    public SysUserVo loginByLink(String link) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        //解密传输进来的userId
        AesKeyVo aesKeyVo = objectMapper.readValue(keySecret, AesKeyVo.class);
        byte[] keyArr = aesKeyVo.getKeyArr();
        Long userId = Long.parseLong(AesSecret.cancel(link, keyArr));

        GroupsPersonDto groupsPerson = groupsPersonService.getByUserId(userId);
        SysUserDto user = sysUserService.get(userId);
        String userName = user.getUsername();

        //密码需要用实验组里的密钥去解密
        Long groupsId = groupsPerson.getGroupsId();
        String aesString = groupsDao.getKeyString(groupsId);
        AesKeyVo aesKey = objectMapper.readValue(aesString, AesKeyVo.class);
        String password = AesSecret.cancel(groupsPerson.getPassword(), aesKey.getKeyArr());

        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername(userName);
        token.setPassword(password.toCharArray());

        // 登出当前session用户
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        // 登录
        Subject loginUser = SecurityUtils.getSubject();
        loginUser.login(token);
        Long currentUserId = ShiroUtils.getUserId();

        return userFacade.get(currentUserId);


    }
}
