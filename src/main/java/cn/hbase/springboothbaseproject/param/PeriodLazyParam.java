package cn.hbase.springboothbaseproject.param;

import lombok.Data;

import java.util.Date;

@Data
public class PeriodLazyParam {
    private String rowKey;
    private String user_log_acct;
    private Long startTimeDesc;
}
