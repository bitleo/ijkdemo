package top.bitleo.ijkdemo


/**
 * Created by shadow
 * Time: 2019/4/30
 */
object AppConfig {


    val CRISP_WEBSITE_ID: String = "e3e204c3-0cd7-4066-ade8-69c80f7bea63"
    val HTTP_TIME_OUT = 10 * 1000

    val HTTP_MAX_CACHE_SIZE = 32 * 1024 * 1024

    val IMAGE_MAX_CACHE_SIZE = 32 * 1024 * 1024

    val CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60


    val DB_NAME = "ssr_client.db"

    val INTERNAL_DOMAINS = "thorvpn.me,thorvpn.app"

    val DEFAULT_HOST = "https://thorvpn.me"

    var API_HOST=DEFAULT_HOST

    var WECHAT_PAY_HOST= API_HOST

    var API_BASE_URL = "$API_HOST/api/v2/"

    var API_BASE_URL_V3 = "$API_HOST/api/v3/"

    val PING_TIME_MAX = 800

    val USE_DISABLE_SELF_VPN = true

    val PING_WEIGHT_MAX = 500.0f

    val PING_WEIGHT_VALUE = 55

    val BANDUSED_WEIGHT_VALUE = 45

    val PACKAGE_NAME = "thor.vpn.free.secure.proxy"

    val AD_TIME_NEW = 10*60*1000 //10min

    fun resetDomain(domain:String){
        if(domain.startsWith("https://https://")){
            val newdomain = domain.replace("https://https://","https://")
            API_HOST = newdomain
        }
        else if(domain.startsWith("https://")){
            API_HOST = domain
        }else{
            API_HOST = "https://$domain"
        }


        API_BASE_URL = "$API_HOST/api/v2/"
        API_BASE_URL_V3 = "$API_HOST/api/v3/"

    }





}