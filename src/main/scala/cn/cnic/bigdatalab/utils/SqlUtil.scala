package cn.cnic.bigdatalab.utils

import cn.cnic.bigdatalab.entity.Schema

/**
  * Created by Flora on 2016/6/23.
  */
object SqlUtil {

  def mysql(schema: Schema): String ={
    val temp = PropertyUtil.getPropertyValue("mysql_create_sql")
    val using = PropertyUtil.getPropertyValue("mysql_driver")
    val url = PropertyUtil.getPropertyValue("mysql_url") + schema.getDb() + "?user=" +
      PropertyUtil.getPropertyValue("mysql_user") + "&password=" + PropertyUtil.getPropertyValue("mysql_password")
    val dbtable = schema.getTable()
    return temp.replace("%tablename%", dbtable).replace("%using%", using).replace("%url%", url).replace("%table%", dbtable).stripMargin
  }

  def mongo(schema: Schema): String ={
    val temp = PropertyUtil.getPropertyValue("mongo_create_sql")
    val using = PropertyUtil.getPropertyValue("mongo_driver")
    val host = PropertyUtil.getPropertyValue("mongo_host")
    val dbtable = schema.getTable()
    val db = schema.getDb()
    val columns: StringBuffer = new StringBuffer()
    for((key, value) <- schema.getColumns()){
      columns.append(key).append(" ").append(value).append(", ")
    }
    columns.delete(columns.length()-2, columns.length()-1)
    return temp.replace("%columns%", columns.toString).replace("%using%", using).replace("%host%", host).replace("%db%", db).replaceAll("%tablename%", dbtable).stripMargin
  }

  def hive(schema: Schema): String ={
    val temp = PropertyUtil.getPropertyValue("hive_create_sql")
    val dbtable = schema.getTable()
    val columns =  schema.columnsToString()
    return temp.replace("%tablename%", dbtable).replace("%columns%", columns).stripMargin
  }

  def hhase(schema: Schema): String ={
    val temp = PropertyUtil.getPropertyValue("hbase_create_sql")
    val using = PropertyUtil.getPropertyValue("hbase_driver")
    val columns = schema.columnsToString()
    val hiveDbtable = schema.getTable()
    val hbaseDbtable = schema.getTable()

    val hbaseColumns: StringBuffer = new StringBuffer()
    hbaseColumns.append(":key,")

    schema.getColumns().keySet.filter(_ != "id").foreach(key =>{
      hbaseColumns.append("cf").append(key).append(":").append(key).append(",")
    } )

    hbaseColumns.deleteCharAt(hbaseColumns.length()-1)

    return temp.replace("%tablename%", hiveDbtable).replace("%columns%", columns).
      replace("%using%", using).replace("%hbase_columns%", hbaseColumns).
      replaceAll("%hbase_tablename%", hbaseDbtable)

  }

  def solr(schema: Schema): String ={
    val temp = PropertyUtil.getPropertyValue("solr_create_sql")
    val using = PropertyUtil.getPropertyValue("solr_driver")
    val zkhost = PropertyUtil.getPropertyValue("solr_zkhost")
    val columns = schema.getColumns().keySet.mkString("", "," ,"")
    val solrDbtable = schema.getTable()

    return temp.replace("%tablename%", solrDbtable).replace("%using%", using).
      replace("%zkhost%", zkhost).replace("%columns%", columns)

  }

}
