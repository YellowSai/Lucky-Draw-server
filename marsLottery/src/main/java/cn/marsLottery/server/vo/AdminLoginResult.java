package cn.marsLottery.server.vo;

import lombok.Data;

@Data
public class AdminLoginResult {
    private String token;
    private SysAdminVO admin;
}
