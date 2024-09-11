import pytest
from app.user_service import app, db  # Import db from the user_service module
import os

@pytest.fixture
def client():
    # Enable testing mode and explicitly disable CSRF protection for the test cases
    app.config['TESTING'] = True
    app.config['WTF_CSRF_ENABLED'] = False  # Explicitly disable CSRF for tests
    app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL', 'sqlite:///:memory:')  # In-memory DB for testing
    
    # Create the tables in the test database
    with app.app_context():
        db.create_all()  # Create the database tables
    
    with app.test_client() as client:
        yield client
    
    # Drop the tables after the tests
    with app.app_context():
        db.drop_all()

def test_register(client):
    response = client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    assert response.status_code == 201

def test_login(client):
    client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    response = client.post('/login', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    assert response.status_code == 200
    assert 'token' in response.json
