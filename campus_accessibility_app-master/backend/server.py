from flask import Flask, request, g
from werkzeug.security import generate_password_hash, check_password_hash
import dataset
import time
import binascii
import os

from shortest_path import shortest_path
from Building_Desc import buildings

# TODO: Encrypt data
# TODO: Make tokens expire?

app = Flask(__name__)
db = dataset.connect("sqlite:///db.sqlite")

usersdbtable = db["users"]
reportsdbtable = db["reports"]
tokensdbtable = db["tokens"]

def get_username(token):
    result = tokensdbtable.find_one(token=token)
    if not result:
        return result
    else:
        return result["username"]

@app.before_request
def check_login_before_request():
    if request.path not in ["/login", "/register"]:
        if "token" not in request.headers or not get_username(request.headers["token"]):
            return {"success": False, "error":"You are not logged in"}, 401
        g.username = get_username(request.headers["token"])

@app.route("/register", methods=["POST"])
def register():
    data = request.json
    if not ("username" in data and "password" in data and "accessibilitylevel" in data):
        return {"success": False, "error":"'username', 'password' and 'accessibilitylevel' fields need to be present"}, 400
    username, password, accessibilitylevel = data["username"], data["password"], data["accessibilitylevel"]

    if usersdbtable.find_one(username=username) is not None:
        return {"success": False, "error":f"Username '{username}' is already registered"}, 400
    accessibilitylevel = generate_password_hash(str(accessibilitylevel))
    usersdbtable.insert(dict(username=username, password=generate_password_hash(password), accessibilitylevel=accessibilitylevel))
    return {"success": True}

@app.route("/login", methods=["POST"])
def login():
    data = request.json
    if not ("username" in data and "password" in data):
        return {"success": False, "error":"'username' and 'password' fields need to be present"}, 400
    username, password = data["username"], data["password"]

    user = usersdbtable.find_one(username=username)
    if user is None:
        return {"success": False, "error":f"Username '{username}' does not exist"}, 400
    if not check_password_hash(user["password"], password):
        return {"success": False, "error":f"Your password is incorrect"}, 400

    token = binascii.hexlify(os.urandom(20)).decode()
    tokensdbtable.insert(dict(username=username, token=token, timestamp=time.time()))
    return {"success": True, "token": token}

@app.route("/changepassword", methods=["POST"])
def changepassword():
    data = request.json
    if "password" not in data:
        return {"success": False, "error":"'password' field need to be present"}, 400
    usersdbtable.update(dict(username=g.username, password=generate_password_hash(data["password"])), ["username"])
    return {"success": True}

@app.route("/submitaveragespeed", methods=["POST"])
def submitaveragespeed():
    data = request.json
    if "averagespeed" not in data:
        return {"success": False, "error":"'averagespeed' field need to be present"}, 400
    usersdbtable.update(dict(username=g.username, averagespeed=data["averagespeed"]), ["username"])
    return {"success": True}

@app.route("/submitaccessibilitylevel", methods=["POST"])
def submitaccessibilitylevel():
    data = request.json
    if "accessibilitylevel" not in data:
        return {"success": False, "error":"'accessibilitylevel' field need to be present"}, 400
    usersdbtable.update(dict(username=g.username, accessibilitylevel=generate_password_hash(str(data["accessibilitylevel"]))), ["username"])
    return {"success": True}
    
def decodeaccessibilitylevel(hashedlevel):
    accessibilitylevel = None
    for i in range(1, 6):
        if check_password_hash(hashedlevel, str(i)):
            accessibilitylevel = i
    return accessibilitylevel

@app.route("/getaccountdetails")
def getaccountdetails():
    result = usersdbtable.find_one(username=g.username)
    accessibilitylevel = decodeaccessibilitylevel(result["accessibilitylevel"])
    if accessibilitylevel == None:
        raise Exception(accessibilitylevel)
    return {"success": True, "username":result["username"], "accessibilitylevel":accessibilitylevel, "averagespeed":result["averagespeed"]}

@app.route("/isloggedin")
def isloggedin():
    return {"success": True}

@app.route("/submitreport", methods=["POST"])
def submitreport():
    # TODO: Maybe a type field?
    data = request.json
    if not ("startNode" in data and "endNode" in data and "comment" in data):
        return {"success": False, "error":"'startNode', 'endNode' and 'comment' fields need to be present"}, 400
    if "timestamp" not in data:
        data["timestamp"] = time.time()
    data["username"] = g.username
    reportsdbtable.insert(data)
    return {"success": True}

@app.route("/getreports", methods=["GET"])
def getreports():
    # TODO: No tests for this currently
    for report in reportsdbtable:
        if time.time()-report["timestamp"] > 60*60*24:
            reportsdbtable.delete(id=report["id"])
    reports = [{k: v for k, v in d.items() if k != "id"} for d in reportsdbtable]
    return {"success":True, "reports":reports}

@app.route("/calculateroute", methods=["POST"])
def calculateroute():
    # TODO: Incomplete tests for this currently
    data = request.json
    if not ("startNode" in data and "endNode" in data and "reported" in data):
        return {"success": False, "error":"'startNode', 'endNode' and 'reported' fields need to be present"}, 400
    startNode, endNode, reported = int(data["startNode"]), int(data["endNode"]), data["reported"]
    if startNode == endNode:
        return {"success": False, "error":"No route needed, the start and destination are the same"}, 400
    accessibilitylevel = decodeaccessibilitylevel(usersdbtable.find_one(username=g.username)["accessibilitylevel"])
    data = shortest_path(startNode, endNode, accessibilitylevel, reported)
    if data is not None:
        data["success"] = True
        return data
    else:
        return {"success": False, "error":"A route to the destination is impossible"}, 400

@app.route("/getbuildingsinformation", methods=["GET"])
def getbuildingsinformation():
    # TODO: No tests for this currently
    return {"success":True, "buildings":buildings}

if __name__ == "__main__":
    app.run(port=10000, debug=True)
