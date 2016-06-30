package cn.cnic.bigdatalab.utils

import ch.ethz.ssh2.ChannelCondition
import ch.ethz.ssh2.Connection
import ch.ethz.ssh2.Session
import ch.ethz.ssh2.StreamGobbler
import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.nio.charset.Charset


/**
  * Created by Flora on 2016/6/29.
  */
object SshUtil {
  private var conn: Connection = _
  private val charset: String = Charset.defaultCharset().toString
  private val TIME_OUT = 1000 * 5 * 60

  def login(ip: String, username: String, password: String): Boolean ={
    conn = new Connection(ip)
    conn.connect()
    conn.authenticateWithPassword(username, password)
  }

  def execWithParams(scriptsLocation: String, ip: String, username: String, password: String, param: String*): Unit = {
    var scriptsParams = scriptsLocation
    if(param!=null) {
      for(p <- param) {
        scriptsParams += " "+p
      }
    }
    exec(scriptsParams, ip, username, password)
  }

  def exec(scriptsLocation: String, ip: String, username: String , password: String): Int ={
//    var stdOut: InputStream = null
//    var stdErr: InputStream = null
//    var outStr: String = null
//    var outErr: String = null
    val ret = 1
    try {
      if (login(ip, username, password)) {
        val session: Session = conn.openSession()
        session.execCommand(scriptsLocation)

//        stdOut = new StreamGobbler(session.getStdout())
//        outStr = processStream(stdOut, charset)
//
//        stdErr = new StreamGobbler(session.getStderr())
//        outErr = processStream(stdErr, charset)
//
//        session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT)
//
//        println("outStr=" + outStr)
//        println("outErr=" + outErr)

//        ret = session.getExitStatus()
      } else {
        throw new Exception("登录远程机器失败" + ip)
      }
    } finally {
      if (conn != null) {
        conn.close()
      }
//      IOUtils.closeQuietly(stdOut)
//      IOUtils.closeQuietly(stdErr)
    }
    ret
  }

  private def processStream(in: InputStream, charset: String): String = {
    val buf = new Array[Byte](1024)
    val sb = new StringBuilder()
    while (in.read(buf) != -1) {
      sb.append(new String(buf, charset))
    }
    sb.toString()
  }


  def main(args: Array[String]): Unit ={
    println("executeResult=" + SshUtil.execWithParams("ls -h ", "10.0.50.216", "root", "bigdata", "/opt/dyy"))
  }

}
