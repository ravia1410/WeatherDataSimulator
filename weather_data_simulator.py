#!/usr/bin/env python
import random
from datetime import datetime
from random import randint,uniform
import time
import sys
import math
import urllib, urllib2
import json

def get_humidity():
	humidity=randint(0,120)
	return humidity

def get_temperature():
	temperature = randint(0,40) -40 + 15
	return temperature

def get_pressure():
	x = 900
	y = 1050
	pressure = round(uniform(x, y), 1)
	return pressure

# function to get weather condition
def get_condition(humidity,temperature):
	if temperature>0 and humidity>95:
		return 'Rain'	
	elif temperature>0 and humidity<95:
		return 'Sunny'
	elif temperature<0 and humidity<95:
		return 'Snow'
	elif temperature<0 and humidity>95:
		return 'Snow'
	elif temperature==0 and humidity<95:
		return 'Sunny'
	elif temperature==0 and humidity>95:
		return 'Rain'

def get_localtime():
	year = randint(2015, 2016)
	month = randint(1, 12)
	day = randint(1, 28)
	hr = randint(0, 23)
	mi = randint(0, 59)
	ss = randint(0, 59)
	local_time = datetime(year, month, day, hr, mi, ss)
	return local_time

# function to fetch elevation from google elevation api (passing lat,lng as parameters)
def get_elevation(lat, lng, sensor=False):
	ELEVATION_BASE_URL = 'http://maps.google.com/maps/api/elevation/json'
	URL_PARAMS = "locations=%.7f,%.7f&sensor=%s" % (lat,lng, "false")
	url = ELEVATION_BASE_URL + "?" + URL_PARAMS
	response = urllib2.urlopen(url)
	jsondata = response.read().decode()
	data = json.loads(jsondata)
	status = data['status']
	if status == 'OK':
		result = data['results'][0]
		elevation = float(result['elevation'])
	else:
		elevation=None	
	return elevation

# Generates random weather data in pipe delimited format
def generate_random_data(lat, lon, num_rows):
    for _ in xrange(num_rows):
        hex1 = '%012x' % random.randrange(16**12) # 12 char random string
        flt = float(random.randint(0,100))
        latitude = math.acos(random.random() * 2 - 1)
        longitude = random.random() * math.pi * 2
        new_lat = lat+latitude 
        new_lng = lon+longitude
        
        # calling get_elevation function
        elevation = get_elevation(new_lat,new_lng)
        
        # calling get_humidity function
        humidity = get_humidity()
        
        # calling get_temperature function
        temperature = get_temperature()
        
        # calling get_pressure function
        pressure = get_pressure()
        
        # calling get_localtime function
        local_time = get_localtime()
        
        # calling get_condition function
        condition = get_condition(humidity,temperature)
        
        a = [(round(new_lat,2),round(new_lng,2),round(elevation,1)),local_time,condition,temperature,pressure,humidity]
        print '|'.join(map(str,a)).translate(None, '()')

if __name__ == "__main__":
	if len(sys.argv)<2:
		print "Usage details: weather_data_simulator.py <num_rows>"
		sys.exit 
	else:	 
		num = int(sys.argv[1])
		generate_random_data(-37.83, 144.98, num )