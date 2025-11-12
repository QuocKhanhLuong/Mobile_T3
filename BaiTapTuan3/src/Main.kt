import kotlin.math.abs

class PhanSo(var tuSo: Int, var mauSo: Int) : Comparable<PhanSo> {

    constructor() : this(0, 1)

    override fun toString(): String {
        return "$tuSo/$mauSo"
    }

    fun nhap() {
        print(" - Nhập tử số: ")
        this.tuSo = readln().toInt()
        do {
            print(" - Nhập mẫu số (phải khác 0): ")
            this.mauSo = readln().toInt()
            if (this.mauSo == 0) {
                println("Mẫu số phải khác 0. Vui lòng nhập lại.")
            }
        } while (this.mauSo == 0)
    }

    private fun timUCLN(a: Int, b: Int): Int {
        var num1 = abs(a)
        var num2 = abs(b)
        while (num2 != 0) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }
        return num1
    }

    fun rutGon() {
        val ucln = timUCLN(this.tuSo, this.mauSo)
        this.tuSo /= ucln
        this.mauSo /= ucln
        if (this.mauSo < 0) {
            this.tuSo *= -1
            this.mauSo *= -1
        }
    }

    operator fun plus(psKhac: PhanSo): PhanSo {
        val newTuSo = this.tuSo * psKhac.mauSo + psKhac.tuSo * this.mauSo
        val newMauSo = this.mauSo * psKhac.mauSo
        val ketQua = PhanSo(newTuSo, newMauSo)
        ketQua.rutGon()
        return ketQua
    }

    override fun compareTo(other: PhanSo): Int {
        val val1 = this.tuSo.toLong() * other.mauSo
        val val2 = other.tuSo.toLong() * this.mauSo
        return val1.compareTo(val2)
    }
}

fun main() {
    print("Nhập số lượng phân số: ")
    val n = readln().toInt()
    val mangPhanSo = Array(n) {
        println("Nhập phân số thứ ${it + 1}:")
        PhanSo().apply { nhap() }
    }

    println("\nMảng phân số vừa nhập:")
    println(mangPhanSo.joinToString())

    mangPhanSo.forEach { it.rutGon() }
    println("\nMảng sau khi rút gọn:")
    println(mangPhanSo.joinToString())

    val tong = mangPhanSo.reduce { acc, phanSo -> acc + phanSo }
    println("\nTổng các phân số là: $tong")

    val max = mangPhanSo.maxOrNull()
    println("\nPhân số lớn nhất là: $max")

    mangPhanSo.sort()
    println("\nMảng sau khi sắp xếp tăng dần:")
    println(mangPhanSo.joinToString())
}