package cn.hbase.springboothbaseproject.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Brief brief;
    private Detail detail;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Brief{
        private String item_name;
        private String brandname;
        private String sale_qtty;
        private String self_ord_flag;
        private String after_prefr_unit_price;
        private String cancel_flag;
        private String finish_flag;
        private String delete_flag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail{
        private String item_sku_id;
        private String item_first_cate_name;
        private String item_second_cate_name;
        private String item_third_cate_name;
        private String before_prefr_unit_price;
        private String user_actual_pay_amount;
        private String check_account_tm;
        private String total_offer_amount;
        private String rev_addr_province_id;
        private String rev_addr_city_id;
    }
}
