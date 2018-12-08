package com.mmall.common;

import com.mmall.exception.ParamException;
import com.mmall.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;

/**
 * 全局异常处理器
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System err";

        // .json,  .page
        //这里我们要求项目中所有请求json数据的，都是用.json结尾
        if(url.endsWith(".json")){
            if(ex instanceof PermissionException || ex instanceof ParamException){
                JsonData result = JsonData.fail(ex.getMessage());
                mv = new ModelAndView("jsonView",result.toMap());
            }else{
                log.error("unknow json exception,url"+url, ex);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView",result.toMap());
            }
            //这里我们要求项目中所有请求页面的，都是用.page结尾
        }else if(url.endsWith(".page")){
            log.error("unknow page exception,url"+url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            //这里配置的exception会默认到根路径下寻找一个exception.jsp的页面
            mv = new ModelAndView("exception", result.toMap());
        }else{
            log.error("unknow exception,url"+url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());
        }
        return mv;
    }
}
