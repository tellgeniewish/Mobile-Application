package ddwu.com.mobileapplication.finalproject.data

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

@Root(name = "response", strict = false)
data class Root(
    @field:Element(name = "header", required = false)
    var header: Header? = null,

    @field:Element(name = "body", required = false)
    var body: Body? = null
)

@Root(name = "header", strict = false)
data class Header(
    @field:Element(name = "resultMsg", required = false)
    var resultMsg: String? = null,

    @field:Element(name = "resultCode", required = false)
    var resultCode: String? = null
)

@Root(name = "body", strict = false)
data class Body(
    @field:Element(name = "totalCount", required = false)
    var totalCount: Int? = null,

    @field:Element(name = "pageNo", required = false)
    var pageNo: Int? = null,

    @field:Element(name = "numOfRows", required = false)
    var numOfRows: Int? = null,

    @field:Element(name = "items", required = false)
    var items: Items? = null  // 🌟🌟🌟
)

@Root(name = "items", strict = false)
data class Items(  // 🌟🌟🌟
    @field:ElementList(name = "item", inline = true, required = false)
    var mdList: List<Medical>? = null
)

@Root(name = "item", strict = false)
data class Medical(
    @field:Element(name = "instit_nm", required = false)
    var institNm: String? = null,

    @field:Element(name = "instit_kind", required = false)
    var institKind: String? = null,

    @field:Element(name = "medical_instit_kind", required = false)
    var medicalInstitKind: String? = null,

    @field:Element(name = "zip_code", required = false)
    var zipCode: String? = null,

    @field:Element(name = "street_nm_addr", required = false)
    var streetNmAddr: String? = null,

    @field:Element(name = "tel", required = false)
    var tel: String? = null,

    @field:Element(name = "organ_loc", required = false)
    var organLoc: String? = null,

    @field:Element(name = "holiday", required = false)
    var holiday: String? = null,

    @field:Element(name = "sunday_oper_week", required = false)
    var sundayOperWeek: String? = null,

    @field:Element(name = "exam_part", required = false)
    var examPart: String? = null,

    @field:Element(name = "regist_dt", required = false)
    var registDt: String? = null,

    @field:Element(name = "update_dt", required = false)
    var updateDt: String? = null,

    @field:Element(name = "lng", required = false)
    var lng: Double? = null,

    @field:Element(name = "lat", required = false)
    var lat: Double? = null,

    @field:Element(name = "monday", required = false)
    var monday: String? = null,

    @field:Element(name = "tuesday", required = false)
    var tuesday: String? = null,

    @field:Element(name = "wednesday", required = false)
    var wednesday: String? = null,

    @field:Element(name = "thursday", required = false)
    var thursday: String? = null,

    @field:Element(name = "friday", required = false)
    var friday: String? = null,

    @field:Element(name = "saturday", required = false)
    var saturday: String? = null,

    @field:Element(name = "sunday", required = false)
    var sunday: String? = null
): Serializable{
    // instit_nm, instit_kind, tel을 결합한 toString 메서드 추가
    override fun toString(): String {
        return "기관명: ${institNm ?: "정보 없음"}\n종류: ${institKind ?: "정보 없음"}\n전화번호: ${tel ?: "정보 없음"}"
    }
}