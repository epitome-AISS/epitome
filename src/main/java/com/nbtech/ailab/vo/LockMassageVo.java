package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * 传递解锁信息的对象
 */
@Data
public class LockMassageVo {

     private Long groupId;
     
     private String elementId;

     private String processId;

     private String roomId;

     private Integer round;

     private Long userId;

     private String roleName;

     private String workId;
}
