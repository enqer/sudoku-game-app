package com.example.enqer.sudoku


class Sudoku internal constructor(// number of columns/rows.
    var N: Int, // No. Of missing digits
    var K: Int
) {
    var fullMat: Array<IntArray>
    var mat: Array<IntArray>
    var SRN // square root of N
            : Int

    // Sudoku Generator
    fun fillValues() {
        // Fill the diagonal of SRN x SRN matrices
        fillDiagonal()

        // Fill remaining blocks
        fillRemaining(0, SRN)

//        fullMat = mat.copyOf()
        for (i in 0 until N) {
            for (j in 0 until N) {
               fullMat[i][j] = mat[i][j]
            }
        }
        // Remove Randomly K digits to make game
        removeKDigits()
    }

    // Fill the diagonal SRN number of SRN x SRN matrices
    fun fillDiagonal() {
        var i = 0
        while (i < N) {
            // for diagonal box, start coordinates->i==j
            fillBox(i, i)
            i = i + SRN
        }
    }

    // Returns false if given 3 x 3 block contains num.
    fun unUsedInBox(rowStart: Int, colStart: Int, num: Int): Boolean {
        for (i in 0 until SRN) for (j in 0 until SRN) if (mat[rowStart + i][colStart + j] == num) return false
        return true
    }

    // Fill a 3 x 3 matrix.
    fun fillBox(row: Int, col: Int) {
        var num: Int
        for (i in 0 until SRN) {
            for (j in 0 until SRN) {
                do {
                    num = randomGenerator(N)
                } while (!unUsedInBox(row, col, num))
                mat[row + i][col + j] = num
            }
        }
    }

    // Random generator
    fun randomGenerator(num: Int): Int {
        return Math.floor(Math.random() * num + 1).toInt()
    }

    // Check if safe to put in cell
    fun CheckIfSafe(i: Int, j: Int, num: Int): Boolean {
        return unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i % SRN, j - j % SRN, num)
    }

    // check in the row for existence
    fun unUsedInRow(i: Int, num: Int): Boolean {
        for (j in 0 until N) if (mat[i][j] == num) return false
        return true
    }

    // check in the row for existence
    fun unUsedInCol(j: Int, num: Int): Boolean {
        for (i in 0 until N) if (mat[i][j] == num) return false
        return true
    }

    // A recursive function to fill remaining
    // matrix
    fun fillRemaining(i: Int, j: Int): Boolean {
        //  System.out.println(i+" "+j);
        var i = i
        var j = j
        if (j >= N && i < N - 1) {
            i = i + 1
            j = 0
        }
        if (i >= N && j >= N) return true
        if (i < SRN) {
            if (j < SRN) j = SRN
        } else if (i < N - SRN) {
            if (j == (i / SRN) * SRN) j = j + SRN
        } else {
            if (j == N - SRN) {
                i = i + 1
                j = 0
                if (i >= N) return true
            }
        }
        for (num in 1..N) {
            if (CheckIfSafe(i, j, num)) {
                mat[i][j] = num
                if (fillRemaining(i, j + 1)) return true
                mat[i][j] = 0
            }
        }
        return false
    }



    // Remove the K no. of digits to
    // complete game
    fun removeKDigits() {
        var count = K
//        while (count != 0) {
//            val cellId = randomGenerator(N * N) - 1
//
//            // System.out.println(cellId);
//            // extract coordinates i  and j
//            val i = cellId / N
//            var j = cellId % 9
//            if (j != 0) j -= 1
//
//            // System.out.println(i+" "+j);
//            if (mat[i][j] != 0) {
//                count--
//                mat[i][j] = 0
//            }
//        }
        while (count !=0){
            var i = (0 until N).random()
            var j = (0 until N).random()

            if (mat[i][j] != 0) {
                count--
                mat[i][j] = 0
            }
        }
    }


    // Print sudoku
    fun printSudoku() {
        for (i in 0 until N) {
            for (j in 0 until N) print(mat[i][j].toString() + " ")
            println()
        }
        println()
    }

//    companion object {
//        // Driver code
//        @JvmStatic
//        fun main(args: Array<String>) {
//            val N = 9
//            val K = 20
//            val sudoku = Sudoku(N, K)
//            sudoku.fillValues()
//            sudoku.printSudoku()
//        }
//    }

    // Constructor
    init {
        K = K

        // Compute square root of N
        val SRNd = Math.sqrt(N.toDouble())
        SRN = SRNd.toInt()
        mat = Array(N) { IntArray(N) }
        fullMat = Array(N) { IntArray(N) }
        fillValues()
    }
}