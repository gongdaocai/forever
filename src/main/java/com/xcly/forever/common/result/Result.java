package com.xcly.forever.common.result;

import java.io.Serializable;

/**
 *  封装返回页面数据
 * @author jiayong.du
 * @date 2018/3/5 10:19
 * @param
 */
public class Result implements Serializable {

	/**
	 * HTTP状态码 200 传输成功 500服务器异常 404资源未找到
	 */
	private Integer code;

	/**
	 * 返回提示信息
	 */
	private String description;

	private BizResultVO detail;

	public Result(Integer code, String description,BizResultVO detail) {
		this.code = code;
		this.description = description;
		this.detail = detail;
	}

	/**
	 * 创建系统异常
	 * @return
	 */
	public static Result createError(){
		return new Result(Constants.UNKNOWN_EXCEPTION, Constants.ERROR_SYSTEM_MEG, null);
	}
	public static Result createError(Integer errorCode,String errorMessage){
		return new Result(errorCode, errorMessage, null);
	}

	/**
	 * 完全正常的请求,无参数，默认返回值为SUCCESS，操作成功码1
	 * @return
	 */
	public static Result createSuccess(){
		return new Result(Constants.SUCCESS_CODE, "SUCCESS" , new BizResultVO(1,"SUCCESS",null ));
	}

	/**
	 * 完全正常的请求,仅传递业务操作反馈信息，操作成功码1
	 * @param bizMessage
	 * @return
	 */
	public static Result createSuccess(String bizMessage){
		return new Result(Constants.SUCCESS_CODE, "SUCCESS" , new BizResultVO(1,bizMessage,null ));
	}

	/**
	 * 完全正常的请求,操作成功码1,传递业务操作反馈信息，有反馈的结果数据
	 * @param bizMessage
	 * @param bizData
	 * @return
	 */
	public static Result createSuccess(String bizMessage,Object bizData ){
		return new Result(Constants.SUCCESS_CODE, "SUCCESS" , new BizResultVO(1,bizMessage,bizData ));
	}

	/**
	 * 请求服务成功，但是出现业务异常，如：参数错误、插入错误等
	 * @param bizCode 业务代码，成功为正数，失败为小于等于0的数
	 * @param bizMessage 业务消息
	 * @return
	 */
	public static Result createBizError(Integer bizCode,String bizMessage){
		return new Result(Constants.SUCCESS_CODE,"",new BizResultVO(bizCode,bizMessage,null));
	}
	
	/**
	 * 请求服务成功，但是出现业务异常，如：参数错误、插入错误等
	 * @param bizCode 业务代码，成功为正数，失败为小于等于0的数
	 * @param bizMessage 业务消息
	 * @return
	 */
	public static Result createBizError(Integer bizCode,String bizMessage,Object bizData){
		return new Result(Constants.SUCCESS_CODE,"",new BizResultVO(bizCode,bizMessage,bizData));
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode( Integer code ) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public Object getDetail() {
		return detail;
	}

	public void setDetail( BizResultVO detail ) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "Result{" +
				"code=" + code +
				", description='" + description + '\'' +
				", detail=" + detail +
				'}';
	}
}
