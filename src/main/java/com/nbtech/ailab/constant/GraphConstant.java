package com.nbtech.ailab.constant;

import com.nbtech.ailab.vo.ModelPersonResultVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nber
 * 图形数据的constant
 */
public interface GraphConstant {
    public static final List<ModelPersonResultVo> USER_AGE_GROUPINGS = new ArrayList<ModelPersonResultVo>() {{
        add(new ModelPersonResultVo(0, 19));
        add(new ModelPersonResultVo(20, 49));
        add(new ModelPersonResultVo(50, 99));
        add(new ModelPersonResultVo(100, 199));
        add(new ModelPersonResultVo(200, 200));
    }};
}
