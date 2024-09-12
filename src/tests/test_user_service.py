import pytest
import os
from app import create_app, db
from dotenv import load_dotenv

@pytest.fixture
def client():
    # Load environment variables from .env file
    load_dotenv(".env")
    
    # Create the Flask app
    app = create_app()

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

# Additional Tests

def test_invalid_register(client):
    # Register a user
    client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    
    # Attempt to register with the same username again
    response = client.post('/register', json={'username': 'john', 'password': 'newpassword'})
    assert response.status_code == 400  # Expect 400 since the user already exists
    assert response.json['message'] == "User already exists"

def test_invalid_login(client):
    # Register a user
    client.post('/register', json={'username': 'john', 'password': os.getenv('TEST_USER_PASSWORD', 'test123')})
    
    # Attempt to login with wrong password
    response = client.post('/login', json={'username': 'john', 'password': 'wrongpassword'})
    assert response.status_code == 401  # Invalid credentials
