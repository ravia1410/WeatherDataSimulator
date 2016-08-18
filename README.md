Here is some more detailed information about the script.

weather_data_simulator.py -  
It generates sample weather data in the following format:
latitude,longitude,elevation|local_time|condition|temperature|pressure|humidity

sample output:  
-36.6, 146.1, 124.1|2015-07-03 00:42:21|Snow|-21|930.5|62  
-36.3, 145.5, 144.7|2015-05-08 19:36:35|Sunny|13|1023.2|30  
-35.4, 150.2, 110.1|2016-04-22 19:20:21|Rain|17|989.8|107

Running:  
python weather_data_simulator.py param_no_of_rows_to_generate  
ex: python weather_data_simulator.py 10
