package com.example.demo.security;

public class sslToken {

    public void EncryToken(String Token) {
        //获得数字
        Token = Token.trim();
        String Token1 = "";
        for (int i = 0; i < Token.length(); i++) {
            if (Token.charAt(i) >= 48 && Token.charAt(i) <= 57) {
                Token1 += Token.charAt(i);
            }
        }
        System.out.println(Token1);

        //获得字母
        var Token2 =Token.replaceAll("\\s*","").replaceAll("[^(A-Za-z)]","");

    }

}
