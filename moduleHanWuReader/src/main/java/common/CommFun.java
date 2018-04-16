package common;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ysy15350.moduleaotaoreader.ReadActivity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommFun {

    /**
     * 正则表达式集合类
     *
     * @author yangshiyou
     */
    private class RegexString {
        // public static final String IP =
        // "^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$";

        // public static final String Chinese = "^[\u4e00-\u9fa5]$";
        // / <summary>
        // / 全数字格式 ^(-|\\+)?\\d+(\\.\\d+)?$
        // / </summary>
        public static final String Number = "^(-|\\+)?\\d+(\\.\\d+)?$";
        // / <summary>
        // / 整数格式
        // / </summary>
        public static final String Integer = "^-?\\d+$";
        // / <summary>
        // / 数字与字母的组合
        // / </summary>
        // public static final String Number_Letter = "^[\\dA-Za-z]+$";
        // / <summary>
        // / 英文字母
        // / </summary>
        public static final String Letter = "^[A-Za-z]+$";

        /**
         * 手机号
         */
        public static final String PHONE = "^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$";

        // / <summary>
        // / 邮件地址格式
        // / </summary>
        public static final String MAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        // / <summary>
        // / 日期时间格式
        // / </summary>
        public static final String LONGDATE = "^(((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d)$";
        public static final String SHORTDATE = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-9]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";

    }

    /****************************** 数据判断 *******************************************/
    /**
     * 判断是否全是数字或小数
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isNumber(Object inputString) {
        Pattern pat = Pattern.compile(RegexString.Number);
        Matcher mat = pat.matcher(CommFun.toString(inputString));
        return mat.find();
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
        try {
            if (isNullOrEmpty(str))
                return false;

            return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }

    /**
     * 判断是否是整数
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isInteger(Object inputString) {
        Pattern pat = Pattern.compile(RegexString.Integer);
        Matcher mat = pat.matcher(CommFun.toString(inputString));
        return mat.find();
    }

    /**
     * 判断是否是日期类型
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isDateTime(Object inputString) {
        Pattern p = Pattern.compile(RegexString.LONGDATE);
        Matcher m = p.matcher(CommFun.toString(inputString));
        boolean b = m.matches();
        return b;
    }

    /**
     * 判断是否是短日期类型
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isShortDate(Object inputString) {
        Pattern p = Pattern.compile(RegexString.SHORTDATE);
        Matcher m = p.matcher(CommFun.toString(inputString));
        boolean b = m.matches();
        return b;
    }

    /**
     * 判断是否是邮件地址类型
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isMail(Object inputString) {
        Pattern pat = Pattern.compile(RegexString.MAIL);
        Matcher mat = pat.matcher(CommFun.toString(inputString));
        return mat.find();
    }

    /**
     * 判断是否是手机号码
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isPhone(Object inputString) {
        Pattern pat = Pattern.compile(RegexString.PHONE);
        Matcher mat = pat.matcher(CommFun.toString(inputString));
        return mat.find();
        //return inputString != null;
    }

    /**
     * 转换手机号，加星号
     *
     * @param phone
     * @return
     */
    public static String getPhone(String phone) {
        try {
            if (isPhone(phone)) {
                String phone_1 = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                return phone_1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return phone;
    }

    /**
     * 验证身份证号码
     *
     * @param cardno
     * @return
     */
    public boolean CheckCardNo(String cardno) {

        boolean flag = false;
        try {
            Pattern regex = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
            Matcher matcher = regex.matcher(cardno);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;

    }

    public static boolean isIdCardNo(String cardno) {

        boolean flag = false;
        try {
            if (isNullOrEmpty(cardno)) {
                return false;
            }
            if (cardno.length() != 18)
                return false;
            else {
                String address = cardno.substring(0, 6);// 6位，地区代码

                String birthday = cardno.substring(6, 14);// 8位，出生日期

                String sequenceCode = cardno.substring(14, 17);// 3位，顺序码：奇为男，偶为女

                String checkCode = cardno.substring(17);// 1位，校验码：检验位

                ReadActivity.log( "身份证号码:" + cardno + "、地区代码:" + address + "、出生日期:" + birthday + "、顺序码:" + sequenceCode
                        + "、校验码:" + checkCode + "\n");

                String[] provinceArray = {"11:北京", "12:天津", "13:河北", "14:山西", "15:内蒙古", "21:辽宁", "22:吉林", "23:黑龙江",
                        "31:上海", "32:江苏", "33:浙江", "34:安徽", "35:福建", "36:江西", "37:山东", "41:河南", "42:湖北 ", "43:湖南",
                        "44:广东", "45:广西", "46:海南", "50:重庆", "51:四川", "52:贵州", "53:云南", "54:西藏 ", "61:陕西", "62:甘肃",
                        "63:青海", "64:宁夏", "65:新疆", "71:台湾", "81:香港", "82:澳门", "91:国外"};

                boolean valideAddress = false;

                for (int i = 0; i < provinceArray.length; i++) {

                    String provinceKey = provinceArray[i].split(":")[0];

                    if (provinceKey.equals(address.substring(0, 2))) {

                        valideAddress = true;

                    }

                }

                if (valideAddress) {

                    String year = birthday.substring(0, 4);

                    String month = birthday.substring(4, 6);

                    String day = birthday.substring(6);

                    Date tempDate = new Date(Integer.parseInt(year), Integer.parseInt(month) - 1,
                            Integer.parseInt(day));

                    if ((tempDate.getYear() != Integer.parseInt(year)
                            || tempDate.getMonth() != Integer.parseInt(month) - 1
                            || tempDate.getDate() != Integer.parseInt(day))) {// 避免千年虫问题

                        System.err.println("身份证号码无效，请重新输入！！！");

                    } else {

                        int[] weightedFactors = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};// 加权因子

                        int[] valideCode = {1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};// 身份证验证位值，其中10代表X

                        int sum = 0;// 声明加权求和变量

                        String[] cardnoArray = new String[cardno.length()];

                        for (int i = 0; i < cardno.length(); i++) {

                            cardnoArray[i] = String.valueOf(cardno.charAt(i));

                        }

                        if ("x".equals(cardnoArray[17].toLowerCase())) {

                            cardnoArray[17] = "10";// 将最后位为x的验证码替换为10

                        }

                        for (int i = 0; i < 17; i++) {

                            sum += weightedFactors[i] * Integer.parseInt(cardnoArray[i]);// 加权求和

                        }

                        int valCodePosition = sum % 11;// 得到验证码所在位置

                        if (Integer.parseInt(cardnoArray[17]) == valideCode[valCodePosition]) {

                            String sex = "男";

                            if (Integer.parseInt(sequenceCode) % 2 == 0) {

                                sex = "女";

                            }

                            ReadActivity.log( "身份证号码有效，性别为：" + sex + "！");

                            flag = true;

                        } else {

                            System.err.println("身份证号码无效，请重新输入！！！");

                        }

                    }
                }

            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;

    }

    /**
     * 判断是否是Boolean数据类型
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isBoolean(Object inputString) {
        String value = CommFun.toString(inputString).toUpperCase();
        return "TRUE".equals(value) || "FALSE".equals(value) || "1".equals(value) || "0".equals(value);
    }

    /**
     * 判断是否是字母或数字或下划线或汉字
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isLettle(Object inputString) {
        Pattern pat = Pattern.compile(RegexString.Letter);
        Matcher mat = pat.matcher(CommFun.toString(inputString));
        return mat.find();
    }

    /**
     * 指定的数据是否为Null或string.Empty
     *
     * @param inputString 输入数据
     * @return 是否符合
     */
    public static Boolean isNullOrEmpty(Object inputString) {
        return inputString == null || "".equals(inputString.toString().trim()) || "null".equals(inputString.toString().trim());
    }


    /**
     * 指定输入的多个数据的字符串值是否相等，突略大小写
     * @return 是否符合
     */
    public static Boolean isEquals(Object[] obj) {
        int count = obj != null ? obj.length : 0;
        Boolean dataEquals = true;
        for (int i = 1; i < count; i++) {
            if (CommFun.toString(obj[i]).toLowerCase() != CommFun.toString(obj[0]).toLowerCase()) {
                dataEquals = false;
                break;
            }
        }
        return dataEquals;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param arg0
     * @param arg1
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static boolean isEquals(String arg0, String arg1) {
        Boolean dataEquals = false;
        try {
            if (CommFun.toString(arg0).toLowerCase().equals(CommFun.toString(arg1).toLowerCase())) {
                dataEquals = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return dataEquals;
    }

    /***************************************** 数据类型转换 *************************************************/
    /**
     * 将指定数据转化为字符串类型,当为Null时返回string.Empty,返回时将去除前后空格
     *
     * @param value 需转化的数据
     * @return 字符串
     */
    public static String toString(Object value) {
        return toString(value, true);
    }

    public static String toString(Object value, String def) {
        String str = toString(value, true);
        return isNullOrEmpty(str) ? def : str;
    }

    /**
     * 将指定数据转化为字符串类型,当为Null时返回string.Empty,返回时将去除前后空格
     *
     * @param value 需转化的数据
     * @param trim  是否去除两端空格
     * @return 字符串
     */
    public static String toString(Object value, Boolean trim) {
        String str = "";
        try {
            if (!CommFun.isNullOrEmpty(value)) {
                str = value.toString();
                if (trim)
                    str = str.trim();
            }
            str = getPlainString(str);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return str;
    }

    /**
     * 将科学计数法转为普通计数法
     *
     * @param value
     * @return
     */
    public static String getPlainString(String value) {
        try {
            if (value.length() >= 8 || value.contains("E")) {

                BigDecimal bigDecimal = new BigDecimal(value);
                value = bigDecimal.toPlainString();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return value;

    }

    /**
     * 将指定数据转化为整数类型，当转换失败时返回指定的返回值
     *
     * @param value       需转化的数据
     * @param returnValue 转换失败时的返回值
     * @return 整数类型数据
     */
    public static int toInt32(Object value, int returnValue) {
        try {
            if (CommFun.isNumber(CommFun.toString(value).trim()) && CommFun.isInteger(value)) {
                return Integer.parseInt(CommFun.toString(value));
            } else if (CommFun.isBoolean(value)) {
                return CommFun.toBoolean(value, false) ? 1 : 0;
            } else if (CommFun.isNumber(CommFun.toString(value).trim())) {
                return (int) CommFun.toDouble(value, 0d);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     * 将指定数据转化为长整型，当转换失败时返回指定的返回值
     *
     * @param value       需转化的数据
     * @param returnValue 转换失败时的返回值
     * @return 整数类型数据
     */
    public static long toLong(Object value, long returnValue) {
        if (CommFun.isNumber(CommFun.toString(value).trim()) && CommFun.isInteger(value)) {
            return Long.parseLong(CommFun.toString(value));
        } else if (CommFun.isBoolean(value)) {
            return CommFun.toBoolean(value, false) ? 1 : 0;
        }
        return returnValue;
    }

    /**
     * 将指定数据转化为小数类型，当转换失败时返回指定的返回值
     *
     * @param value       需转化的数据
     * @param returnValue 转换失败时的返回值
     * @return 整小数类型数据类型数据
     */
    public static double toDouble(Object value, Double returnValue) {
        try {
            if (CommFun.isNumber(CommFun.toString(value).trim())) {
                double numbervalue = Double.parseDouble(CommFun.toString(value));
                numbervalue = toNumber(numbervalue, 2);
                return numbervalue;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return returnValue;
    }

    /**
     * 小数四舍五入
     *
     * @param num
     * @param pointNumber
     * @return
     */
    public static double toNumber(double num, int pointNumber) {
        if (Double.isNaN(num)) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(pointNumber, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num;
    }

    /**
     * 去除数据中的空格字符 包括前，后，中间的空格
     *
     * @param value 输入数据
     * @return 字符串
     */
    public static String toTrim(Object value) {
        String str = CommFun.toString(value).trim();
        return str.replace(" ", "");
    }

    /**
     * 将指定数据转化为BOOL类型，当转换失败时返回指定的返回值
     *
     * @param value       需转化的数据
     * @param returnValue 转换失败时的返回值
     * @return BOOL类型数据
     */
    public static Boolean toBoolean(Object value, Boolean returnValue) {
        if (isBoolean(value)) {
            String input = CommFun.toString(value).trim().toUpperCase();
            return ("TRUE".equals(input) || "1".equals(input));

        }
        return returnValue;
    }

    /**
     * 将Boolean类型数据转化为字符×或√
     *
     * @param value 输入数据
     * @return 返回√或×
     */
    public static String toBooleanStyle(Object value) {
        if (toBoolean(value, false)) {
            return "√";
        } else {
            return "×";
        }
    }

    /**
     * 将字符串转化成枚举对象
     */
    @SuppressWarnings("unchecked")
    public static Object toEnum(Class enumType, Object obj) {

        return Enum.valueOf(enumType, CommFun.toString(obj));
    }

    /***************************************** 数据其他处理 *************************************************/

    /**
     * 字符串填充
     *
     * @param str   要左填充的字符串
     * @param charc 要填充的字符
     * @param len   要求字符串的总长度
     * @return 填充后的字符串
     */
    public static String padding(String str, char charc, int len) {
        StringBuffer sbf = new StringBuffer("");

        for (int i = 0; i < len - str.length(); i++) {
            sbf.append(charc);
        }
        return sbf.toString() + str;
    }

    /**
     * 移除源字符串两端指定的字符串
     * @return
     */
    public static String trim(String src, String trimChars) {
        String retStr = src;
        if (src != null) {
            // 去头
            if (src.startsWith(trimChars)) {
                retStr = src.substring(trimChars.length());
            }
            // 去尾
            if (src.endsWith(trimChars)) {
                retStr = retStr.substring(0, retStr.length() - trimChars.length());
            }
        }
        return retStr;
    }

    /**
     * 将指定数据转化为日期类型，当转换失败时返回指定的返回值
     *
     * @param value       需转化的数据
     * @param returnValue 转换失败时的返回值
     * @return 日期类型数据 yyyy-MM-dd- HH:mm:ss
     */
    public static Date toDateTime(Object value, Date returnValue) {
        return CommFun.toDate(value, null, returnValue);
    }

    /**
     * 将指定数据转化为日期类型，当转换失败时返回指定的返回值
     *
     * @param value       需转化的数据
     * @param formatStr   转换格式
     * @param returnValue 转换失败时的返回值
     * @return 日期类型数据 yyyy-MM-dd- HH:mm:ss
     */
    private static Date toDate(Object value, String formatStr, Date returnValue) {

        try {
            DateFormat format = null;
            if (CommFun.isDateTime(CommFun.toString(value).trim())) {
                formatStr = CommFun.toString(formatStr).length() == 0 ? "yyyy-MM-dd HH:mm:ss" : formatStr;
                format = new SimpleDateFormat(formatStr);
                return format.parse(CommFun.toString(value));
            }
            if (CommFun.isShortDate(CommFun.toString(value).trim())) {
                formatStr = CommFun.toString(formatStr).length() == 0 ? "yyyy-MM-dd" : formatStr;
                format = new SimpleDateFormat(formatStr);
                return format.parse(CommFun.toString(value));
            }
            return format.parse(format.format(returnValue));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     */
    public static String getMonthStr(Date date) {
        try {
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                int day = calendar.get(Calendar.MONTH) + 1;
                switch (day) {
                    case 1:
                        return "一月";
                    case 2:
                        return "二月";
                    case 3:
                        return "三月";
                    case 4:
                        return "四月";
                    case 5:
                        return "五月";
                    case 6:
                        return "六月";
                    case 7:
                        return "七月";
                    case 8:
                        return "八月";
                    case 9:
                        return "九月";
                    case 10:
                        return "十月";
                    case 11:
                        return "十一月";
                    case 12:
                        return "十二月";

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将指定数据转化为日期类型，当转换失败时返回指定的返回值
     *
     * @param value       需转化的数据
     * @param format   转换格式
     * @param defValue 转换失败时的返回值
     * @return 日期类型数据 yyyy-MM-dd HH:mm:ss
     */
    public static String toDateString(Date value, String format, Date defValue) {
        if (value == null) {
            if (!isDateTime(defValue)) {
                return CommFun.toString(defValue);
            }
            value = CommFun.toDate(value, format, defValue);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(value);

    }

    /**
     * 获取系统当前日期，自动转东八区
     *
     * @return
     */
    public static Date getDate() {
        try {
            TimeZone zone = TimeZone.getTimeZone("GMT-8:00");
            Calendar cal = Calendar.getInstance(zone);
            return cal.getTime();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 获取当前时间字符串
     *
     * @param formatStr yyyyMMddHHmmssSSS yyyy-MM-dd HH:mm:ss
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateString(String formatStr) {
        try {
            return toDateString(getDate(), formatStr, new Date());
        } catch (Exception e) {
            // TODO: handle exception
        }

        return "";

    }

    /**
     * 格式化日期
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String toDateString(Date date, String formatStr) {
        try {
            return toDateString(date, formatStr, new Date());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    /**
     * 将日期字符串转为指定格式
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static String toDateString(String dateStr, String formatStr) {

        Date date = toDate(dateStr);

        return toDateString(date, formatStr, new Date());

    }


    /**
     * 将时间戳转为日期
     *
     * @param timestamp
     * @return
     */
    public static Date stampToDate(String timestamp) {
        try {
            long time = System.currentTimeMillis();
            long lt = new Long(timestamp + "000");
            Date date = new Date(lt);
            return date;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 时间戳转为指定日期格式字符串
     *
     * @param timestamp 时间戳
     * @param formatStr 日期格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String stampToDateStr(String timestamp, String formatStr) {
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);

            Date date = stampToDate(timestamp);
            return simpleDateFormat.format(date);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return timestamp;
    }

    /**
     * 根据日期字符串获取日期
     *
     * @param dateStr
     * @return
     */
    public static Date toDate(String dateStr) {

        Date date = null;

        try {
            HashMap<String, String> dateRegFormat = new HashMap<String, String>();
            dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$",
                    "yyyy-MM-dd-HH-mm-ss");// 2014年3月12日
            // 13时5分34秒，2014-03-12
            // 12:05:34，2014/3/12
            // 12:5:34
            dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH-mm");// 2014-03-12
            // 12:05
            dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH");// 2014-03-12
            // 12
            dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");// 2014-03-12
            dateRegFormat.put("^\\d{4}\\D+\\d{2}$", "yyyy-MM");// 2014-03
            dateRegFormat.put("^\\d{4}$", "yyyy");// 2014
            dateRegFormat.put("^\\d{17}$", "yyyyMMddHHmmssSSS");// 20140312120534
            dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");// 20140312120534
            dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");// 201403121205
            dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");// 2014031212
            dateRegFormat.put("^\\d{8}$", "yyyyMMdd");// 20140312
            dateRegFormat.put("^\\d{6}$", "yyyyMM");// 201403
            dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm-ss");// 13:05:34
            // 拼接当前日期
            dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm");// 13:05
            // 拼接当前日期
            dateRegFormat.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");// 14.10.18(年.月.日)
            dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}$", "yyyy-dd-MM");// 30.12(日.月)
            // 拼接当前年份
            dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");// 12.21.2013(日.月.年)

            String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            DateFormat formatter2;
            String dateReplace;
            String strSuccess = "";
            try {
                for (String key : dateRegFormat.keySet()) {
                    if (Pattern.compile(key).matcher(dateStr).matches()) {
                        formatter2 = new SimpleDateFormat(dateRegFormat.get(key));
                        if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$")
                                || key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {// 13:05:34
                            // 或
                            // 13:05
                            // 拼接当前日期
                            dateStr = curDate + "-" + dateStr;
                        } else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {// 21.1
                            // (日.月)
                            // 拼接当前年份
                            dateStr = curDate.substring(0, 4) + "-" + dateStr;
                        }
                        dateReplace = dateStr.replaceAll("\\D+", "-");

                        date = formatter2.parse(dateReplace);
                        // System.out.println(dateRegExpArr[i]);
                        strSuccess = formatter1.format(formatter2.parse(dateReplace));
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("-----------------日期格式无效:" + dateStr);
                throw new Exception("日期格式无效");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return date;
    }

    /**
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String toDateFormatDuring(long mss) {
        String formatDate = "";
        try {
            long days = mss / (1000 * 60 * 60 * 24);
            long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (mss % (1000 * 60)) / 1000;
            if (days != 0)
                formatDate += days + " days ";
            if (hours != 0)
                formatDate += hours + " hours ";
            if (minutes != 0)
                formatDate += minutes + " minutes ";
            if (seconds != 0)
                formatDate += seconds + " seconds ";
            // return days + " days " + hours + " hours " + minutes +
            // " minutes "
            // + seconds + " seconds ";
        } catch (Exception e) {
            // TODO: handle exception
        }

        return formatDate;

    }

    public static String toDateFormatDuring_1(long mss) {
        String formatDate = "";
        try {
            long days = mss / (1000 * 60 * 60 * 24);
            long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (mss % (1000 * 60)) / 1000;
            if (days != 0) {
                formatDate += days + " 天 ";
                return formatDate;
            }
            if (hours != 0) {
                formatDate += hours + " 小时 ";
                return formatDate;
            }
            if (minutes != 0) {
                formatDate += minutes + " 分钟 ";
                return formatDate;
            }
            if (seconds != 0) {
                formatDate += seconds + " 秒 ";
                return formatDate;
            }
            // return days + " days " + hours + " hours " + minutes +
            // " minutes "
            // + seconds + " seconds ";
        } catch (Exception e) {
            // TODO: handle exception
        }

        return formatDate;

    }

    /**
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     * @author fy.zhang
     */
    public static String toDateFormatDuring(Date begin, Date end) {
        return toDateFormatDuring(end.getTime() - begin.getTime());
    }

    // / <summary>
    // / 指定一周的某天 获取中文名称
    // / </summary>
    // / <param name="dayOfWeek">DayOfWeek实体</param>
    // / <returns></returns>
    // public static string ToWeekName(DayOfWeek dayOfWeek)
    // {
    // switch (dayOfWeek)
    // {
    // case DayOfWeek.Friday:
    // return "星期五";
    // case DayOfWeek.Monday:
    // return "星期一";
    // case DayOfWeek.Saturday:
    // return "星期六";
    // case DayOfWeek.Sunday:
    // return "星期日";
    // case DayOfWeek.Thursday:
    // return "星期四";
    // case DayOfWeek.Tuesday:
    // return "星期二";
    // case DayOfWeek.Wednesday:
    // return "星期三";
    // }
    // return "星期日";
    // }

    /**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
     */
    public static String getDigitUppercase(double n) {
        try {
            String fraction[] = {"角", "分"};
            String digit[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
            String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

            String head = n < 0 ? "负" : "";
            n = Math.abs(n);

            String s = "";
            for (int i = 0; i < fraction.length; i++) {
                s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
            }
            if (s.length() < 1) {
                s = "整";
            }
            int integerPart = (int) Math.floor(n);

            for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
                String p = "";
                for (int j = 0; j < unit[1].length && n > 0; j++) {
                    p = digit[integerPart % 10] + unit[1][j] + p;
                    integerPart = integerPart / 10;
                }
                s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
            }
            return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零")
                    .replaceAll("^整$", "零元整");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "0";
    }

    // / <summary>
    // / 转换人民币大小金额
    // / </summary>
    // / <param name="num">金额</param>
    // / <returns>返回大写形式</returns>
    public static String ToRMB(Double num) {
        return "未实现";
        // String str1 = "零壹贰叁肆伍陆柒捌玖"; //0-9所对应的汉字
        // String str2 = "万仟佰拾亿仟佰拾万仟佰拾元角分"; //数字位所对应的汉字
        // String str3 = ""; //从原num值中取出的值
        // String str4 = ""; //数字的字符串形式
        // String str5 = ""; //人民币大写金额形式
        // int i; //循环变量
        // int j; //num的值乘以100的字符串长度
        // String ch1 = ""; //数字的汉语读法
        // String ch2 = ""; //数字位的汉字读法
        // int nzero = 0; //用来计算连续的零值是几个
        // int temp; //从原num值中取出的值
        //
        // num = Math.round(Math.abs(num));//.Round(Math.abs(num), 2);
        // //将num取绝对值并四舍五入取2位小数
        // str4 = ((long)(num * 100)).ToString(); //将num乘100并转换成字符串形式
        // j = str4.Length; //找出最高位
        // if (j > 15) { return "溢出"; }
        // str2 = str2.Substring(15 - j);
        // //取出对应位数的str2的值。如：200.55,j为5所以str2=佰拾元角分
        //
        // //循环取出每一位需要转换的值
        // for (i = 0; i < j; i++)
        // {
        // str3 = str4.Substring(i, 1); //取出需转换的某一位的值
        // temp = Convert.ToInt32(str3); //转换为数字
        // if (i != (j - 3) && i != (j - 7) && i != (j - 11) && i != (j - 15))
        // {
        // //当所取位数不为元、万、亿、万亿上的数字时
        // if (str3 == "0")
        // {
        // ch1 = "";
        // ch2 = "";
        // nzero = nzero + 1;
        // }
        // else
        // {
        // if (str3 != "0" && nzero != 0)
        // {
        // ch1 = "零" + str1.Substring(temp * 1, 1);
        // ch2 = str2.Substring(i, 1);
        // nzero = 0;
        // }
        // else
        // {
        // ch1 = str1.Substring(temp * 1, 1);
        // ch2 = str2.Substring(i, 1);
        // nzero = 0;
        // }
        // }
        // }
        // else
        // {
        // //该位是万亿，亿，万，元位等关键位
        // if (str3 != "0" && nzero != 0)
        // {
        // ch1 = "零" + str1.Substring(temp * 1, 1);
        // ch2 = str2.Substring(i, 1);
        // nzero = 0;
        // }
        // else
        // {
        // if (str3 != "0" && nzero == 0)
        // {
        // ch1 = str1.Substring(temp * 1, 1);
        // ch2 = str2.Substring(i, 1);
        // nzero = 0;
        // }
        // else
        // {
        // if (str3 == "0" && nzero >= 3)
        // {
        // ch1 = "";
        // ch2 = "";
        // nzero = nzero + 1;
        // }
        // else
        // {
        // if (j >= 11)
        // {
        // ch1 = "";
        // nzero = nzero + 1;
        // }
        // else
        // {
        // ch1 = "";
        // ch2 = str2.Substring(i, 1);
        // nzero = nzero + 1;
        // }
        // }
        // }
        // }
        // }
        // if (i == (j - 11) || i == (j - 3))
        // {
        // //如果该位是亿位或元位，则必须写上
        // ch2 = str2.Substring(i, 1);
        // }
        // str5 = str5 + ch1 + ch2;
        //
        // if (i == j - 1 && str3 == "0")
        // {
        // //最后一位（分）为0时，加上“整”
        // str5 = str5 + '整';
        // }
        // }
        // if (num == 0)
        // {
        // str5 = "零元整";
        // }
        // return str5;
    }

    // / <summary>
    // / 实体序列化为JSON
    // / </summary>
    // / <param name="val">实体</param>
    // / <returns></returns>
    // public static String ToJson(Object val)
    // {
    // if (val != null)
    // return Newtonsoft.Json.JavaScriptConvert.SerializeObject(val);
    // return "";
    //
    // }

    // / <summary>
    // / JSON序列化为实体
    // / </summary>
    // / <typeparam name="T">实体</typeparam>
    // / <param name="val">JSON串</param>
    // / <returns></returns>
    // public static Object ToJson<>(string val)
    // {
    // if (CommFun.IsNullOrEmpty(val))
    // return Newtonsoft.Json.JavaScriptConvert.DeserializeObject(val,
    // typeof(T));
    // return null;
    //
    // }

}