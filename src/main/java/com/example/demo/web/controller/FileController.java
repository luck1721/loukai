package com.example.demo.web.controller;

import cn.com.citycloud.hcs.common.auth.AuthHolder;
import cn.com.citycloud.hcs.common.utils.ConvertUtils;
import cn.com.citycloud.hcs.common.web.DataType;
import cn.com.citycloud.hcs.common.web.ParamType;
import cn.com.citycloud.hcs.common.web.ResponseData;
import com.example.demo.bll.entity.FileUploadLog;
import com.example.demo.bll.service.impl.FileUploadLogService;
import com.example.demo.web.domain.FileUpload;
import com.example.demo.web.domain.vo.FileUpLoadLogVo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传下载控制器
 *
 * @date 2020年4月12日
 * @author huanglj
 */
@RestController("coreFileController")
@RequestMapping({"/file","/file/v1"})
public class FileController {

    @Autowired
    private FileUploadLogService fileUploadLogService;
    //@Autowired
    //private UserService userService;
    //@Autowired
	//private TenantService tenantService;

    @Order(1)
    @ApiOperation(value = "上传文件",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiImplicitParams({
    		@ApiImplicitParam(name = "type",value = "文件类型",dataType = DataType.string,allowEmptyValue = true,paramType = ParamType.form),
    		@ApiImplicitParam(name = "file",value = "文件",dataTypeClass = MultipartFile[].class,required = true,paramType = ParamType.form)
    })
    @PostMapping("")
    public List<FileUpLoadLogVo> uploadFile(@RequestParam(required = false) String type, @RequestParam MultipartFile[] file){
        return ConvertUtils.convertList(fileUploadLogService.uploadFiles(buildFileUploads(type, file), false), FileUpLoadLogVo.class);
    }

    @Order(2)
    @ApiOperation(value = "文件下载",produces=MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiImplicitParams({
    		@ApiImplicitParam(name="path",value="文件路径",dataType= DataType.string,required=true,paramType= ParamType.path),
    		@ApiImplicitParam(name="open",value="是否打开",dataType= DataType.bool,allowEmptyValue=true,paramType= ParamType.query),
    })
    @ApiResponses({
			@ApiResponse(code = HttpServletResponse.SC_OK, message = "文件内容", response = void.class)
    })
    @GetMapping("/{path}")
    public void downloadFile(@PathVariable String path, @RequestParam(required = false) boolean open, HttpServletResponse response) throws Exception {
        Assert.hasText(path, "文件路径不能为空！");
        FileUploadLog fileUploadLog = fileUploadLogService.findByPath(path);
        Assert.isTrue(fileUploadLog != null && (fileUploadLog.isValid() || StringUtils.equals(fileUploadLog.getUploaderId(), AuthHolder.<String>currentUserId())), "文件不存在");
        response.reset();
        String contentType = fileUploadLog.getContentType();
    	if(StringUtils.isNotBlank(contentType)) {
        	if(!contentType.contains("/")) {
        		if(!contentType.contains(";") && StringUtils.isNotBlank(fileUploadLog.getFormat())) {
        			contentType += "/" + fileUploadLog.getFormat();
        		}
        	} else if(!contentType.toLowerCase().contains("charset")) {
        		contentType += ";charset=UTF-8";
        	}
        	response.setContentType(contentType);
        }
        if(fileUploadLog.getSize() != null) {
        	response.setHeader("Content-Length",  fileUploadLog.getSize().toString());
        }
        if(!open) {
        	response.setHeader("Content-Disposition", "attachment;filename=" +
        			URLEncoder.encode(fileUploadLog.getName(), "UTF-8"));
        }
        fileUploadLogService.downloadFile(fileUploadLog, response.getOutputStream());
    }

    @Order(2)
    @ApiOperation(value = "文件刪除")
    @ApiImplicitParam(name="path",value="文件路径",dataType= DataType.string,required=true,paramType= ParamType.path)
    @DeleteMapping("/{path}")
    public void deleteFile(@PathVariable String path) throws Exception{
        Assert.hasText(path, "文件路径不能为空！");
        fileUploadLogService.modifyValid(path, false);
    }

    @Order(3)
    @ApiOperation(value = "上传用户头像",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiImplicitParam(name ="icon", value = "用户头像",dataTypeClass = MultipartFile.class,required = true,paramType = ParamType.form)
    @ApiResponses({
			@ApiResponse(code = HttpServletResponse.SC_OK, message = "头像路径")
    })
    @PostMapping("/user/icon")
    public Long updateUserIcon(@RequestParam MultipartFile icon) throws Exception {
        FileUploadLog fileUploadLog = fileUploadLogService.uploadFile(buildFileUpload(icon, "image"), true);
        Assert.notNull(fileUploadLog, "用户头像不能为空！");
        //String lastIcon = userService.modifyUserIcon(AuthHolder.<String>currentUserId(), fileUploadLog.getPath());
        //if(StringUtils.isNotBlank(lastIcon)) {
        	//fileUploadLogService.modifyValid(lastIcon, false);
        //}
        return fileUploadLog.getPath();
    }



	private FileUpload buildFileUpload(MultipartFile multipart, String contentType) {
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName(multipart.getOriginalFilename());
		fileUpload.setContentType(StringUtils.isNotBlank(contentType) ? contentType : multipart.getContentType());
		fileUpload.setSource(multipart);
		fileUpload.setSize(multipart.getSize());
		fileUpload.setUploaderId(AuthHolder.<String>currentUserId());
		return fileUpload;
	}

	private List<FileUpload> buildFileUploads(String contentType, MultipartFile... multiparts) {
		List<FileUpload> fileUploads = new ArrayList<>();
		for (MultipartFile multipart : multiparts) {
			fileUploads.add(buildFileUpload(multipart, contentType));
		}
		return fileUploads;
	}

}
