package org.wzeiri.zr.zrtaxiplatform.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算工具类(Double)
 *
 * @author sunj
 */
public class CalculateUtil {
    public static BigDecimal getBigDecimal(double num) {
        return new BigDecimal(num);
    }

    /**
     * 加法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double add(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.add(big2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 减法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double subtract(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.subtract(big2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 乘法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double multiply(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.multiply(big2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal bigDecimalMultiply(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.multiply(big2);
    }

    /**
     * 除法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double divide(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.divide(big2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

/*	public static double multiply(double price,int num) {
        double a = price;
		double b = new Integer(num).doubleValue();
		return a*b;
	}*/

/*	public static double multiply(double price, int num) {
        BigDecimal big1 = getBigDecimal(price);
		BigDecimal big2 = getBigDecimal(num*1.00d);
		return big1.multiply(big2).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}*/

    public static String getInterval(double num1, double num2) {
        if (num1 == num2) {
            return num1 + "";
        }
        return num1 + " - " + num2;
    }

    //强转,强转失败均返回0.00
    public static int toInteger(String value) {
        int tem = 0;
        try {
            tem = Integer.parseInt(value);
        } catch (Exception e) {
        }
        return tem;
    }

    public static double todouble(String value) {
        double tem = 0;
        try {
            tem = Double.parseDouble(value);
        } catch (Exception e) {
        }
        return tem;
    }

    /**
     * 单位转换→万(四舍五入保留2位小数)<br>
     * 已变更 直接加上单位(万)
     *
     * @param value
     * @return
     */
    public static String unitConversionToTenThousand(String value) {
        //return unitConversionToTenThousand(value,true);
        if (value == null) {
            return "";
        }
        return value.concat("万");
    }

    /**
     * 单位转换→万(四舍五入保留2位小数)
     *
     * @param value
     * @param addUnit 是否加上单位(万)
     * @return
     */
    public static String unitConversionToTenThousand(double value, boolean addUnit) {
        if (value < 10000) {
            return getFormatToString(value);
        } else if (value >= 10000) {
            return getFormatToString(divide(value, 10000d)) + (addUnit ? "万" : "");
        } else {
            return "";
        }
    }

    /**
     * 四舍五入 保留小数
     *
     * @param value
     * @param scale 保留小数位数
     * @return
     */
    public static double getFormat(double value, int scale) {
        BigDecimal b = new BigDecimal(value);
        double d1 = b.setScale(scale, RoundingMode.HALF_UP).doubleValue();
        return d1;
    }

    /**
     * 保留小数
     *
     * @param value
     * @param scale   保留小数位数
     * @param isRound 是否四舍五入
     * @return
     */
    public static String getFormatToString(double value, int scale, boolean isRound) {
        String s = "#0.";
        if (scale < 0) {
            scale = 2;
        } else if (scale == 0) {
            s = "#0";
        }
        if (scale > 0) {
            for (int i = 0; i < scale; i++) {
                s += "0";
            }
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat(s);
        if (isRound) {
            return df.format(getFormat(value, scale));
        } else {
            return df.format(value);
        }
    }

    /**
     * 四舍五入 保留2位小数
     *
     * @param value
     * @return
     */
    public static String getFormatToString(double value) {
        return getFormatToString(value, 2, true);
    }

    /**
     * 四舍五入
     *
     * @param value
     * @return
     */
    public static String getFormatToString(double value, int scale) {
        return getFormatToString(value, scale, true);
    }

    /**
     * 取小数
     *
     * @param value 数字
     * @param scale 保留小数位数
     * @return
     */
    public static String getFormatDecimalFraction(double value, int scale) {
        StringBuffer decimalFraction = new StringBuffer("#");
        if (scale > 0) {
            decimalFraction.append(".");
        }
        for (int i = 0; i < scale; i++) {
            decimalFraction.append("#");
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat(decimalFraction.toString());
        return df.format(value);
    }


}
