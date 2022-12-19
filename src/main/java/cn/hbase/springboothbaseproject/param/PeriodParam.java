package cn.hbase.springboothbaseproject.param;

import lombok.Data;

import java.util.Date;

@Data
public class PeriodParam {
    private String user_log_acct;
    private Long startTimeDesc;
    private Long endTimeDesc;
}
