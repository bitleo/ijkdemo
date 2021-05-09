package top.bitleo.ijkdemo.http


class HttpResponseModel<T> {
    var code:Int=0
    var status:String?=null
    var message:String?=null
    var data:T?=null
}