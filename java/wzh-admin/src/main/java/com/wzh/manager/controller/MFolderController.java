package com.wzh.manager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.wzh.manager.domain.MFolderVo;
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
import com.wzh.manager.domain.MFolder;
import com.wzh.manager.service.IMFolderService;
import com.wzh.common.utils.poi.ExcelUtil;
import com.wzh.common.core.page.TableDataInfo;

/**
 * 逻辑文件夹Controller
 * 
 * @author ruoyi
 * @date 2025-03-23
 */
@RestController
@RequestMapping("/manager/folder")
public class MFolderController extends BaseController
{
    @Autowired
    private IMFolderService mFolderService;

    /**
     * 查询逻辑文件夹列表
     */
    @PreAuthorize("@ss.hasPermi('manager:folder:list')")
    @GetMapping("/list")
    public TableDataInfo list(MFolder mFolder)
    {
        startPage();
        List<MFolder> list = mFolderService.selectMFolderList(mFolder);
        return getDataTable(list);
    }

    /**
     * 查询逻辑文件夹列表
     */
    @PreAuthorize("@ss.hasPermi('manager:folder:list')")
    @GetMapping("/deepTree")
    public AjaxResult deepTree(MFolder mFolder)
    {
        List<MFolderVo> list = mFolderService.deepTree(mFolder);
        return AjaxResult.success(list);
    }

    /**
     * 导出逻辑文件夹列表
     */
    @PreAuthorize("@ss.hasPermi('manager:folder:export')")
    @Log(title = "逻辑文件夹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MFolder mFolder)
    {
        List<MFolder> list = mFolderService.selectMFolderList(mFolder);
        ExcelUtil<MFolder> util = new ExcelUtil<MFolder>(MFolder.class);
        util.exportExcel(response, list, "逻辑文件夹数据");
    }

    /**
     * 获取逻辑文件夹详细信息
     */
    @PreAuthorize("@ss.hasPermi('manager:folder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mFolderService.selectMFolderById(id));
    }

    /**
     * 新增逻辑文件夹
     */
    @PreAuthorize("@ss.hasPermi('manager:folder:add')")
    @Log(title = "逻辑文件夹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MFolder mFolder)
    {
        return toAjax(mFolderService.insertMFolder(mFolder));
    }

    /**
     * 修改逻辑文件夹
     */
    @PreAuthorize("@ss.hasPermi('manager:folder:edit')")
    @Log(title = "逻辑文件夹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MFolder mFolder)
    {
        return toAjax(mFolderService.updateMFolder(mFolder));
    }

    /**
     * 删除逻辑文件夹
     */
    @PreAuthorize("@ss.hasPermi('manager:folder:remove')")
    @Log(title = "逻辑文件夹", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mFolderService.deleteMFolderByIds(ids));
    }
}
