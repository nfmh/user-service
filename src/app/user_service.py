from flask import Flask, request, jsonify, make_response
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity, set_access_cookies, unset_jwt_cookies
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
import os
from flask_wtf.csrf import CSRFProtect
import sys
sys.path.append("src")
from services.user_service import find_user_by_username, create_user  # Import the service functions

app = Flask(__name__)

# Enable CSRF protection conditionally based on the environment
csrf = CSRFProtect()

app.config['JWT_SECRET_KEY'] = os.getenv('JWT_SECRET_KEY')  
app.config['JWT_TOKEN_LOCATION'] = ['cookies']  # Store JWTs in cookies
app.config['JWT_COOKIE_CSRF_PROTECT'] = True  # Enable CSRF protection on JWT cookies
app.config['JWT_ACCESS_COOKIE_PATH'] = '/'  # Cookie path
app.config['JWT_COOKIE_SECURE'] = os.getenv('FLASK_ENV') == 'production'  # Use secure cookies in production
app.config['JWT_COOKIE_SAMESITE'] = 'Lax'  # SameSite can be 'Lax', 'Strict', or 'None'

app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Only initialize CSRF if the app is NOT in testing mode
app.config['TESTING'] = os.getenv('FLASK_ENV') == 'testing'
if not app.config['TESTING']:
    csrf.init_app(app)  # Enable CSRF protection only for non-test environments

jwt = JWTManager(app)
db = SQLAlchemy(app)

# Set different CORS policies based on environment
if os.getenv('FLASK_ENV') == 'production':
    CORS(app, resources={r"/*": {"origins": "https://my-frontend-domain.com"}}) 
else:
    # In testing/development environments, restrict CORS more than a wildcard
    allowed_origins = os.getenv('ALLOWED_ORIGINS', '*')
    CORS(app, resources={r"/*": {"origins": allowed_origins}})

# Initialize the database
with app.app_context():
    db.create_all()

# Routes using the service layer
@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    existing_user = find_user_by_username(data['username'])

    if existing_user:
        return jsonify(message="User already exists"), 400

    create_user(data['username'], data['password'])
    return jsonify(message="User registered"), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    user = find_user_by_username(data['username'])

    if user and bcrypt.checkpw(data['password'].encode('utf-8'), user.password.encode('utf-8')):
        access_token = create_access_token(identity={'username': user.username})

        # Set JWT in a cookie
        response = make_response(jsonify(message="Login successful"))
        set_access_cookies(response, access_token)
        return response
    return jsonify(message="Invalid credentials"), 401
