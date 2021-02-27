#!/usr/bin/env python3

# Copyright (c) FIRST and other WPILib contributors.
# Open Source Software; you can modify and/or share it under the terms of
# the WPILib BSD license file in the root directory of this project.

import json
from time import sleep
import sys

import cv2
import numpy as np

from cscore import CameraServer, VideoSource, UsbCamera, MjpegServer
from networktables import NetworkTablesInstance

#   JSON format:
#   {
#       "team": <team number>,
#       "ntmode": <"client" or "server", "client" if unspecified>
#       "cameras": [
#           {
#               "name": <camera name>
#               "path": <path, e.g. "/dev/video0">
#               "pixel format": <"MJPEG", "YUYV", etc>   // optional
#               "width": <video mode width>              // optional
#               "height": <video mode height>            // optional
#               "fps": <video mode fps>                  // optional
#               "brightness": <percentage brightness>    // optional
#               "white balance": <"auto", "hold", value> // optional
#               "exposure": <"auto", "hold", value>      // optional
#               "properties": [                          // optional
#                   {
#                       "name": <property name>
#                       "value": <property value>
#                   }
#               ],
#               "stream": {                              // optional
#                   "properties": [
#                       {
#                           "name": <stream property name>
#                           "value": <stream property value>
#                       }
#                   ]
#               }
#           }
#       ]
#       "switched cameras": [
#           {
#               "name": <virtual camera name>
#               "key": <network table key used for selection>
#               // if NT value is a string, it's treated as a name
#               // if NT value is a double, it's treated as an integer index
#           }
#       ]
#   }

configFile = "/boot/frc.json"

class CameraConfig: pass

team = None
server = False
cameraConfigs = []
switchedCameraConfigs = []
cameras = []

def parseError(str):
    """Report parse error."""
    print("config error in '" + configFile + "': " + str, file=sys.stderr)

def readCameraConfig(config):
    """Read single camera configuration."""
    cam = CameraConfig()

    # name
    try:
        cam.name = config["name"]
    except KeyError:
        parseError("could not read camera name")
        return False

    # path
    try:
        cam.path = config["path"]
    except KeyError:
        parseError("camera '{}': could not read path".format(cam.name))
        return False

    # stream properties
    cam.streamConfig = config.get("stream")

    cam.config = config

    cameraConfigs.append(cam)
    return True

def readSwitchedCameraConfig(config):
    """Read single switched camera configuration."""
    cam = CameraConfig()

    # name
    try:
        cam.name = config["name"]
    except KeyError:
        parseError("could not read switched camera name")
        return False

    # path
    try:
        cam.key = config["key"]
    except KeyError:
        parseError("switched camera '{}': could not read key".format(cam.name))
        return False

    switchedCameraConfigs.append(cam)
    return True

def readConfig():
    """Read configuration file."""
    global team
    global server

    # parse file
    try:
        with open(configFile, "rt", encoding="utf-8") as f:
            j = json.load(f)
    except OSError as err:
        print("could not open '{}': {}".format(configFile, err), file=sys.stderr)
        return False

    # top level must be an object
    if not isinstance(j, dict):
        parseError("must be JSON object")
        return False

    # team number
    try:
        team = j["team"]
    except KeyError:
        parseError("could not read team number")
        return False

    # ntmode (optional)
    if "ntmode" in j:
        str = j["ntmode"]
        if str.lower() == "client":
            server = False
        elif str.lower() == "server":
            server = True
        else:
            parseError("could not understand ntmode value '{}'".format(str))

    # cameras
    try:
        cameras = j["cameras"]
    except KeyError:
        parseError("could not read cameras")
        return False
    for camera in cameras:
        if not readCameraConfig(camera):
            return False

    # switched cameras
    if "switched cameras" in j:
        for camera in j["switched cameras"]:
            if not readSwitchedCameraConfig(camera):
                return False

    return True

def startCamera(config):
    """Start running the camera."""
    print("Starting camera '{}' on {}".format(config.name, config.path))
    inst = CameraServer.getInstance()
    camera = UsbCamera(config.name, config.path)
    server = inst.startAutomaticCapture(camera=camera, return_server=True)

    camera.setConfigJson(json.dumps(config.config))
    camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen)

    if config.streamConfig is not None:
        server.setConfigJson(json.dumps(config.streamConfig))

    return camera

def startSwitchedCamera(config):
    """Start running the switched camera."""
    print("Starting switched camera '{}' on {}".format(config.name, config.key))
    server = CameraServer.getInstance().addSwitchedCamera(config.name)

    def listener(fromobj, key, value, isNew):
        if isinstance(value, float):
            i = int(value)
            if i >= 0 and i < len(cameras):
              server.setSource(cameras[i])
        elif isinstance(value, str):
            for i in range(len(cameraConfigs)):
                if value == cameraConfigs[i].name:
                    server.setSource(cameras[i])
                    break

    NetworkTablesInstance.getDefault().getEntry(config.key).addListener(
        listener,
        NetworkTablesInstance.NotifyFlags.IMMEDIATE |
        NetworkTablesInstance.NotifyFlags.NEW |
        NetworkTablesInstance.NotifyFlags.UPDATE)

    return server

def map_value(value, o_min, o_max, n_min, n_max):
    value -= o_min
    value /= (o_max - o_min)
    value *= (n_max - n_min)
    value += n_min

    return value

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        configFile = sys.argv[1]

    # read configuration
    if not readConfig():
        sys.exit(1)

    # start NetworkTables
    ntinst = NetworkTablesInstance.getDefault()
    if server:
        print("Setting up NetworkTables server")
        ntinst.startServer()
    else:
        print("Setting up NetworkTables client for team {}".format(team))
        ntinst.startClientTeam(team)
        ntinst.startDSClient()

    # start cameras
    for config in cameraConfigs:
        cameras.append(startCamera(config))

    # start switched cameras
    for config in switchedCameraConfigs:
        startSwitchedCamera(config)

    limelight_table = ntinst.getTable("limelight")
    limelight_tx = limelight_table.getEntry("tx")
    limelight_ty = limelight_table.getEntry("ty")

    
    limelight_min_hue = limelight_table.getEntry("MIN_HUE")
    limelight_min_sat = limelight_table.getEntry("MIN_SAT")
    limelight_min_val = limelight_table.getEntry("MIN_VAL")
    limelight_max_hue = limelight_table.getEntry("MAX_HUE")
    limelight_max_sat = limelight_table.getEntry("MAX_SAT")
    limelight_max_val = limelight_table.getEntry("MAX_VAL")

    limelight_min_hue.setNumber(0)
    limelight_min_sat.setNumber(0)
    limelight_min_val.setNumber(128)
    limelight_max_hue.setNumber(360)
    limelight_max_sat.setNumber(255)
    limelight_max_val.setNumber(255)
    
    cs = CameraServer.getInstance()

    # constants
    WIDTH, HEIGHT = 320, 240
    MAX_ANGLE_X = +27.5
    MIN_ANGLE_X = -27.5
    
    MAX_ANGLE_Y = +21.0 
    MIN_ANGLE_Y = -21.0

    output = cs.putVideo("Processed Logi", WIDTH, HEIGHT)
    sink = cs.getVideo(name="Logi")


    input_img = np.ndarray(shape=(WIDTH, HEIGHT, 3), dtype=np.uint8)
     
    # loop forever
    while True:
        sleep(1.0 / 24.0)

        time, input_img = sink.grabFrame(input_img)

        if time == 0: # There is an error
            continue
        
        min_thresholds = (limelight_min_hue.getNumber(0), limelight_min_sat.getNumber(0), limelight_min_val.getNumber(0))
        max_thresholds = (limelight_max_hue.getNumber(0), limelight_max_sat.getNumber(0), limelight_max_val.getNumber(0))

        # conver to hsv to threshold, and then get a thresholded binary image
        hsv_img = cv2.cvtColor(input_img, cv2.COLOR_BGR2HSV)
        binary_img = cv2.inRange(hsv_img, min_thresholds, max_thresholds)

        # find ALL the contours with the binary image
        _, contours, _ = cv2.findContours(binary_img, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        
        # create a processed image
        processed_img = binary_img.copy()
        
        # find the largest contour
        if len(contours) > 0:
            largest = contours[0]
            
            for contour in contours:
                if cv2.contourArea(contour) > cv2.contourArea(largest):
                    largest = contour

            # if there is a contour, draw the largest one
            processed_img = cv2.drawContours(processed_img, [largest], 0, (0,255,0), 3)
            
            # convert the contour to a rectangle to angles
            rect = cv2.minAreaRect(largest)
            center, _, _ = rect
            center_x, center_y = center

            # fix center_y
            center_y = HEIGHT - center_y

            # normalize center_x, center_y into a (fake) angle
            angle_x = map_value(center_x, 0, WIDTH, MIN_ANGLE_X, MAX_ANGLE_X)
            angle_y = map_value(center_y, 0, HEIGHT, MIN_ANGLE_Y, MAX_ANGLE_Y)

            limelight_tx.setNumber(angle_x)
            limelight_ty.setNumber(angle_y)
        else:
            limelight_tx.setNumber(0)
            limelight_ty.setNumber(0)
        # draw the original image with the contour
        output.putFrame(processed_img)

