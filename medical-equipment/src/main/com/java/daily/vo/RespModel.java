package com.java.daily.vo;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * 操作消息提醒
 *
 * @author luck
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RespModel<T> {

   private static final long serialVersionUID = 1L;

   /**
    * 状态码
    */
   private int code;

   /**
    * 返回内容
    */
   private String msg;

   /**
    * 数据对象
    */
   private T data;

   /**
    * 初始化一个新创建的 RespModel 对象
    *
    * @param code 状态码
    * @param msg  返回内容
    */
   public RespModel(int code, String msg) {
      this.code = code;
      this.msg = msg;
   }

   /**
    * 返回成功消息
    *
    * @return 成功消息
    */
   public static RespModel<Void> success() {
      return RespModel.success("操作成功");
   }

   /**
    * 返回成功数据
    *
    * @return 成功消息
    */
   public static <T> RespModel<T> success(T data) {
      return RespModel.success("操作成功", data);
   }

   /**
    * 返回成功消息
    *
    * @param msg 返回内容
    * @return 成功消息
    */
   public static RespModel success(String msg) {
      return RespModel.success(msg, null);
   }

   /**
    * 返回成功消息
    *
    * @param msg  返回内容
    * @param data 数据对象
    * @return 成功消息
    */
   public static <T> RespModel<T> success(String msg, T data) {
      return new RespModel<>(HttpStatus.OK.value(), msg, data);
   }


   /**
    * 返回错误消息
    *
    * @return
    */
   public static RespModel<Void> error() {
      return RespModel.error("操作失败");
   }

   /**
    * 返回错误消息
    *
    * @param msg 返回内容
    * @return 警告消息
    */
   public static RespModel<Void> error(String msg) {
      return RespModel.error(msg, null);
   }

   /**
    * 返回错误消息
    *
    * @param msg  返回内容
    * @param data 数据对象
    * @return 警告消息
    */
   public static <T> RespModel<T> error(String msg, T data) {
      return new RespModel<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
   }

   /**
    * 返回错误消息
    *
    * @param code 状态码
    * @param msg  返回内容
    * @return 警告消息
    */
   public static RespModel<Void> error(int code, String msg) {
      return new RespModel<>(code, msg, null);
   }

}
