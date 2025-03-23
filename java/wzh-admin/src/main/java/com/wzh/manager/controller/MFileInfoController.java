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
import com.wzh.manager.domain.MFileInfo;
import com.wzh.manager.service.IMFileInfoService;
import com.wzh.common.utils.poi.ExcelUtil;
import com.wzh.common.core.page.TableDataInfo;

/**
 * 文件信息Controller
 * 
 * @author wzh
 * @date 2025-03-23
 */
@RestController
@RequestMapping("/manager/fileInfo")
public class MFileInfoController extends BaseController
{
    @Autowired
    private IMFileInfoService mFileInfoService;

    /**
     * 查询文件信息列表
     */
    @PreAuthorize("@ss.hasPermi('manager:fileInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(MFileInfo mFileInfo)
    {
        startPage();
        List<MFileInfo> list = mFileInfoService.selectMFileInfoList(mFileInfo);
        return getDataTable(list);
    }

    /**
     * 导出文件信息列表
     */
    @PreAuthorize("@ss.hasPermi('manager:fileInfo:export')")
    @Log(title = "文件信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MFileInfo mFileInfo)
    {
        List<MFileInfo> list = mFileInfoService.selectMFileInfoList(mFileInfo);
        ExcelUtil<MFileInfo> util = new ExcelUtil<MFileInfo>(MFileInfo.class);
        util.exportExcel(response, list, "文件信息数据");
    }

    /**
     * 获取文件信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('manager:fileInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mFileInfoService.selectMFileInfoById(id));
    }

    /**
     * 新增文件信息
     */
    @PreAuthorize("@ss.hasPermi('manager:fileInfo:add')")
    @Log(title = "文件信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MFileInfo mFileInfo)
    {
        return toAjax(mFileInfoService.insertMFileInfo(mFileInfo));
    }

    /**
     * 修改文件信息
     */
    @PreAuthorize("@ss.hasPermi('manager:fileInfo:edit')")
    @Log(title = "文件信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MFileInfo mFileInfo)
    {
        return toAjax(mFileInfoService.updateMFileInfo(mFileInfo));
    }

    /**
     * 删除文件信息
     */
    @PreAuthorize("@ss.hasPermi('manager:fileInfo:remove')")
    @Log(title = "文件信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mFileInfoService.deleteMFileInfoByIds(ids));
    }
}
