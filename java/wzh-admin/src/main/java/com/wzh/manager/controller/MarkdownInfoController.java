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
import com.wzh.manager.domain.MarkdownInfo;
import com.wzh.manager.service.IMarkdownInfoService;
import com.wzh.common.utils.poi.ExcelUtil;
import com.wzh.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author wzh
 * @date 2025-04-23
 */
@RestController
@RequestMapping("/manager/info")
public class MarkdownInfoController extends BaseController
{
    @Autowired
    private IMarkdownInfoService mMarkdownInfoService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('manager:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(MarkdownInfo mMarkdownInfo)
    {
        startPage();
        List<MarkdownInfo> list = mMarkdownInfoService.selectMMarkdownInfoList(mMarkdownInfo);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('manager:info:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MarkdownInfo mMarkdownInfo)
    {
        List<MarkdownInfo> list = mMarkdownInfoService.selectMMarkdownInfoList(mMarkdownInfo);
        ExcelUtil<MarkdownInfo> util = new ExcelUtil<MarkdownInfo>(MarkdownInfo.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('manager:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mMarkdownInfoService.selectMMarkdownInfoById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('manager:info:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MarkdownInfo mMarkdownInfo)
    {
        return AjaxResult.success(mMarkdownInfoService.insertMMarkdownInfo(mMarkdownInfo));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('manager:info:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MarkdownInfo mMarkdownInfo)
    {
        return toAjax(mMarkdownInfoService.updateMMarkdownInfo(mMarkdownInfo));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('manager:info:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mMarkdownInfoService.deleteMMarkdownInfoByIds(ids));
    }
}
