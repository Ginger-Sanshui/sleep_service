package com.example.demo.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class TokenAuth implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        String requestPath  = request.getRequestURI();

        /*
        * 开放获取Token端口
        * */
        if(requestPath.equals("/Token")){
            return true;
        }

        /*
        * 获取请求头Token
        * */

        String token = request.getHeader("Author-token");

        //判断Token长度为24
        if(token == null || token.length() == 24){
            response.sendError(405,"Token 缺失");
            return false;
        }
        try {
        if (token.equals("86146e3c320c71484befa19486d76981")){
            return true;
        }else {
            return false;
        }

        }catch (Exception e) {
            sendError(response, "服务器内部错误");
            e.printStackTrace();
            return false;
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException, IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\": 401, \"message\": \"" + message + "\"}");
    }
}
