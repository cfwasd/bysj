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
import com.wzh.manager.domain.MShared;
import com.wzh.manager.service.IMSharedService;
import com.wzh.common.utils.poi.ExcelUtil;
import com.wzh.common.core.page.TableDataInfo;

/**
 * 文件分享相关Controller
 *
 * @author wzh
 * @date 2025-05-02
 */
@RestController
@RequestMapping("/manager/shared")
public class MSharedController extends BaseController
{
    @Autowired
    private IMSharedService mSharedService;

    /**
     * 查询文件分享相关列表
     */
    @PreAuthorize("@ss.hasPermi('manager:shared:list')")
    @GetMapping("/list")
    public TableDataInfo list(MShared mShared)
    {
        startPage();
        List<MShared> list = mSharedService.selectMSharedList(mShared);
        return getDataTable(list);
    }

    /**
     * 导出文件分享相关列表
     */
    @PreAuthorize("@ss.hasPermi('manager:shared:export')")
    @Log(title = "文件分享相关", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MShared mShared)
    {
        List<MShared> list = mSharedService.selectMSharedList(mShared);
        ExcelUtil<MShared> util = new ExcelUtil<MShared>(MShared.class);
        util.exportExcel(response, list, "文件分享相关数据");
    }

    /**
     * 获取文件分享相关详细信息
     */
    @PreAuthorize("@ss.hasPermi('manager:shared:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mSharedService.selectMSharedById(id));
    }

    /**
     * 新增文件分享相关
     */
    @PreAuthorize("@ss.hasPermi('manager:shared:add')")
    @Log(title = "文件分享相关", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MShared mShared) {
        return mSharedService.insertMShared(mShared);
    }

    /**
     * 修改文件分享相关
     */
    @PreAuthorize("@ss.hasPermi('manager:shared:edit')")
    @Log(title = "文件分享相关", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MShared mShared)
    {
        return toAjax(mSharedService.updateMShared(mShared));
    }

    /**
     * 删除文件分享相关
     */
    @PreAuthorize("@ss.hasPermi('manager:shared:remove')")
    @Log(title = "文件分享相关", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mSharedService.deleteMSharedByIds(ids));
    }
}
