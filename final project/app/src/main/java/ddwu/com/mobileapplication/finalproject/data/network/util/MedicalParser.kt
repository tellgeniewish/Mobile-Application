package ddwu.com.mobileapplication.finalproject.data.network.util
//
//import android.util.Xml
//import ddwu.com.mobileapplication.finalproject.data.Medical
//import org.xmlpull.v1.XmlPullParser
//import org.xmlpull.v1.XmlPullParserException
//import java.io.IOException
//import java.io.InputStream
//
//class MedicalParser {
//    private val ns: String? = null
//
//    companion object {
//        /*Parsing 에 사용할 TAG 정적상수 선언*/
//        val UPPER_TAG = "response"
//        val BODY_TAG = "body"
//        val ITEMS_TAG = "items"
//
//        val ITEM_TAG = "item"
//        val INSTIT_NM_TAG = "instit_nm"
//        val INSTIT_KIND_TAG = "instit_kind"
//
//        val STREET_ADDR_TAG = "street_nm_addr"
//        val TEL_TAG = "tel"
//
//        val LNG_TAG = "lng"
//        val LAT_TAG = "lat"
//
//        val MONDAY_TAG = "monday"
//        val TUESDAY_TAG = "tuesday"
//        val WEDNESDAY_TAG = "wednesday"
//        val THURSDAY_TAG = "thursday"
//        val FRIDAY_TAG = "friday"
//        val SATURDAY_TAG = "saturday"
//        val SUNDAY_TAG = "sunday"
//    }
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    fun parse(inputStream: InputStream?) : List<Medical> {
//
//        inputStream.use { inputStream ->
//            val parser : XmlPullParser = Xml.newPullParser()
//
//            /*Parser 의 동작 정의, next() 호출 전 반드시 호출 필요*/
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
//
//            /* Paring 대상이 되는 inputStream 설정 */
//            parser.setInput(inputStream, null)
//
//            /*Parsing 대상 태그의 상위 태그까지 이동*/
//            while (parser.name != UPPER_TAG/*상위태그*/) {
//                parser.next()
//            }
//
//            return readItems(parser)
//        }
//    }
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun readItems(parser: XmlPullParser) : List<Medical> {
//        val medicals = mutableListOf<Medical>()
//
//        parser.require(XmlPullParser.START_TAG, ns,  UPPER_TAG/*상위태그*/)
//        while(parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
////            if (parser.name == ITEM_TAG/*항목태그*/) {
////                medicals.add( readItem(parser) )
////            } else {
////                skip(parser)
////            }
//            if (parser.name == BODY_TAG) {
//                // items 태그를 찾음
//                while (parser.next() != XmlPullParser.END_TAG) {
//                    if (parser.eventType != XmlPullParser.START_TAG) {
//                        continue
//                    }
//
//                    if (parser.name == ITEMS_TAG) {
//                        // item 태그들 순차적으로 처리
//                        while (parser.next() != XmlPullParser.END_TAG) {
//                            if (parser.eventType != XmlPullParser.START_TAG) {
//                                continue
//                            }
//
//                            if (parser.name == ITEM_TAG) {
//                                medicals.add(readItem(parser))  // instit_nm 태그 내용 추출
//                            } else {
//                                skip(parser)
//                            }
//                        }
//                    } else {
//                        skip(parser)
//                    }
//                }
//            } else {
//                skip(parser)
//            }
//        }
//
//        return medicals
//    }
//
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun readItem(parser: XmlPullParser) : Medical {
//        parser.require(XmlPullParser.START_TAG, ns, ITEM_TAG/*항목태그*/)
//
//        /*Parsing 한 TEXT 값을 저장할 변수 선언*/
//        var instit_nm: String? = null
//        var instit_kind: String? = null
//
//        var street_nm_addr: String? = null
//        var tel: String? = null
//        var lng: Double? = null
//        var lat: Double? = null
//
//        var monday: String? = null
//        var tuesday: String? = null
//        var wednesday: String? = null
//        var thursday: String? = null
//        var friday: String? = null
//        var saturday: String? = null
//        var sunday: String? = null
//
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//            when (parser.name) {
//                /*TAG 명에 따라 변수에 TEXT 저장*/
//                INSTIT_NM_TAG -> instit_nm = readTextInTag(parser, INSTIT_NM_TAG)
//                INSTIT_KIND_TAG -> instit_kind = readTextInTag(parser, INSTIT_KIND_TAG)
//
//                STREET_ADDR_TAG -> street_nm_addr = readTextInTag(parser, STREET_ADDR_TAG)
//                TEL_TAG -> tel = readTextInTag(parser, TEL_TAG)
//                LNG_TAG -> lng = readTextInTag(parser, LNG_TAG).toDouble()
//                LAT_TAG -> lat = readTextInTag(parser, LAT_TAG).toDouble()
//
//                MONDAY_TAG -> monday = readTextInTag(parser, MONDAY_TAG)
//                TUESDAY_TAG -> tuesday = readTextInTag(parser, TUESDAY_TAG)
//                WEDNESDAY_TAG -> wednesday = readTextInTag(parser, WEDNESDAY_TAG)
//                THURSDAY_TAG -> thursday = readTextInTag(parser, THURSDAY_TAG)
//                FRIDAY_TAG -> friday = readTextInTag(parser, FRIDAY_TAG)
//                SATURDAY_TAG -> saturday = readTextInTag(parser, SATURDAY_TAG)
//                SUNDAY_TAG -> sunday = readTextInTag(parser, SUNDAY_TAG)
//
//                else -> skip(parser)
//            }
//        }
//        /*저장한 변수로 Movie 생성*/
//        return Medical(instit_nm, instit_kind, street_nm_addr, tel, lng, lat, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
//    }
//
//
//    @Throws(IOException::class, XmlPullParserException::class)
//    private fun readTextInTag (parser: XmlPullParser, tag: String): String {
//        parser.require(XmlPullParser.START_TAG, ns, tag)
//        var text = ""
//        if (parser.next() == XmlPullParser.TEXT) {
//            text = parser.text
//            parser.nextTag()
//        }
//        parser.require(XmlPullParser.END_TAG, ns, tag)
//        return text
//    }
//
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun skip(parser: XmlPullParser) {
//        if (parser.eventType != XmlPullParser.START_TAG) {
//            throw IllegalStateException()
//        }
//        var depth = 1
//        while (depth != 0) {
//            when (parser.next()) {
//                XmlPullParser.END_TAG -> depth--
//                XmlPullParser.START_TAG -> depth++
//            }
//        }
//    }
//}