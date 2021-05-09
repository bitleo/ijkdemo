package top.bitleo.ijkdemo.util
import android.text.TextUtils
import android.util.Base64
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.abs


object StringUtils {

    private val DATE_REGEX_MAP = HashMap<Locale, String>()

    private val RECORD_DATE_REGEX_MAP = HashMap<Locale, String>()

    fun isBlank(value:String):Boolean{
        return value.isBlank();
    }

    fun isBlankList(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }

    fun stringToList(str: String, separator: String): List<String>? {
        var list: List<String>? = null
        if (!str.contains(separator)) {
            return list
        }
        val strs = str.split(separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        list = listOf(*strs)
        return list
    }

    fun listToString(list: List<String>, separator: String): String {
        val stringBuilder = StringBuilder("")
        if (list.isEmpty() || separator.isBlank()) {
            return stringBuilder.toString()
        }
        for (i in list.indices) {
            stringBuilder.append(list[i])
            if (i != list.size - 1) {
                stringBuilder.append(separator)
            }
        }
        return stringBuilder.toString()
    }

    fun getSizeString(size: Long): String? {
        if (size < 1024) {
            return String.format(Locale.getDefault(), "%d B", size)
        } else if (size < 1024 * 1024) {
            val sizeK = size / 1024f
            return String.format(Locale.getDefault(), "%.2f KB", sizeK)
        } else if (size < 1024 * 1024 * 1024) {
            val sizeM = size / (1024f * 1024f)
            return String.format(Locale.getDefault(), "%.2f MB", sizeM)
        } else if (size / 1024 < 1024 * 1024 * 1024) {
            val sizeG = size / (1024f * 1024f * 1024f)
            return String.format(Locale.getDefault(), "%.2f GB", sizeG)
        }
        return null
    }

    fun getDateByTime(time: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = time
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }


    fun upCaseFirstChar(str: String): String? {
        return if (isBlank(str)) null else str.substring(0, 1).toUpperCase() + str.substring(1)
    }

    fun hideMiddleString(str:String):String{
        return  str.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }

    fun getPriceString(price:Double):String{
        var intPrice =  price.toInt()
        if(price-intPrice>0){
            return "￥$price"
        }else{
            return "￥$intPrice"
        }

    }

    fun getDateTimeString(date: Date,local:Locale = Locale.CHINA):String{
        var str = RECORD_DATE_REGEX_MAP[local]
        if(str==null){
            if(local.language ==Locale.CHINA.language){
                str = RECORD_DATE_REGEX_MAP[Locale.CHINA]
            }else{
                str = RECORD_DATE_REGEX_MAP[Locale.ENGLISH]
            }
        }
        val sdf = SimpleDateFormat(str)
        return sdf.format(date)
    }

    fun getBase64String(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun getBase64Bytes(str: String):ByteArray{
        return Base64.decode(str,Base64.DEFAULT)
    }

    fun isBase64Img(imgurl:String):Boolean{
        if(!TextUtils.isEmpty(imgurl)&&(imgurl.startsWith("data:image/png;base64,")
                    ||imgurl.startsWith("data:image/*;base64,")||imgurl.startsWith("data:image/jpg;base64,")||imgurl.startsWith("data:image/jpeg;base64,")
                    ))
        {
            return true;
        }
        return false;
    }

    fun getMemberComboContent(content:String?,readPrice:Boolean = false):String{

        content?.let {
             if(readPrice){
                 val startIndex = content.indexOf("#");
                 val endIndex = content.lastIndexOf("#");
                 if(startIndex>0&&endIndex>0&&startIndex<endIndex){
                     return content.substring(startIndex+1,endIndex)
                 }
             }else{
                 val index = content.indexOf("#");
                 return if(index>0){
                     content.substring(0,index)
                 }else{
                     content
                 }
             }
        }
        return ""
    }

    fun getDateStrAmPm(date: Date): String? {
        val aa = SimpleDateFormat("KK:mm aa", Locale.ENGLISH)
        return aa.format(date).toUpperCase()
    }

    fun getAmPmForm(dateStr:String?,regex:String ="yyyy-MM-dd HH:mm:ss"):String?{
        if(dateStr.isNullOrEmpty()){
            return ""
        }
        try{
            val format = SimpleDateFormat(regex)
            val date = format.parse(dateStr)
            return getDateStrAmPm(date)
        }catch (_:Exception){

        }
        return dateStr

    }

    fun getDateString(date: Date, regex:String ="yyyy-MM-dd HH:mm:ss"): String {

        val format = SimpleDateFormat(regex)
        return format.format(date)
    }

    init {
        DATE_REGEX_MAP[Locale.CHINA] = "yyyy-MM-dd"
        DATE_REGEX_MAP[Locale.TAIWAN] = "yyyy-MM-dd"
        DATE_REGEX_MAP[Locale.ENGLISH] = "MMM d, yyyy"
        DATE_REGEX_MAP[Locale.GERMAN] = "dd.MM.yyyy"
        DATE_REGEX_MAP[Locale.GERMANY] = "dd.MM.yyyy"
        RECORD_DATE_REGEX_MAP[Locale.CHINA] = "yyyy年MM月dd日 HH:mm"
        RECORD_DATE_REGEX_MAP[Locale.ENGLISH] = "yyyy-MM-dd  HH:mm"

    }


    fun getHostFrom(url: String): String {
        try{
            val urls = java.net.URL(url)
            return urls.host// 结果 blog.csdn.net
        }catch (_:Exception){

        }
       return ""
    }



    // 邮箱验证
    fun  emailFormat(email:String): Boolean{
        val pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        val mc = pattern.matcher(email);
        return mc.matches();
    }

    //时间分转天数
     fun getTimeStringFromMinute(minute: Int): String {

        //int.TryParse(minuteString, out minute);
        if (minute < 0) {
            return ""
        }
        var time: String = ""
        val day = minute / 60 / 24
        val hour = minute / 60 % 24
        val min = minute % 60
        if (day > 0) {
            time += day.toString() + "天"
        }
        if (hour > 0) {
            time += hour.toString() + "小时"
        }
        if (min > 0) {
            time += min.toString() + "分钟"
        }
        return time
    }




    fun compareVersion(v1: String, v2: String): Boolean {
        if (v1 == null || v1.isEmpty() || v2 == null || v2.isEmpty()) return false

        val version1 = v1.split(".")
        val version2 = v2.split(".")

        if(version1[0].toInt()>version2[0].toInt()){
            return true
        }else if((version1[0].toInt()==version2[0].toInt())&&(version1[1].toInt()>version2[1].toInt())){
            return true
        }else if((version1[0].toInt()==version2[0].toInt())&&(version1[1].toInt()==version2[1].toInt())&&(version1[2].toInt()>version2[2].toInt())){
            return  true
        }


        return false
    }

    fun phoneFormat(phone: String): Boolean {

        val regExp = "^\\d{11}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.matches()
    }
}
object Hash {

    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return printHexBinary(bytes)
    }

    fun printHexBinary(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(HEX_CHARS[i shr 4 and 0xF])
            r.append(HEX_CHARS[i and 0xF])
        }
        return r.toString()
    }
}