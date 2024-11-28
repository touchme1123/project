package com.example.demo.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

public class MemberDTO extends User {

    private String email, pw, nickname;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    //security 가 필요로 하는 DTO 이며, 이 DTO는 생성자가 필수적이고 가장 쉽게 만드는 권한부여가 SimpleGrantedAuthority
    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {
        super(
                email,
                pw,
                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList())
        );

        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims(){

        Map<String,Object> dataMap = new HashMap<>();

        dataMap.put("email",email);
        dataMap.put("pw",pw);
        dataMap.put("nickname",nickname);
        dataMap.put("social",social);
        dataMap.put("roleNames",roleNames);

        return dataMap;
    }


}
