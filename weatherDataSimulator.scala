import java.text.SimpleDateFormat
import java.text.DecimalFormat

import java.sql.Timestamp

import scalaj.http.Http
import scalaj.http._

import scala.util.parsing.json._

object weatherDataSimulator  {
 def main(args: Array[String]) {

   val weather_conditions = Map("Sunny" -> Map("temp" -> (40, 15), "pressure" -> (1200, 1000), "humidity" -> (70, 55)),"Rain" -> Map("temp" -> (20, 10), "pressure" -> (1200, 1000), "humidity" -> (70, 55)),"Snow" -> Map("temp" -> (-1, -10), "pressure" -> (1050, 900), "humidity" -> (70, 55)))
   
   val stations = Map("SYD"-> "-33.86,151.21", "MEL"-> "-37.83,144.98", "BRB"-> "-27.46,153.02", "PER"-> "-31.95,115.86", "ADL"-> "-34.92,138.62", "DAR"-> "-12.41,130.87", "BAL"-> "-37.56,143.85", "CAN"-> "-35.28,149.13", "GEE"-> "-38.14,144.36", "WAR"-> "-38.36,142.49", "ALB"-> "-36.07,146.91", "GOS"-> "-33.42,151.34", "GRA"-> "-29.68,152.93")  
   
   val weather = Array("Sunny", "Rain", "Snow")
   
   val rnd = new scala.util.Random
   
   val formatter = new DecimalFormat("+###0.0;-#")
   
   def getRandDate = {
       val offset = Timestamp.valueOf("2017-11-29 00:00:00").getTime()
       val end = Timestamp.valueOf("2017-11-31 00:00:00").getTime()
       val diff = end - offset + 1
       val rand = new Timestamp(offset + (Math.random() * diff).toLong).toString    
       rand
   }
   
   def dateConversion(inputDate:String) = {
       val originFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss")
       val targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
       val output = targetFormat.format(originFormat.parse(inputDate))
       output
   }
   
   def getWeather = {
          
       val condition1 = rnd.shuffle(weather.toList).head    
       val condition          = weather_conditions(condition1)
       val (maxTemp, minTemp) = condition("temp")
       val (maxPre, minPres)  = condition("pressure")
       val (maxHum, minHum)   = condition("humidity")
       val temp = formatter.format(minTemp + rnd.nextFloat*((maxTemp - minTemp) + 1 )).toString
       val pressure1 = (minPres + rnd.nextFloat*( (maxPre - minPres) + 1 ))
       val pressure = (f"$pressure1%1.1f").toString
       val humidity = (minHum + rnd.nextInt( (maxHum - minHum) + 1 )).toString
       
       (condition1 + "|" + temp + "|" + pressure + "|" + humidity)
   }
   
   //using google api to get elevation 
   def getElevation(latlong:String) = {
   
      val elevation_base_url = "http://maps.google.com/maps/api/elevation/json"
      val url_params = "locations="+latlong
      val url = elevation_base_url + "?" + url_params
      try{  
           val json = Http(url).postForm.asString.body
            
           class CC[T] { def unapply(a:Any):Option[T] = Some(a.asInstanceOf[T]) }
           
           object M extends CC[Map[String, Any]]
           object L extends CC[List[Any]]
           object D extends CC[Double]
           
           val result = for {
               Some(M(map)) <- List(JSON.parseFull(json))
               L(results) = map("results")
               M(result1) <- results
               D(elevation) = result1("elevation")
           } yield elevation
           
           val elevation = result.head.round.toString
           elevation
         }
      catch{
           case e: NoSuchElementException => println("***Problem with Google API please run the code again****")
           System.exit(1)
           }
   }
   
   for ((iata,latlon) <- stations) {
      val ele = getElevation(latlon)
      val time = dateConversion(getRandDate)
      println(s"$iata|$latlon,$ele|$time|$getWeather") //used scala string interpolation
   }

  }
}

