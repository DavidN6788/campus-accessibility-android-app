import os

def pytest_sessionstart(session):
    for filename in ["db.sqlite", "db.sqlite-shm", "db.sqlite-wal"]:
        if os.path.exists(filename):
            os.remove(filename)
        assert not os.path.isfile(filename)