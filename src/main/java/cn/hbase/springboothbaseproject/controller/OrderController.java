package cn.hbase.springboothbaseproject.controller;


import cn.hbase.springboothbaseproject.bean.Order;
import cn.hbase.springboothbaseproject.common.Constant;
import cn.hbase.springboothbaseproject.common.Result;
import cn.hbase.springboothbaseproject.param.*;
import cn.hbase.springboothbaseproject.rpc.EsRPC;
import cn.hbase.springboothbaseproject.service.ICURDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ICURDService icurdService;
//    @Autowired
//    private EsDAO esDAO;
    @Autowired
    private EsRPC esRPC;

    //TODO 返回类型还需要封装
    /**
     * 登录后的第一次访问
     * @param usrAcct 账户
     * @return
     */
    @PostMapping(value = "/all")
    public Result getOrdersBriefByUsrAcct(@RequestBody UsrAcct usrAcct) {
        String rowKeyPrefix = MD5(usrAcct.getUser_log_acct()) + "+" + usrAcct.getUser_log_acct();
        String startRowKey = rowKeyPrefix;
        String endRowKey = rowKeyPrefix + "+" + Long.MAX_VALUE;
        List<Order.Brief> data = icurdService.findBriefByStartAndEnd("order", startRowKey, endRowKey);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 向下滑动懒加载，适用于全部查询
     * @param startRowKey 上次加载的最后一条，注意前端要处理一下！把最后一条pk要+1
     * @return
     */
    @PostMapping(value = "/all/lazy")
    public Result getOrdersBriefLazy(@RequestBody RowKey startRowKey) {
        String[] rowKeyParts = startRowKey.getRowKey().split("\\+");
        String endRowKey = rowKeyParts[0] + "+" + rowKeyParts[1] + "+" + Long.MAX_VALUE + "";
        List<Order.Brief> data = icurdService.findBriefByStartAndEnd("order", startRowKey.getRowKey(), endRowKey);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 点击详情，返回给前端之后前端要把brief的数据整合倒一起
     * @param rowKey
     * @return
     */
    @PostMapping(value = "/detail")
    public Result getOrdersDetailByRowKey(@RequestBody RowKey rowKey) {
        return Result.OK(icurdService.findBy("order", rowKey.getRowKey()));
    }

    /**
     * 时间范围查询
     * @param param
     * @return
     */
    @PostMapping(value = "/period")
    public Result getOrdersBriefByPeriod(@RequestBody PeriodParam param) {
        String rowKeyPrefix = MD5(param.getUser_log_acct()) + "+" + param.getUser_log_acct();
        String startRowKey = rowKeyPrefix + "+" + param.getEndTimeDesc(); //注意此处前端会把时间处理成Max-endTime，所以后端就可以直接查
        String endRowKey = rowKeyPrefix + "+" + param.getStartTimeDesc();
        System.out.println(startRowKey);
        System.out.println(endRowKey);
        System.out.println("================");
        List<Order.Brief> data = icurdService.findBriefByStartAndEnd("order", startRowKey, endRowKey);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 时间范围查询向下滑动的懒加载
     * @param param
     * @return
     */
    @PostMapping(value = "/period/lazy")
    public Result getOrdersBriefByPeriodLazy(@RequestBody PeriodLazyParam param) {
        String rowKeyPrefix = MD5(param.getUser_log_acct()) + "+" + param.getUser_log_acct();
        String startRowKey = param.getRowKey(); //注意此处前端会把时间处理成Max-endTime，所以后端就可以直接查
        String endRowKey = rowKeyPrefix + "+" + param.getStartTimeDesc();;
        List<Order.Brief> data = icurdService.findBriefByStartAndEnd("order", startRowKey, endRowKey);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 根据关键字查询
     * @return
     */
    @PostMapping(value = "/search/keyword")
    public Result searchOrderBriefByKeyword(@RequestBody KeySearchParam param) throws IOException {
//        List<String> rowKeys = esDAO.searchByName(param.getKeyword(), param.getUser_log_acct(), param.getOffset());
        List rowKeys = esRPC.searchByName(param);
        List<Order.Brief> data = icurdService.findBriefByKeyList("order", rowKeys);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 查询已完成
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("/search/finish")
    public Result searchOrderFinished(@RequestBody FlagSearchParam param) throws IOException {
        List<String> rowKeys = esRPC.searchFlag(param, "/status/finish");
        List<Order.Brief> data = icurdService.findBriefByKeyList("order", rowKeys);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 查询待收货
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("/search/wait")
    public Result searchOrderWaited(@RequestBody FlagSearchParam param) throws IOException {
        List<String> rowKeys = esRPC.searchFlag(param, "/status/wait");
        List<Order.Brief> data = icurdService.findBriefByKeyList("order", rowKeys);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 查询已取消
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("/search/cancel")
    public Result searchOrderCanceled(@RequestBody FlagSearchParam param) throws IOException {
        List<String> rowKeys = esRPC.searchFlag(param, "/status/cancel");
        List<Order.Brief> data = icurdService.findBriefByKeyList("order", rowKeys);
        return Result.OK(checkExistsMore(data));
    }

    /**
     * 删除订单，记住，只有已完成和已取消才可以删除
     * @param rowkey
     * @return
     * @throws IOException
     */
    @PostMapping("/delete")
    public Result deleteOrder(@RequestBody RowKey rowkey) throws IOException{
        int response = esRPC.updateOrder(rowkey, "/delete");
        //TODO HBase里面要更新字段，请先不要测试以免数据不一致！！！！！！！！
        return Result.OK(response);
    }

    /**
     * 确认收获，记住，待收货的订单只能确认收货不能删除
     * @param rowkey
     * @return
     * @throws IOException
     */
    @PostMapping("/confirm")
    public Result confirmOrder(@RequestBody RowKey rowkey) throws IOException{
        int response = esRPC.updateOrder(rowkey, "/confirm");
        //TODO HBase里面要更新字段，请先不要测试以免数据不一致！！！！！！！！
        return Result.OK(response);
    }


    private Map<String, Object> checkExistsMore(List data){
        Map<String,Object> result = new HashMap<>();
        result.put("Data", data);
        if (data.size() < Constant.PAGE_SIZE)
            result.put("More", false); //没有更多数据了
        else
            result.put("More", true);
        return result;
    }
    static String MD5(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(input.getBytes());// 计算md5函数
        /**
         * digest()最后确定返回md5 hash值，返回值为8位字符串。
         * 因为md5 hash值是16位的hex值，实际上就是8位的字符
         * BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
         * 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
         */
        String s = new BigInteger(1, md.digest()).toString(16);
        return s.substring(3, 7);// 16是表示转换为16进制数
    }
}
