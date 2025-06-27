package com.Neitirite
import java.io.File

class Logger {
    val LOGS_DIR = File(System.getenv("LOGS_PATH").toString())
    val logFile = File("$LOGS_DIR/KotlinDBBackend.log")
    fun writeLog(msg: String) {
        if(!logFile.exists()) {
            logFile.createNewFile()
        }
        logFile.appendText("$msg \n")
    }
    fun clearLogs(){
        if(logFile.exists()) {
            logFile.writeText("")
        }
    }
}