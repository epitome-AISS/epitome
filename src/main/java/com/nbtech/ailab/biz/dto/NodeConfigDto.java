package com.nbtech.ailab.biz.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NodeConfigDto {
    private String roleType;
    private String roleName;
    private String flowType;
    private Long userId;
    private String roleDes;
    private String roleDesDetail;
    private String roleHead;
    private Boolean isHost;
    private Object id;
    private List<String> optionList;
    private String outputVar;
    private String inputPrompt;
    private String showVar;
    private Integer initRow;
    private Integer minRow;
    private Integer maxRow;
    private String headerContent;
    private String inputVarConfig;
    private String inputVar;

    private String type;

    private String displayType;

    private String canCopy;

    private Long materialId;

    private String name;

    private String content;

    private Long questionnaireId;

    private String questionnaireName;

    private String questionnaireData;

    private String questionnaireDesc;

    private String questionnaireAttribution;

    private Long experimentPlanId;

    private String experimentPlanName;

    private String experimentPlanTitle;

    private Boolean needTimer;

    private Long creator;

    private LocalDateTime createDate;

    private Long updater;

    private LocalDateTime updateDate;

    private String status;

    private String createName;

    private String updateName;

    private String workFlow;

    private Boolean isReview;

    private Long roleId;

    private String modelInfo;

    private Integer isDeleted;

    private String elementId;

    private String isFavorite;

}
