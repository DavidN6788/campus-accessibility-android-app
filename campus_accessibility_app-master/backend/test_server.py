import pytest
from server import app
import json
import itertools
import dataset
import time

def missingfields(fulldict):
    fields = list(fulldict.keys())
    for combo in itertools.chain.from_iterable(itertools.combinations(fields, r) for r in range(len(fields))):
        newdict = {key:fulldict[key] for key in combo}
        yield newdict

@pytest.fixture()
def client():
    app.config.update({"TESTING": True})
    return app.test_client()

def test_0_1(client):
    # Test every possible route (apart from 'login' and 'register') with no token and an invalid token
    for rule in app.url_map.iter_rules():
        if not rule.rule.startswith("/static") and rule.rule not in ["/login", "/register"]:
            if "GET" in rule.methods and "POST" not in rule.methods:
                response = client.get(rule.rule, headers={})
                assert response.json == {"success": False, "error": "You are not logged in"}
                response = client.get(rule.rule, headers={"token":"invalidtoken"})
                assert response.json == {"success": False, "error": "You are not logged in"}
            elif "POST" in rule.methods and "GET" not in rule.methods:
                response = client.post(rule.rule, headers={})
                assert response.json == {"success": False, "error": "You are not logged in"}
                response = client.post(rule.rule, headers={"token":"invalidtoken"})
                assert response.json == {"success": False, "error": "You are not logged in"}
            else:
                raise Exception(rule)

def test_1_1(client):
    # Test attempt to register with missing fields
    for data in missingfields({"username":"test", "password":"test", "accessibilitylevel":5}):
        headers = {"Content-Type":"application/json"}
        response = client.post("/register", data=json.dumps(data), headers=headers)
        assert response.json == {"success": False, "error":"'username', 'password' and 'accessibilitylevel' fields need to be present"}

def test_1_2(client):
    # Test registering a new user (with the username NOT already existing)
    data = {"username":"test", "password":"test", "accessibilitylevel":5}
    headers = {"Content-Type":"application/json"}
    response = client.post("/register", data=json.dumps(data), headers=headers)
    assert response.json == {"success": True}

def test_1_3(client):
    # Test registering a new user (with the username already existing)
    data = {"username":"test", "password":"test", "accessibilitylevel":5}
    headers = {"Content-Type":"application/json"}
    response = client.post("/register", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"Username 'test' is already registered"}

def test_2_1(client):
    # Test attempt to login with missing fields
    for data in missingfields({"username":"test", "password":"test"}):
        headers = {"Content-Type":"application/json"}
        response = client.post("/login", data=json.dumps(data), headers=headers)
        assert response.json == {"success": False, "error":"'username' and 'password' fields need to be present"}

def test_2_2(client):
    # Test attempt to login with non-existent username
    data = {"username":"notexist", "password":"notexist"}
    headers = {"Content-Type":"application/json"}
    response = client.post("/login", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"Username 'notexist' does not exist"}

def test_2_3(client):
    # Test attempt to login with wrong password
    data = {"username":"test", "password":"wrongpassword"}
    headers = {"Content-Type":"application/json"}
    response = client.post("/login", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"Your password is incorrect"}

def test_2_4(client):
    # Test attempt to login with correct username and password
    data = {"username":"test", "password":"test"}
    headers = {"Content-Type":"application/json"}
    response = client.post("/login", data=json.dumps(data), headers=headers)
    assert response.json["success"]
    assert "token" in response.json
    pytest.token = response.json["token"]

def test_3_1(client):
    # Test attempt to change average speed value without specifying new average speed value
    data = {}
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.post("/submitaveragespeed", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"'averagespeed' field need to be present"}

def test_3_2(client):
    # Test attempt to change average speed value
    data = {"averagespeed":1}
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.post("/submitaveragespeed", data=json.dumps(data), headers=headers)
    assert response.json == {"success": True}

def test_3_3(client):
    # Test attempt to change accessibility level without specifying new accessibility level
    data = {}
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.post("/submitaccessibilitylevel", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"'accessibilitylevel' field need to be present"}

def test_3_4(client):
    # Test attempt to change accessibility level
    data = {"accessibilitylevel":3}
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.post("/submitaccessibilitylevel", data=json.dumps(data), headers=headers)
    assert response.json == {"success": True}
    
def test_3_5(client):
    # Test changing the user's password without specifying a new password
    data = {}
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.post("/changepassword", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"'password' field need to be present"}

def test_3_6(client):
    # Test changing the user's password
    data = {"password":"changedpassword"}
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.post("/changepassword", data=json.dumps(data), headers=headers)
    assert response.json == {"success": True}
    
    # Then try logging in with the new password
    data = {"username":"test", "password":"changedpassword"}
    headers = {"Content-Type":"application/json"}
    response = client.post("/login", data=json.dumps(data), headers=headers)
    assert response.json["success"]
    assert "token" in response.json
    pytest.token = response.json["token"]

def test_4_1(client):
    # Test getting the user's account details
    headers = {"token":pytest.token}
    response = client.get("/getaccountdetails", headers=headers)
    assert response.json == {"success": True, "username":"test", "accessibilitylevel":3, "averagespeed":1}

def test_4_2(client):
    # Test checking if the user is logged in
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.get("/isloggedin", headers=headers)
    assert response.json == {"success": True}
    
def test_4_3(client):
    # Test getting the information about buildings
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.get("/getbuildingsinformation", headers=headers)
    assert response.json["success"]
    assert type(response.json["buildings"]) == dict

def test_5_1(client):
    # Test what happens if either the start node, destination node or comment fields are missing when submitting a report
    headers = {"Content-Type":"application/json", "token":pytest.token}
    for data in missingfields({"startNode":4, "endNode":4, "comment":"An incident has occurred on this edge with unknown consequences."}):
        response = client.post("/submitreport", data=json.dumps(data), headers=headers)
        assert response.json == {"success": False, "error":"'startNode', 'endNode' and 'comment' fields need to be present"}

def test_5_2(client):
    # Test getting the list of reports (which are empty)
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.get("/getreports", headers=headers)
    assert response.json == {"success": True, "reports": []}

def test_5_3(client):
    # Test submitting a report
    headers = {"Content-Type":"application/json", "token":pytest.token}
    data = {"startNode":4, "endNode":4, "comment":"An incident has occurred on this edge with unknown consequences"}
    response = client.post("/submitreport", data=json.dumps(data), headers=headers)
    assert response.json == {"success": True}
    
def test_5_4(client):
    # Test getting the list of reports (after one is added)
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.get("/getreports", headers=headers)
    assert response.json["success"]
    assert len(response.json["reports"]) == 1
    assert response.json["reports"][0]["startNode"] == response.json["reports"][0]["endNode"] == 4
    assert response.json["reports"][0]["username"] == "test"
    assert response.json["reports"][0]["comment"] == "An incident has occurred on this edge with unknown consequences"
    
def test_5_5(client):
    # Test getting the list of reports (which are empty because the last one is deleted by simulated the passing of time by over 24 hours)
    db = dataset.connect("sqlite:///db.sqlite")
    reportsdbtable = db["reports"]
    reportsdbtable.update({"id":1, "timestamp":time.time() - (25*60*60)}, ["id"])
    
    headers = {"Content-Type":"application/json", "token":pytest.token}
    response = client.get("/getreports", headers=headers)
    assert response.json == {"success": True, "reports": []}
    
def test_6_1(client):
    # Test what happens if either the start node, destination node or reported field is missing when calculating a route
    headers = {"Content-Type":"application/json", "token":pytest.token}
    
    for data in missingfields({"startNode":4, "endNode":4, "reported":[]}):
        response = client.post("/calculateroute", data=json.dumps(data), headers=headers)
        assert response.json == {"success": False, "error":"'startNode', 'endNode' and 'reported' fields need to be present"}

def test_6_2(client):
    # Test what happens if the start and destination nodes are the same when calculating a route
    headers = {"Content-Type":"application/json", "token":pytest.token}
    data = {"startNode":4, "endNode":4, "reported":[]}
    response = client.post("/calculateroute", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"No route needed, the start and destination are the same"}
    
def test_6_3(client):
    # Test calculating an impossible route
    headers = {"Content-Type":"application/json", "token":pytest.token}
    data = {"startNode":1, "endNode":5, "reported":[[1, 4], [1, 3], [1, 2]]}
    response = client.post("/calculateroute", data=json.dumps(data), headers=headers)
    assert response.json == {"success": False, "error":"A route to the destination is impossible"}
    
def test_6_4(client):
    # Test calculating a route
    headers = {"Content-Type":"application/json", "token":pytest.token}
    data = {"startNode":1, "endNode":5, "reported":[]}
    response = client.post("/calculateroute", data=json.dumps(data), headers=headers)
    assert response.json["success"]