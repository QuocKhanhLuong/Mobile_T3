import kotlin.math.abs
import java.lang.NumberFormatException

// Hàm tìm ước chung lớn nhất (UCLN) bằng thuật toán Euclid.
fun timUCLN(a: Int, b: Int): Int {
    var num1 = abs(a)
    var num2 = abs(b)
    while (num2 != 0) {
        val temp = num1 % num2
        num1 = num2
        num2 = temp
    }
    return num1
}

/**
 * Lớp biểu diễn một phân số và các phép toán liên quan.
 * @property tuSo Tử số.
 * @property mauSo Mẫu số.
 */
class PhanSo(var tuSo: Int = 0, var mauSo: Int = 1) {

    // Nhập và xác thực dữ liệu cho phân số từ người dùng.
    fun nhap() {
        while (true) {
            print("Nhập tử số: ")
            try {
                tuSo = readln().toInt()
                break
            } catch (e: NumberFormatException) {
                println("Lỗi: Vui lòng nhập một số nguyên.")
            }
        }

        while (true) {
            print("Nhập mẫu số (khác 0): ")
            try {
                mauSo = readln().toInt()
                if (mauSo == 0) {
                    println("Lỗi: Mẫu số phải khác 0.")
                } else {
                    break
                }
            } catch (e: NumberFormatException) {
                println("Lỗi: Vui lòng nhập một số nguyên.")
            }
        }
    }

    // In phân số theo định dạng tuSo/mauSo.
    fun inPhanSo() {
        print("$tuSo/$mauSo")
    }

    // Tối giản phân số và đảm bảo mẫu số luôn dương.
    fun toiGian() {
        if (mauSo < 0) {
            tuSo = -tuSo
            mauSo = -mauSo
        }
        val ucln = timUCLN(tuSo, mauSo)
        tuSo /= ucln
        mauSo /= ucln
    }

    // So sánh hai phân số bằng cách nhân chéo để tránh sai số khi chia.
    fun soSanh(other: PhanSo): Int {
        return (this.tuSo * other.mauSo).compareTo(other.tuSo * this.mauSo)
    }

    // Cộng phân số hiện tại với một phân số khác và trả về kết quả đã tối giản.
    fun tinhTong(other: PhanSo): PhanSo {
        val tuSoMoi = this.tuSo * other.mauSo + other.tuSo * this.mauSo
        val mauSoMoi = this.mauSo * other.mauSo
        val ketQua = PhanSo(tuSoMoi, mauSoMoi)
        ketQua.toiGian()
        return ketQua
    }
}

fun main() {
    // 1. Nhập số lượng phân số
    var n = 0
    while(true) {
        print("Nhập số lượng phân số: ")
        try {
            n = readln().toInt()
            if (n > 0) break else println("Số lượng phải lớn hơn 0.")
        } catch (e: NumberFormatException) {
            println("Vui lòng nhập một con số.")
        }
    }

    // 2. Nhập mảng phân số
    val danhSachPhanSo = mutableListOf<PhanSo>()
    for (i in 1..n) {
        println("\n--- Nhập phân số thứ $i ---")
        val ps = PhanSo()
        ps.nhap()
        danhSachPhanSo.add(ps)
    }

    println("\n--- Mảng phân số ban đầu ---")
    danhSachPhanSo.forEach { it.inPhanSo(); print("  ") }
    println()

    // 3. Tối giản các phân số
    danhSachPhanSo.forEach { it.toiGian() }
    println("\n--- Mảng sau khi tối giản ---")
    danhSachPhanSo.forEach { it.inPhanSo(); print("  ") }
    println()

    // 4. Tính tổng các phân số
    var tong = PhanSo(0, 1)
    danhSachPhanSo.forEach { tong = tong.tinhTong(it) }
    println("\n--- Tổng các phân số ---")
    print("Tổng = ")
    tong.inPhanSo()
    println()

    // 5. Tìm phân số lớn nhất
    if (danhSachPhanSo.isNotEmpty()) {
        val max = danhSachPhanSo.reduce { acc, phanSo -> if (phanSo.soSanh(acc) > 0) phanSo else acc }
        println("\n--- Phân số lớn nhất ---")
        print("Giá trị lớn nhất là: ")
        max.inPhanSo()
        println()
    }

    // 6. Sắp xếp mảng giảm dần
    danhSachPhanSo.sortWith { ps1, ps2 -> ps2.soSanh(ps1) }
    println("\n--- Mảng sau khi sắp xếp giảm dần ---")
    danhSachPhanSo.forEach { it.inPhanSo(); print("  ") }
    println()
}