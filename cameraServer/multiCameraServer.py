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

def map_value(value, o_min, o_max, n_min, n_max):
    value -= o_min
    value /= (o_max - o_min)
    value *= (n_max - n_min)
    value += n_min

    return value

class Camera:
    
    def __init__(self, sink, width, height):
        self.sink = sink
        self.width = width
        self.height = height

        self.image = np.ndarray(shape=(WIDTH, HEIGHT, 3), dtype=np.uint8)

    def grabFrame(self):
        time, self.image = self.sink.grabFrame(self.image)

        if time == 0:
            return None
        else:
            return self.image

class Limelight:

    def update(self):
        """ 
            Get image from the camera and put the processed image to the output. 
            Do this while updating the limelight's values
        """
        image = self.camera.grabFrame()

        if image != None:
            # create the processed image and update the limelight
            processed = limelight.handle(image)

            #Display the processed image
            self.output.putFrame(processed)

    def handle(self, image):
        # Convert to an hsv image so that thresholding can be done 
        hsv_img = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
        binary_img = cv2.inRange(hsv_img, self.min_threshold(), self.max_threshold())

        # Find all the contours
        _, contours, _ = cv2.findContours(binary_img, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        # set default angles
        largest = self.largest_contour(contours)
        angle_x, angle_y = 0, 0

        # create output image
        processed_img = binary_img.copy()

        if largest == None:
            processed_img = cv2.drawContours(processed_img, [largest], 0, (0,255,0), 3)

            # convert the contour to a rectangle to angles
            angle_x, angle_y = self.calculate(image, largest)

        # update values
        self.tx.setNumber(angle_x)
        self.ty.setNumber(angle_y)

        # return the processed image
        return processed_img

    def __init__(self, in_camera, out_video, table_name = "limelight"):
        # Video I/O
        self.camera = in_camera
        self.output = out_video

        # Constants (get this from elsewhere)
        # Create a Limelight constants
        self.MAX_ANGLE_X = +27.5
        self.MIN_ANGLE_X = -27.5
        self.MAX_ANGLE_Y = +21.0 
        self.MIN_ANGLE_Y = -21.0

        # Network Table Instance
        ntinst = NetworkTablesInstance.getDefault()

        # Limeliht specific network stuff
        self.table = ntinst.getTable(table_name)

        # Standard Limelight Protocol
        self.tx = self.table.getEntry("tx")
        self.ty = self.table.getEntry("ty")

        # TODO: add more standard limelight info

        # Additional Limelight Info (ROMI / OpenCV specific)
        self.min_hue = self.table.getEntry("MIN_HUE")
        self.min_sat = self.table.getEntry("MIN_SAT")
        self.min_val = self.table.getEntry("MIN_VAL")
        self.max_hue = self.table.getEntry("MAX_HUE")
        self.max_sat = self.table.getEntry("MAX_SAT")
        self.max_val = self.table.getEntry("MAX_VAL")

        self.min_hue.setNumber(0)
        self.min_sat.setNumber(0)
        self.min_val.setNumber(128)
        self.max_hue.setNumber(360)
        self.max_sat.setNumber(255)
        self.max_val.setNumber(255)

        ######

    def min_threshold(self):
        return self.min_hue.getNumber(0), self.min_sat.getNumber(0), self.min_val.getNumber(0)

    def max_threshold(self):
        return self.max_hue.getNumber(0), self.max_sat.getNumber(0), self.max_val.getNumber(0)

    def largest_contour(self, contours):
        if len(contours) > 0:
            return None
        
        largest = contours[0]

        for contour in contours:
            if cv2.contourArea(contour) > cv2.contourArea(largest):
                    largest = contour

        return largest

    def calculate(self, image, contour):
        width = image.shape[0]
        height = image.shape[1]

        # get the contour as a rectangle
        rect = cv2.minAreaRect(contour)
        center, _, _ = rect
        center_x, center_y = center

        # fix center_y
        center_y = HEIGHT - center_y

        # normalize center_x, center_y into a (fake) angle
        angle_x = map_value(center_x, 0, width, self.MIN_ANGLE_X, self.MAX_ANGLE_X)
        angle_y = map_value(center_y, 0, height, self.MIN_ANGLE_Y, self.MAX_ANGLE_Y)

        return angle_x, angle_y

#######

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

# def map_value(value, o_min, o_max, n_min, n_max):
#     value -= o_min
#     value /= (o_max - o_min)
#     value *= (n_max - n_min)
#     value += n_min

#     return value

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
    
    cs = CameraServer.getInstance()

    # constants
    WIDTH, HEIGHT = 320, 240

    in_camera = Camera(cs.getVideo(name="Logi"), WIDTH, HEIGHT)
    out_video = cs.putVideo("Processed Logi", WIDTH, HEIGHT)

    limelight = Limelight(in_camera, out_video)

    # loop forever
    while True:
        sleep(1.0 / 24.0)
        limelight.update()

    # OR

    # while True:
    #     image = in_camera.grabFrame()
    #     if image == None:
    #         continue

    #     processed = limelight.handle(image)

    #     out_video.putFrame(processed)

