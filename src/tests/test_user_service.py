import pytest
import sys
sys.path.append("src")

from app.user_service import app, db  # Update the import path
import os

@pytest.fixture
def client():
    # Enable testing mode and explicitly disable CSRF protection for the test cases
    app.config['TESTING'] = True
    app.config['WTF_CSRF_ENABLED'] = False 
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
    # Register user
    client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    
    # Login user and check if the JWT is set in cookies
    response = client.post('/login', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    assert response.status_code == 200
    assert 'Set-Cookie' in response.headers  # Check if JWT is set in cookies

def test_profile(client):
    # Register and login user
    client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    login_response = client.post('/login', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    
    # Extract JWT from cookies
    access_cookie = login_response.headers.get('Set-Cookie')
    assert access_cookie is not None  # Ensure that the cookie was set
    
    # Send request to protected route with cookie
    profile_response = client.get('/profile', headers={'Cookie': access_cookie})
    assert profile_response.status_code == 200
    assert profile_response.json['logged_in_as']['username'] == 'john'
