import pytest
import os
from app import create_app, db
from app.models import Quote, Song, Movement, Mood
from flask_jwt_extended import create_access_token
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

def mock_jwt_token(app):
    """ Helper function to generate a mock JWT token. """
    with app.app_context():
        # Generate a JWT token using Flask-JWT-Extended
        access_token = create_access_token(identity={'username': 'john'})
        return access_token

# Test adding a new song
def test_add_song(client, app):
    # Generate JWT token
    jwt_token = mock_jwt_token(app)

    # Set the JWT token as a cookie
    client.set_cookie('localhost', 'access_token_cookie', jwt_token)

    # Add a new song with mood 'happy'
    data = {
        'mood': 'happy',
        'title': 'Happy Song',
        'url': 'https://happy-song-url.com'
    }
    response = client.post('/song', json=data)
    assert response.status_code == 201
    assert response.json['message'] == "Added new song"

# Test retrieving mood info
def test_get_mood_info(client, app):
    # Generate JWT token
    jwt_token = mock_jwt_token(app)

    # Set the JWT token as a cookie
    client.set_cookie('localhost', 'access_token_cookie', jwt_token)

    # Test retrieving mood info for 'happy'
    data = {'mood': 'happy'}
    response = client.post('/mood', json=data)
    assert response.status_code == 200
    assert 'quote' in response.json
    assert 'songs' in response.json
    assert 'image_url' in response.json

# Test adding song with missing data
def test_add_song_missing_data(client, app):
    # Generate JWT token
    jwt_token = mock_jwt_token(app)

    # Set the JWT token as a cookie
    client.set_cookie('localhost', 'access_token_cookie', jwt_token)

    data = {
        'mood': 'happy',
        'title': 'Incomplete Song'
        # Missing URL
    }
    response = client.post('/song', json=data)
    assert response.status_code == 400
    assert response.json['message'] == "Missing data"

# Test retrieving mood info for non-existent mood
def test_get_invalid_mood_info(client, app):
    # Generate JWT token
    jwt_token = mock_jwt_token(app)

    # Set the JWT token as a cookie
    client.set_cookie('localhost', 'access_token_cookie', jwt_token)

    data = {'mood': 'non_existent_mood'}
    response = client.post('/mood', json=data)
    assert response.status_code == 404
    assert response.json['message'] == "Mood not found"

# Test adding a song for a non-existent mood
def test_add_song_invalid_mood(client, app):
    # Generate JWT token
    jwt_token = mock_jwt_token(app)

    # Set the JWT token as a cookie
    client.set_cookie('localhost', 'access_token_cookie', jwt_token)

    data = {
        'mood': 'non_existent_mood',
        'title': 'Song with Invalid Mood',
        'url': 'https://invalid-mood-song.com'
    }
    response = client.post('/song', json=data)
    assert response.status_code == 404
    assert response.json['message'] == "Mood not found"
