import pytest
from app.user_service import app

@pytest.fixture
def client():
    with app.test_client() as client:
        yield client

def test_register(client):
    response = client.post('/register', json={'username': 'john', 'password': 'myapp123'})
    assert response.status_code == 201

def test_login(client):
    client.post('/register', json={'username': 'john', 'password': 'myapp123'})
    response = client.post('/login', json={'username': 'john', 'password': 'myapp123'})
    assert response.status_code == 200
    assert 'token' in response.json
