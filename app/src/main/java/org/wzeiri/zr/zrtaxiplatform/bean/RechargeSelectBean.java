package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.List;

/**
 * @author k-lm on 2017/12/20.
 */

public class RechargeSelectBean {


    /**
     * oilCardType : 0
     * oilCards : [{"oilCardNumber":"string","id":0}]
     * oilRechargeTemps : [{"amount":0,"realPrice":0,"id":0}]
     */

    private int oilCardType;
    private List<OilCardsBean> oilCards;
    private List<OilRechargeTempBean> oilRechargeTemps;

    public int getOilCardType() {
        return oilCardType;
    }

    public void setOilCardType(int oilCardType) {
        this.oilCardType = oilCardType;
    }

    public List<OilCardsBean> getOilCards() {
        return oilCards;
    }

    public void setOilCards(List<OilCardsBean> oilCards) {
        this.oilCards = oilCards;
    }

    public List<OilRechargeTempBean> getOilRechargeTemps() {
        return oilRechargeTemps;
    }

    public void setOilRechargeTemps(List<OilRechargeTempBean> oilRechargeTemps) {
        this.oilRechargeTemps = oilRechargeTemps;
    }

    public static class OilCardsBean {
        /**
         * oilCardNumber : string
         * id : 0
         */

        private String oilCardNumber;
        private int id;

        public String getOilCardNumber() {
            return oilCardNumber;
        }

        public void setOilCardNumber(String oilCardNumber) {
            this.oilCardNumber = oilCardNumber;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


}
