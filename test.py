#!/bin/python

import requests

r = requests.post("https://api.cognitive.microsoft.com/sts/v1.0/issueToken", headers={"Content-type":"application/x-www-form-urlencoded", "Content-Length":"0","Ocp-Apim-Subscription-Key":"e4625eb7fcc64fd48b1e3094d3cf293d"})

request_id = r.headers["apim-request-id"]

access_token = r.text

payload = {"scenarios":"smd", "appid":"D4D52672-91D7-4C74-8AD8-42B1D98141A5", "locale":"en-US", "device.os":"MAC OS", "version":"3.0", "format":"json", "instanceid":"E26A08F3-C543-587E-8DE6-462EEEC18453", "requestid":request_id}
headers = {"Authorization": "Bearer {}".format(access_token)}

audio = open("RecordAudio1.wav", "rb")
r2 = requests.post("https://speech.platform.bing.com/recognize", params=payload, headers=headers,  data=audio)
with open("data.json", "w") as fin:
	fin.write(r2.json().get("results")[0].get("name"))

print r2.status_code
print r2.url

print r2.content

print access_token
