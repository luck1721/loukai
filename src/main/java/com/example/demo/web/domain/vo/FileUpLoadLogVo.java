package com.example.demo.web.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "上传文件返回信息")
public class FileUpLoadLogVo {

    @ApiModelProperty(value = "上传文件成功标志，失败时path为空", position = 1)
    private boolean success;
    @ApiModelProperty(value = "文件名称",position = 2)
    private String name;
    @ApiModelProperty(value = "文件路径",position = 3)
    private String path;

    public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
