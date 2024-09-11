import pytest
from app.user_service import app
import os

@pytest.fixture
def client():
    # Enable testing mode and disable CSRF protection
    app.config['TESTING'] = True  # To ensure CSRF is disabled
    with app.test_client() as client:
        yield client

def test_register(client):
    response = client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD')})
    assert response.status_code == 201

def test_login(client):
    client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD')})
    response = client.post('/login', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD')})
    assert response.status_code == 200
    assert 'token' in response.json
