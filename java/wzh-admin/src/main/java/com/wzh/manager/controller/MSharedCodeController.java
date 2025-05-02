package com.wzh.manager.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wzh.common.annotation.Log;
import com.wzh.common.core.controller.BaseController;
import com.wzh.common.core.domain.AjaxResult;
import com.wzh.common.enums.BusinessType;
import com.wzh.manager.domain.MSharedCode;
import com.wzh.manager.service.IMSharedCodeService;
import com.wzh.common.utils.poi.ExcelUtil;
import com.wzh.common.core.page.TableDataInfo;

/**
 * 分享码管理Controller
 * 
 * @author wzh
 * @date 2025-05-02
 */
@RestController
@RequestMapping("/manager/code")
public class MSharedCodeController extends BaseController
{
    @Autowired
    private IMSharedCodeService mSharedCodeService;

    /**
     * 查询分享码管理列表
     */
    @PreAuthorize("@ss.hasPermi('manager:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(MSharedCode mSharedCode)
    {
        startPage();
        List<MSharedCode> list = mSharedCodeService.selectMSharedCodeList(mSharedCode);
        return getDataTable(list);
    }

    /**
     * 导出分享码管理列表
     */
    @PreAuthorize("@ss.hasPermi('manager:code:export')")
    @Log(title = "分享码管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MSharedCode mSharedCode)
    {
        List<MSharedCode> list = mSharedCodeService.selectMSharedCodeList(mSharedCode);
        ExcelUtil<MSharedCode> util = new ExcelUtil<MSharedCode>(MSharedCode.class);
        util.exportExcel(response, list, "分享码管理数据");
    }

    /**
     * 获取分享码管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('manager:code:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mSharedCodeService.selectMSharedCodeById(id));
    }

    /**
     * 新增分享码管理
     */
    @PreAuthorize("@ss.hasPermi('manager:code:add')")
    @Log(title = "分享码管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MSharedCode mSharedCode)
    {
        return toAjax(mSharedCodeService.insertMSharedCode(mSharedCode));
    }

    /**
     * 修改分享码管理
     */
    @PreAuthorize("@ss.hasPermi('manager:code:edit')")
    @Log(title = "分享码管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MSharedCode mSharedCode)
    {
        return toAjax(mSharedCodeService.updateMSharedCode(mSharedCode));
    }

    /**
     * 删除分享码管理
     */
    @PreAuthorize("@ss.hasPermi('manager:code:remove')")
    @Log(title = "分享码管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mSharedCodeService.deleteMSharedCodeByIds(ids));
    }
}
