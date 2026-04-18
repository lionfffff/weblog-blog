package com.yhh.weblogcommon.model.vo;/**
 * @Author Eason
 * @Date 2025-04-28 20:54
 * @PackageName:com.yhh.weblogcommon.model.vo
 * @ClassName: SelectRspVO
 * @Description: TODO
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2025-04-28 20:54
 * @PackageName:com.yhh.weblogcommon.model.vo
 * @ClassName:SelectRspVO
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectRspVO {

    /**
     * Select 下拉列表的展示文字
     */
    private String label;

    /**
     * Select 下拉列表的 value 值，如 ID 等
     */
    private Object value;
}
