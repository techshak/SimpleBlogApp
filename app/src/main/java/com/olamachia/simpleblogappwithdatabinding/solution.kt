package com.olamachia.simpleblogappwithdatabinding

import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap




        fun sortCsvColumns(csvFileContent: String): String {
            val columns: MutableMap<Int, String> = HashMap()
            getColumnNames(csvFileContent, columns)
            val sortedColumns = sortMapByValue(columns)
            return sortColumns(sortedColumns, csvFileContent)
        }

        private fun getColumnNames(csvFileContent: String, columns: MutableMap<Int, String>) {
            val pattern: Pattern = Pattern.compile("([^;\\n]+)([;\\n])")
            val matcher: Matcher = pattern.matcher(csvFileContent)
            var i = 0
            while (matcher.find() && !matcher.group(2).equals("\n")) {
                columns[i] = matcher.group(1)
                i++
            }
            columns[i] = matcher.group(1)

	}
        private fun sortMapByValue(unsortedMap: Map<Int, String>): IntArray {
            val list: LinkedList<Map.Entry<Int, String>> = LinkedList<Map.Entry<Int, String>>(
                unsortedMap.entries as Collection<Map.Entry<Int, String>?>
            )
            list.sortWith(Comparator { o1, o2 -> o1.value.lowercase().compareTo(o2.value.lowercase()) })
            val sortedColumns = IntArray(unsortedMap.size)
            var i = 0
            for ((key) in list) {
                sortedColumns[i] = key
                i++
            }
            return sortedColumns
        }

        private fun sortColumns(sortedColumns: IntArray, csvFileContent: String): String {
            val resultString = StringBuffer()
            val csvElements: ArrayList<Array<String>> = ArrayList<Array<String>>(
                listOf(
                    csvFileContent.split("[;\\n]".toRegex()).toTypedArray()
                )
            )


            for (j in 0 until csvElements.size / sortedColumns.size) {
                for (i in sortedColumns.indices) {
                    resultString.append(csvElements[j * sortedColumns.size + sortedColumns[i]])
                    if (i < sortedColumns.size - 1) {
                        resultString.append(";")
                    }
                }
                if (j < csvElements.size / sortedColumns.size - 1) {
                    resultString.append("\n")
                }
            }
            return resultString.toString()
        }
fun main(){
    sortCsvColumns("Berth,Adam,Eric\n"
    +"1010,347,6700"+"12,5,78")
}


